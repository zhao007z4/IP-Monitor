package com.zzy.net;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AppList extends AsyncTask<Void,Void,ArrayList<AppInfo>>
{

	private PackageManager pManager = null;
	private int selfUID = 0;
	private OnAppListen onAppList;

	public AppList(Context context)
	{

		pManager = context.getApplicationContext().getPackageManager();
		PackageInfo info = null;
		try
		{
			info = pManager.getPackageInfo(context.getPackageName(), 0);
			selfUID = info.applicationInfo.uid;
		}
		catch (PackageManager.NameNotFoundException e)
		{
			e.printStackTrace();
		}
	}

	@Override
	protected void onPreExecute() {


	}

	@Override
	protected void onPostExecute(ArrayList<AppInfo> lsInfo) {

		NetApp.mInstance.setAppList(lsInfo);

		if(onAppList!=null)
		{
			onAppList.getAppList(lsInfo);
		}
	}

	public ArrayList<AppInfo> GetPackagesList()
	{
		ArrayList<AppInfo> lsApp = new ArrayList<AppInfo>();
		AppInfo appInfo = null;
		String sTmp = null;
		List<PackageInfo> packinfos = pManager.getInstalledPackages(PackageManager.GET_PERMISSIONS);
		for (PackageInfo info : packinfos)
		{
			if (info.applicationInfo.uid == selfUID)
			{
				continue;
			}

			String[] premissions = info.requestedPermissions;
			if (premissions != null && premissions.length > 0)
			{
				for (String premission : premissions)
				{
					if ("android.permission.INTERNET".equals(premission))
					{
						appInfo = new AppInfo();
						sTmp = info.applicationInfo.loadLabel(pManager).toString();
						appInfo.setAppName(sTmp);
						sTmp = HanziToPinyin.getFirstPinYin(sTmp);
						appInfo.setAllLetter(sTmp);
						appInfo.setLetter(sTmp.substring(0,1));

						appInfo.setIcon(info.applicationInfo.loadIcon(pManager));
						appInfo.setPackName(info.packageName);
						appInfo.setUid(info.applicationInfo.uid);
						appInfo.setVersion(info.versionName);
						lsApp.add(appInfo);
						break;
					}
				}
			}
		}

		Collections.sort(lsApp, new AppComparator());

		return lsApp;
	}

	@Override
	protected ArrayList<AppInfo> doInBackground(Void... voids) {
		return GetPackagesList();
	}

	public void setOnAppList(OnAppListen onAppList)
	{
		this.onAppList = onAppList;
	}


	public interface OnAppListen
	{
		public void getAppList(ArrayList<AppInfo> lsApp);
	}

}
