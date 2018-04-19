package com.zzy.net;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class BaseActivity extends AppCompatActivity {
	private ToolBarHelper mToolBarHelper ;
	public Toolbar toolbar ;
	private ImageButton btnRight;
	private ImageButton btnLeft;
	TextView tvTitle;
	private boolean isHideTitle;
	private Toast mToast = null;
	private boolean bOverly=false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	public void setOverly(boolean bOverly)
	{
		this.bOverly = bOverly;
	}

	@Override
	public void setContentView(int layoutResID) {

		if (!isHideTitle) {
			mToolBarHelper = new ToolBarHelper(this,layoutResID, toolbarLayout(),bOverly) ;
			toolbar = mToolBarHelper.getToolBar() ;
			setContentView(mToolBarHelper.getContentView());
			/*把 toolbar 设置到Activity 中*/
			setSupportActionBar(toolbar);
			/*自定义的一些操作*/
			onCreateCustomToolBar(toolbar) ;
			btnRight = (ImageButton)toolbar.findViewById(R.id.btnRight);
			btnLeft = (ImageButton)toolbar.findViewById(R.id.btnLeft);
			tvTitle = (TextView)toolbar.findViewById(R.id.tvTitle);
		}
		else {
			super.setContentView(layoutResID);
		}

	}

	@Override
	public void setTitle(int titleId) {
		setTitle(getString(titleId));
	}

	@Override
	public void setTitle(CharSequence title) {

		if (title != null) {
			title = title.toString().toUpperCase();
		}
		if (toolbar != null) {
			tvTitle.setText(title);
		}
		else {
			super.setTitle(title);
		}
	}

	public void setToolBarBackgroundColor(int res)
	{
		toolbar.setBackgroundColor(res);
	}

	public void onClickLeftButton(View view)
	{
		this.finish();
	}


	public int toolbarLayout() {
		return R.layout.layout_toolbar;
	}


	public ImageButton getLeftButton() {
		return btnLeft;
	}

	public ImageButton getRightButton() {
		return btnRight;
	}

	public void onCreateCustomToolBar(Toolbar toolbar){
		toolbar.setContentInsetsRelative(0,0);
	}

	//please call this function before call the super.onCreate
	public void hideTitle(boolean isHide){
		isHideTitle = isHide;
	}

	public void showToast(int id)
	{
		if (null == mToast)
		{
			mToast = Toast.makeText(getApplicationContext(),id, Toast.LENGTH_LONG);
		}
		else
		{
			mToast.setText(id);
		}

		mToast.show();
	}

    public static class ToolBarHelper {

        /*上下文，创建view的时候需要用到*/
        private Context mContext;

        /*base view*/
        private FrameLayout mContentView;

        /*用户定义的view*/
        private View mUserView;

        /*toolbar*/
        private Toolbar mToolBar;

        /*视图构造器*/
        private LayoutInflater mInflater;

        private int toolBarlayoutId;


        public ToolBarHelper(Context context, int layoutId, int toolBarlayoutId,boolean bOverly) {
            this.mContext = context;
            this.toolBarlayoutId = toolBarlayoutId;
            mInflater = LayoutInflater.from(mContext);
            /*初始化整个内容*/
            initContentView();
            /*初始化用户定义的布局*/
            initUserView(layoutId,bOverly);
            /*初始化toolbar*/
            initToolBar();
        }

        private void initContentView() {
            /*直接创建一个帧布局，作为视图容器的父容器*/
            mContentView = new FrameLayout(mContext);
            ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT);
            mContentView.setLayoutParams(params);

        }

        private void initToolBar() {
            /*通过inflater获取toolbar的布局文件*/
            View toolbar = mInflater.inflate(toolBarlayoutId, mContentView);
            mToolBar = (Toolbar) toolbar.findViewById(R.id.toolbar);
        }

        @SuppressWarnings("ResourceType")
        private void initUserView(int id,boolean bOverly) {
            mUserView = mInflater.inflate(id, null);
            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            int toolBarSize = (int) mContext.getResources().getDimension(R.dimen.tools_bar_height);

            params.topMargin = bOverly ? 0 : toolBarSize;
            mContentView.addView(mUserView, params);

        }

        public FrameLayout getContentView() {
            return mContentView;
        }

        public Toolbar getToolBar() {
            return mToolBar;
        }
    }
}
