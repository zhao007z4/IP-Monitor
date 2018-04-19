package com.zzy.net;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class DetailAdpter extends BaseAdapter
{
	private List<NetInfo> list = null;
	private LayoutInflater la;

	public DetailAdpter(Context mContext, List<NetInfo> list)
	{
		this.list = list;
		la = LayoutInflater.from(mContext);
	}

	public void updateListView(List<NetInfo> list)
	{
		this.list = list;
		notifyDataSetChanged();
	}

	public int getCount()
	{
		return this.list.size();
	}

	public NetInfo getItem(int position)
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
			pConvertView =la.inflate(R.layout.layout_detail_item, null);
		}

		NetInfo info = list.get(position);

		TextView tvLetter= pConvertView.findViewById(R.id.tvLetter);
		TextView tvName =pConvertView.findViewById(R.id.tvName);

		int type = info.getType();
		if(position>0)
		{
			if(!isSameType(type,list.get(position-1).getType() ))
			{
				tvLetter.setVisibility(View.VISIBLE);
				tvLetter.setText(getLetter(type));
			}
			else
			{
				tvLetter.setVisibility(View.GONE);
			}
		}
		else
		{
			tvLetter.setVisibility(View.VISIBLE);
			tvLetter.setText(getLetter(type));
		}
		tvName.setText(info.getAddress()+":"+info.getPort());
		return pConvertView;

	}

	private boolean isSameType(int firstType,int secondType)
	{
		if(firstType== secondType)
		{
			return true;
		}
		else
		{
			if(firstType== NetFile.TYPE_TCP && secondType == NetFile.TYPE_TCP6)
			{
				return true;
			}
			else if(firstType== NetFile.TYPE_TCP6 && secondType == NetFile.TYPE_TCP)
			{
				return true;
			}
			else if(firstType== NetFile.TYPE_UDP && secondType == NetFile.TYPE_UDP6)
			{
				return true;
			}
			else if(firstType== NetFile.TYPE_UDP6 && secondType == NetFile.TYPE_UDP)
			{
				return true;
			}
			else if(firstType== NetFile.TYPE_RAW && secondType == NetFile.TYPE_RAW6)
			{
				return true;
			}
			else if(firstType== NetFile.TYPE_RAW6 && secondType == NetFile.TYPE_RAW)
			{
				return true;
			}
			return false;
		}
	}

	private String getLetter(int type)
	{
		switch (type)
		{
			case NetFile.TYPE_TCP:
			case NetFile.TYPE_TCP6:
				return "TCP";
			case NetFile.TYPE_UDP:
			case NetFile.TYPE_UDP6:
				return "UDP";
			default:
				return "RAW";
		}
	}

}