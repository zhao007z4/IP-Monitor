package com.zzy.net;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;


public class NetService extends Service
{
	private NetFile netFile;
	@Override
	public void onCreate()
	{
		NetFile netFile = new NetFile(this);
		netFile.start();
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId)
	{
		return super.onStartCommand(intent,flags,startId);
	}

	@Override
	public boolean onUnbind(Intent intent)
	{
		return super.onUnbind(intent);
	}

	@Override
	public IBinder onBind(Intent intent)
	{
		return null;
	}

	@Override
	public void onDestroy()
	{
		super.onDestroy();
		netFile.stop();
		netFile = null;
	}
}


