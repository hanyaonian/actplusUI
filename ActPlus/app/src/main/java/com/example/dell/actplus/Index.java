package com.example.dell.actplus;

import android.app.ProgressDialog;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.ListViewAutoScrollHelper;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AbsListView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.handmark.pulltorefresh.library.PullToRefreshListView;

import java.util.ArrayList;
import java.util.List;

public class Index extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_index);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        //headerview(viewpager ,推荐最新活动)
        PullToRefreshListView PTF_listView = (PullToRefreshListView)findViewById(R.id.PTF_listview);
        ListView listView = PTF_listView.getRefreshableView();
        View headerView = View.inflate(getApplicationContext(), R.layout.viewpager, null);
        //bug from pulltorefreshlistview. solve:http://blog.csdn.net/pk0071/article/details/50464247
        //add header view
        listView.addHeaderView(headerView);
        //初始化类
        tool = new NetTools();
        //获取初始数据
        try {
            dialog = ProgressDialog
                    .show(Index.this, "亲别急", "活动正在加载中", false);
            UpdateDataAndUI(0, 5, "allList");
            setUpViewPager();
        } catch (Exception e) {
            Log.e("On Create", e.toString());
        }
    }
    private NetTools tool;
    private List<ActItem> listData;

    //设置正在加载,progress
    private ProgressDialog dialog;
    public void setUpViewPager(){
        ViewPager viewPager = (ViewPager) findViewById(R.id.viewPager);
    }
    public void UpdateDataAndUI(final int startPage, final int pageSize, final String dataType) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    listData = tool.getList(startPage, pageSize, dataType);
                    Message message = new Message();
                    message.what = UPDATE_CONTENT;
                    message.obj = listData;
                    handler.sendMessage(message);
                } catch(Exception e) {
                    Log.i("onCreate", e.toString());
                }
            }
        }).start();
    }
    private final int UPDATE_CONTENT = 0;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case UPDATE_CONTENT:
                    List<ActItem> data = (List<ActItem>) msg.obj;
                    updateUI(data);
                    break;
                default:
                    break;
            }
        }
    };
    private void updateUI(final List<ActItem> data) {
        try {
            PullToRefreshListView listView = (PullToRefreshListView) findViewById(R.id.PTF_listview);
            Myadpter myadpter = new Myadpter(getApplicationContext(), data);
            listView.setAdapter(myadpter);
            dialog.dismiss();
        } catch (Exception e) {
            Log.e("updateUI", e.toString());
        }
    }
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.index, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
