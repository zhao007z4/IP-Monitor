package com.zzy.net;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class NetFile
{
	public final static int TYPE_TCP = 0;
	public final static int TYPE_TCP6 = 1;
	public final static int TYPE_UDP = 2;
	public final static int TYPE_UDP6 = 3;
	public final static int TYPE_RAW = 4;
	public final static int TYPE_RAW6 = 5;
	public final static int TYPE_MAX = 6;

	public final static int MSG_NET_CALLBACK = 1;
	public final static String MSG_NET_GET= "com.zzy.net.get";

	private Context context;
	private Handler handler;
	private NetThread netThread;

	private final static int DATA_REMOTE = 3;
	private final static int DATA_UID = 8;

	public NetFile(Context context) {
		this.context = context;
		handler = new Handler(new Handler.Callback() {
			@Override
			public boolean handleMessage(Message msg) {

				if(msg.what == MSG_NET_CALLBACK)
				{
					Intent intent = new Intent();
					intent.setAction(MSG_NET_GET);
					if(NetFile.this.context !=null) {
						NetFile.this.context.sendBroadcast(intent);
					}
				}
				return false;
			}
		});
	}

	public void start()
	{
		netThread = new NetThread(handler);
		netThread.start();
	}

	public void stop()
	{
		handler = null;
		context = null;
		netThread.stopNet();
	}

	public static class NetThread extends Thread
	{
		private  Handler handler;
		private boolean isStop;
		private StringBuilder sbBuilder = new StringBuilder();

		private NetThread(Handler handler)
		{
			this.handler = handler;
			isStop = false;
		}

		public void stopNet()
		{
			isStop = true;
			handler = null;
		}

		public void execute(String[] cmmand, String directory, int type) throws IOException
		{
			NetInfo netInfo = null;
			String sTmp = null;

			ProcessBuilder builder = new ProcessBuilder(cmmand);

			if (directory != null) {
				builder.directory(new File(directory));
			}
			builder.redirectErrorStream(true);
			Process process = builder.start();
			InputStream is = process.getInputStream();

			Scanner s = new Scanner(is);
			s.useDelimiter("\n");
			while(s.hasNextLine()){
				sTmp = s.nextLine();
				netInfo = parseDataNew(sTmp);
				if(netInfo!=null) {
					netInfo.setType(type);
					saveToList(netInfo);
				}
			}
		}

		private int strToInt(String value,int iHex,int iDefault)
		{
			int iValue = iDefault;
			if(value==null)
			{
				return iValue;
			}

			try
			{
				iValue = Integer.parseInt(value,iHex);
			}
			catch (NumberFormatException e)
			{
				e.printStackTrace();
			}

			return iValue;
		}

		private long strToLong(String value,int iHex,int iDefault)
		{
			long iValue = iDefault;
			if(value==null)
			{
				return iValue;
			}

			try
			{
				iValue = Long.parseLong(value,iHex);
			}
			catch (NumberFormatException e)
			{
				e.printStackTrace();
			}

			return iValue;
		}

		private NetInfo parseDataNew(String sData)
		{
			String sSplitItem[] = sData.split("\\s+");
			String sTmp = null;
			if(sSplitItem.length<9)
			{
				return null;
			}

			NetInfo netInfo = new NetInfo();

			sTmp = sSplitItem[DATA_REMOTE];
			String sDesItem[] = sTmp.split(":");
			if(sDesItem.length<2)
			{
				return null;
			}
			netInfo.setPort(strToInt(sDesItem[1], 16, 0));

			sTmp = sDesItem[0];
			int len = sTmp.length();
			if(len<8)
			{
				return null;
			}

			sTmp = sTmp.substring(len-8);
			netInfo.setIp(strToLong(sTmp, 16, 0));

			sbBuilder.setLength(0);
			sbBuilder.append(strToInt(sTmp.substring(6, 8), 16, 0))
					.append(".")
					.append(strToInt(sTmp.substring(4, 6), 16, 0))
					.append(".")
					.append(strToInt(sTmp.substring(2, 4), 16, 0))
					.append(".")
					.append(strToInt(sTmp.substring(0, 2), 16, 0));

			sTmp = sbBuilder.toString();
			netInfo.setAddress(sTmp);

			if(sTmp.equals("0.0.0.0")) {
				return null;
			}

			sTmp =sSplitItem[DATA_UID];
			netInfo.setUid(strToInt(sTmp, 10, 0));

			return netInfo;
		}
		
		private void saveToList(NetInfo netInfo)
		{
			if(netInfo==null)
			{
				return;
			}

			ArrayList<AppInfo> lsApp = NetApp.mInstance.getAppList();
			if (lsApp == null) {
				return;
			}

			ArrayList<NetInfo> lsNet = null;
			for(AppInfo info:lsApp)
			{
				if(info.getUid() != netInfo.getUid())
				{
					continue;
				}

				switch (netInfo.getType())
				{
					case TYPE_TCP:
					case TYPE_TCP6:
						lsNet = info.lsTcp;
						if(lsNet == null)
						{
							lsNet = new ArrayList<>();
							info.lsTcp = lsNet;
						}
						break;
					case TYPE_UDP:
					case TYPE_UDP6:
						lsNet = info.lsUdp;
						if(lsNet == null)
						{
							lsNet = new ArrayList<>();
							info.lsUdp = lsNet;
						}
						break;
					default:
						lsNet = info.lsRaw;
						if(lsNet == null)
						{
							lsNet = new ArrayList<>();
							info.lsRaw = lsNet;
						}
						break;
				}

				boolean found = false;
				for(NetInfo tmp:lsNet)
				{
					if(tmp.getPort() == netInfo.getPort() && tmp.getIp() == netInfo.getIp())
					{
						found = true;
						break;
					}
				}

				if(!found)
				{
					lsNet.add(netInfo);
				}
				break;
			}
		}

		public void read(int type)
		{
			try
			{
				switch (type)
				{
					case TYPE_TCP:
						String[] ARGS ={ "cat", "/proc/net/tcp" };
						execute(ARGS, "/",TYPE_TCP);

						break;
					case TYPE_TCP6:
						String[] ARGS1 ={ "cat", "/proc/net/tcp6" };
						execute(ARGS1, "/",TYPE_TCP6);
						break;
					case TYPE_UDP:
						String[] ARGS2 ={ "cat", "/proc/net/udp" };
						execute(ARGS2, "/",TYPE_UDP);
						break;
					case TYPE_UDP6:
						String[] ARGS3 ={ "cat", "/proc/net/udp6" };
						execute(ARGS3, "/",TYPE_UDP6);
						break;
					case TYPE_RAW:
						String[] ARGS4 ={ "cat", "/proc/net/raw" };
						execute(ARGS4, "/",TYPE_UDP);
						break;
					case TYPE_RAW6:
						String[] ARGS5 ={ "cat", "/proc/net/raw6" };
						execute(ARGS5, "/",TYPE_UDP6);
						break;

					default:
						break;
				}
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}

		private void sleep(int millis)
		{
			try
			{
				Thread.sleep(millis);
			}
			catch (InterruptedException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		@Override
		public void run()
		{
			final String PATH_TCP = "/proc/net/tcp";
			final String PATH_TCP6 ="/proc/net/tcp6";
			final String PATH_UDP = "/proc/net/udp";
			final String PATH_UDP6 ="/proc/net/udp6";
			final String PATH_RAW = "/proc/net/raw";
			final String PATH_RAW6 ="/proc/net/raw6";

			File file[] = new File[TYPE_MAX];
			file[0] = new File(PATH_TCP);
			file[1] = new File(PATH_TCP6);
			file[2] = new File(PATH_UDP);
			file[3] = new File(PATH_UDP6);
			file[4] = new File(PATH_RAW);
			file[5] = new File(PATH_RAW6);

			long lastTime[] = new long[TYPE_MAX];
			Arrays.fill(lastTime, 0);

			while(true)
			{
				if(isStop)
				{
					break;
				}

				if(NetApp.mInstance.getAppList()==null)
				{
					sleep(1000);
					continue;
				}

				for(int i=0;i<TYPE_MAX;i++)
				{
					long iTime = file[i].lastModified();
					if(iTime !=lastTime[i] )
					{
						read(i);
						lastTime[i] = iTime;
					}
				}

				if(handler != null) {
					handler.sendEmptyMessage(MSG_NET_CALLBACK);
				}

				sleep(10000);

			}
		}
	}
}
