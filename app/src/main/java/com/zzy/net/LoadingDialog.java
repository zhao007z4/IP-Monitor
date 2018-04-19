package com.zzy.net;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;


public class LoadingDialog extends Dialog
{
	
	private ImageView imgLoading;
	private Animation animation;
	public LoadingDialog(Context context)
	{
		this(context, R.style.CustomProgressDialog,"");
	}

	public LoadingDialog(Context context, String strMessage)
	{
		this(context, R.style.CustomProgressDialog, strMessage);
	}

	public LoadingDialog(Context context, int theme, String strMessage)
	{
		super(context, theme);
		this.setContentView(R.layout.layout_loading_dialog);
		this.getWindow().getAttributes().gravity = Gravity.CENTER;
		setCanceledOnTouchOutside(false);

		imgLoading = (ImageView)findViewById(R.id.imgLoading);
		animation = AnimationUtils.loadAnimation(context, R.anim.loading);
	}
	
	public void showDialog()
	{
		imgLoading.startAnimation(animation);
	}
	
	public void cancelDialog()
	{
		animation.cancel();
		//dismiss();
	}
	
	@Override
	public void onWindowFocusChanged(boolean hasFocus)
	{

		if (!hasFocus)
		{
			dismiss();
		}
	}

}
