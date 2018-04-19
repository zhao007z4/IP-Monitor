package com.zzy.net;

import java.util.Comparator;

public class AppComparator implements Comparator<AppInfo>
{

	public int compare(AppInfo o1, AppInfo o2)
	{
		if (!o1.getLetter().equals("#") && o2.getLetter().equals("#"))
		{
			return -1;
		}
		else if (o1.getLetter().equals("#") && !o2.getLetter().equals("#"))
		{
			return 1;
		} 
		else
		{
			return o1.getLetter().compareToIgnoreCase(o2.getLetter());
		}
	}

}
