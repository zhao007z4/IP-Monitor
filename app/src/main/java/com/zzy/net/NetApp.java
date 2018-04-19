package com.zzy.net;

import android.app.Application;

import java.util.ArrayList;

public class NetApp extends Application
{
	public static NetApp mInstance;
	private ArrayList<AppInfo> listAPP;

	@Override
	public void onCreate()
	{
		super.onCreate();
		mInstance = this;
		HanziToPinyin.init();
	}

	public ArrayList<AppInfo> getAppList() {
		return listAPP;
	}

	public void setAppList(ArrayList<AppInfo> lsApp) {
		listAPP = lsApp;
	}


}
