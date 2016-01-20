package com.qiao.androidlab.lightreader.Activities;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.widget.ProgressBar;

import com.qiao.androidlab.lightreader.Parts.LightPic;
import com.qiao.androidlab.lightreader.Parts.MyAdapter;
import com.qiao.androidlab.lightreader.R;
import com.qiao.androidlab.lightreader.db.DBCtrl;
import com.qiao.androidlab.lightreader.db.DBUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private android.support.v7.widget.RecyclerView recyclerView;
    private android.support.design.widget.FloatingActionButton fab;
    private View reveal_view;
    private ProgressBar progressBar;    //圆形进度条
    private SwipeRefreshLayout swipeRefreshLayout;
    private LightPic lightPic;
    private List<LightPic> datas;
    private MyAdapter adapter;
    private Intent intent;
    private DBCtrl dbCtrl;
    private DBUtil dbUtil;
    private Handler handler;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle(R.string.mainTitle);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        setHuillotine();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        adapter = new MyAdapter(this, new ArrayList<LightPic>());
        recyclerView.setAdapter(adapter);
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if (msg.what == 0x123) { //数据加载完毕
                    progressBar.setVisibility(View.INVISIBLE);
                    adapter.notifyDataSetChanged();
                }
            }
        };
        progressBar.setVisibility(View.VISIBLE);    //显示进度条
        new MyThread().start();   //开始线程
        initEvent();
        intent = new Intent(MainActivity.this, CameraActivity.class);
    }

    /**
     * 设置铡刀菜单
     */
    private void setHuillotine() {

    }
    /**
     * 初始化数据，从数据库中获取数据信息
     */
    private void initData() throws IOException {
        datas = new ArrayList<>();
        dbCtrl = new DBCtrl(this);  //数据库已经创建
        dbUtil = new DBUtil();
        dbUtil.mOPenorCreateDatabase(dbCtrl);
        datas = dbUtil.mDBSelect(this); //查询数据
//        adapter = new MyAdapter(this, datas);   //将数据同步到列表里
        adapter.setmDatas(datas);
    }

    private void initView() {
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        toolbar = (Toolbar) findViewById(R.id.main_toolbar);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        reveal_view = findViewById(R.id.reveal_view);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);
    }

    private void initEvent() {
        /*错误：adapter是空的，原因是：*/
        adapter.setmOnItemClickListener(new MyAdapter.OnItemClickListener() {
            @Override
            public void OnItemClick(View view, int position) {
                Snackbar.make(recyclerView, "This is " + position, Snackbar.LENGTH_SHORT).setAction("OK", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                    }
                }).show();
            }

            @Override
            public void OnItemLongClick(View view, int position) {
                dbCtrl = new DBCtrl(MainActivity.this);  //数据库已经创建
                dbUtil = new DBUtil();
                dbUtil.mOPenorCreateDatabase(dbCtrl);
                dbUtil.mDBDelete(position);
                adapter.deleteDate(position);
                Snackbar.make(recyclerView, "删除成功", Snackbar.LENGTH_SHORT).setAction("OK", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                    }
                }).show();
            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= 21) {
                    intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    int[] location = new int[2];
                    fab.getLocationOnScreen(location);
                    int centerX = location[0];
                    int centerY = location[1];
                    Animator animShow = ViewAnimationUtils.createCircularReveal(reveal_view, centerX, centerY, 0, 1920);
                    animShow.setDuration(350);
                    reveal_view.setVisibility(View.VISIBLE);
                    animShow.addListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);
                            startActivity(intent);
                            overridePendingTransition(0, 0);    //设置页面切换的效果
                        }
                    });
                    animShow.start();
                } else {
                    startActivity(intent);
                }
            }
        });

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
//                adapter.notifyDataSetChanged();     //刷新列表
                new MyThread().start();   //开始线程
                swipeRefreshLayout.setRefreshing(false);
            }
        });

    }

    @Override
    protected void onResume() {
        reveal_view.setVisibility(View.INVISIBLE);
        super.onResume();
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_about) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    class MyThread extends Thread {

        @Override
        public void run() {
            try {
                initData();
            } catch (IOException e) {
                e.printStackTrace();
            }
            Message message = new Message();
            message.what = 0x123;
            handler.sendMessage(message);
        }
    }

}

