package com.zzy.net.sortlist;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.zzy.net.AppInfo;
import com.zzy.net.R;

import java.util.List;

public class SortAdapter extends BaseAdapter
{
	private List<AppInfo> list = null;
	private LayoutInflater la;

	public SortAdapter(Context mContext, List<AppInfo> list)
	{
		this.list = list;
		la = LayoutInflater.from(mContext);
	}

	public void updateListView(List<AppInfo> list)
	{
		this.list = list;
		notifyDataSetChanged();
	}

	public int getCount()
	{
		return this.list.size();
	}

	public AppInfo getItem(int position)
	{
		return list.get(position);
	}

	public long getItemId(int position)
	{
		return position;
	}

	public View getView(int position, View pConvertView, ViewGroup parent)
	{
		if (pConvertView == null)
		{
			pConvertView =la.inflate(R.layout.layout_app_item, null);
		}
		
		AppInfo appInfo = list.get(position);
		
		TextView tvLetter= pConvertView.findViewById(R.id.tvLetter);
		TextView tvName =pConvertView.findViewById(R.id.tvName);
		ImageView imgvIcon =  pConvertView.findViewById(R.id.imgvIcon);

		int section = appInfo.getLetter().charAt(0);
		if(position>0)
		{
			if(section != list.get(position-1).getLetter().charAt(0))
			{
				tvLetter.setVisibility(View.VISIBLE);
				tvLetter.setText(appInfo.getLetter());
			}
			else
			{
				tvLetter.setVisibility(View.GONE);
			}
		}
		else
		{
			tvLetter.setVisibility(View.VISIBLE);
			tvLetter.setText(appInfo.getLetter());
		}
		tvName.setText(appInfo.getAppName());
		imgvIcon.setImageDrawable(appInfo.getIcon());

		return pConvertView;

	}

	public int getPositionForSection(int section)
	{
		for (int i = 0; i < getCount(); i++)
		{
			char firstChar = list.get(i).getLetter().charAt(0);
			if (firstChar == section)
			{
				return i;
			}
		}

		return -1;
	}
}
