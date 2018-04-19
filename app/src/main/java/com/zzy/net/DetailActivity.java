package com.zzy.net;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;


public class DetailActivity extends BaseActivity {

    private AppInfo appInfo;
    private ListView lvNet;
    private DetailAdpter detailAdpter;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_detail);
        setTitle(R.string.detail);

        Intent intent = getIntent();
        if(intent==null)
        {
            this.finish();
            return;
        }

        int index = intent.getIntExtra("index",0);
        ArrayList<AppInfo> listAPP = NetApp.mInstance.getAppList();
        if(index<0 || index>listAPP.size()-1)
        {
            this.finish();
            return;
        }
        appInfo = listAPP.get(index);

        ImageView imgvIcon = findViewById(R.id.imgvIcon);
        imgvIcon.setImageDrawable(appInfo.getIcon());
        TextView tvName = findViewById(R.id.tvName);
        tvName.setText(appInfo.getAppName());
        TextView tvPackeName = findViewById(R.id.tvPackeName);
        tvPackeName.setText(appInfo.getPackName());
        TextView tvVersion = findViewById(R.id.tvVersion);
        tvVersion.setText(appInfo.getVersion());

        lvNet = findViewById(R.id.lvNet);
        updatList();
    }

    private void updatList()
    {
        ArrayList<NetInfo> lsNet = new ArrayList<>();

        if(appInfo.lsTcp!=null) {
            lsNet.addAll(appInfo.lsTcp);
        }

        if(appInfo.lsUdp!=null) {
            lsNet.addAll(appInfo.lsUdp);
        }

        if(appInfo.lsRaw!=null) {
            lsNet.addAll(appInfo.lsRaw);
        }

        if(detailAdpter == null) {
            detailAdpter = new DetailAdpter(this,lsNet);
            lvNet.setAdapter(detailAdpter);
        }else{
            detailAdpter.updateListView(lsNet);
        }
    }

    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            return false;
        }
    });


    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            handler.removeCallbacks(runnable);
            updatList();
            handler.postDelayed(this,10000);
        }
    };

    @Override
    protected void onStop()
    {
        super.onStop();
        handler.removeCallbacks(runnable);
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        handler.postDelayed(runnable,10000);
    }

}
