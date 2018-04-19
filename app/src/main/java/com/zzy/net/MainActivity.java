package com.zzy.net;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.zzy.net.sortlist.ClearEditText;
import com.zzy.net.sortlist.SideBar;
import com.zzy.net.sortlist.SortAdapter;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity {

    private SortAdapter adpSort;
    private ListView lvApp;
    private LoadingDialog loadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_app);
        setTitle(R.string.app_name);

        loadingDialog = new LoadingDialog(this);
        loadingDialog.showDialog();
        loadingDialog.show();

        AppList appLis= new AppList(this);
        appLis.setOnAppList(new AppList.OnAppListen() {
            @Override
            public void getAppList(ArrayList<AppInfo> lsApp) {
                if(lsApp==null || lsApp.size()<1)
                {
                    return;
                }

                startService(new Intent(MainActivity.this,NetService.class));
            }
        });
        appLis.execute();


        ClearEditText etSearch= findViewById(R.id.etSearch);
        lvApp =findViewById(R.id.lvApp);
        lvApp.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                AppInfo appInfo = adpSort.getItem(position);
                List<AppInfo> lsApp = NetApp.mInstance.getAppList();
                AppInfo tmp = null;
                int index = -1;
                for(int i=0;i<lsApp.size();i++)
                {
                    tmp = lsApp.get(i);
                    if(tmp.getUid() == appInfo.getUid())
                    {
                        index = i;
                        break;
                    }
                }
                if(index==-1)
                {
                    return;
                }
                Intent intent = new Intent(MainActivity.this,DetailActivity.class);
                intent.putExtra("index",index);
                startActivity(intent);
            }
        });

        TextView tvAppHead =findViewById(R.id.tvAppHead);
        SideBar sideApp = findViewById(R.id.sideApp);
        sideApp.setTextView(tvAppHead);

        sideApp.setOnTouchingLetterChangedListener(new SideBar.OnTouchingLetterChangedListener()
        {

            @Override
            public void onTouchingLetterChanged(String s)
            {
                if(adpSort==null)
                {
                    return;
                }
                int position = adpSort.getPositionForSection(s.charAt(0));
                if (position != -1)
                {
                    lvApp.setSelection(position);
                }
            }
        });


        etSearch.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
                if(adpSort==null)
                {
                    return;
                }
                filterData(s.toString());
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after)
            {

            }

            @Override
            public void afterTextChanged(Editable s)
            {
            }
        });
    }

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            if(loadingDialog!=null && loadingDialog.isShowing()) {
                loadingDialog.cancelDialog();
                loadingDialog.dismiss();
            }
            updateList();
        }
    };

    @Override
    protected void onResume()
    {
        super.onResume();

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(NetFile.MSG_NET_GET);
        registerReceiver(broadcastReceiver,intentFilter);
    }

    @Override
    protected void onStop()
    {
        super.onStop();
        unregisterReceiver(broadcastReceiver);
    }

    private void updateList()
    {
        List<AppInfo> lsApp = NetApp.mInstance.getAppList();
        List<AppInfo> filterList = new ArrayList<>();
        for (AppInfo info : lsApp)
        {
            if ((info.lsTcp!=null && info.lsTcp.size()>0) || (info.lsUdp!=null &&info.lsUdp.size()>0) || (info.lsRaw!=null && info.lsRaw.size()>0))
            {
                filterList.add(info);
            }
        }

        if(adpSort==null) {
            adpSort = new SortAdapter( MainActivity.this, filterList);
            lvApp.setAdapter(adpSort);
        }else{
            adpSort.updateListView(filterList);
        }
    }


    private void filterData(String filter)
    {
        List<AppInfo> filterList;
        List<AppInfo> lsApp = NetApp.mInstance.getAppList();
        filter = filter.toLowerCase();
        if (TextUtils.isEmpty(filter))
        {
            filterList = lsApp;
        }
        else
        {
            filterList = new ArrayList<>();
            for (AppInfo info : lsApp)
            {
                String name = info.getAppName().toLowerCase();
                if (name.contains(filter) || info.getAllLetter().toLowerCase().contains(filter))
                {
                    filterList.add(info);
                }
            }
        }

        adpSort.updateListView(filterList);
    }
}
