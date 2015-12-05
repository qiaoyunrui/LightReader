package com.qiao.androidlab.lightreader.Activities;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewAnimationUtils;

import com.qiao.androidlab.lightreader.CameraUtil.CameraUtil;
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
    private LightPic lightPic;
    private List<LightPic> datas;
    private MyAdapter adapter;
    private Intent intent;
    private DBCtrl dbCtrl;
    private DBUtil dbUtil;
    private CameraUtil cameraUtil;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        try {
            initData();
        } catch (IOException e) {
            e.printStackTrace();
        }
        initView();
        initEvent();
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle(R.string.mainTitle);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        recyclerView.setAdapter(adapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        intent = new Intent(MainActivity.this, CameraActivity.class);
        cameraUtil = new CameraUtil(this);
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
        adapter = new MyAdapter(this, datas);
    }

    private void initView() {
        toolbar = (Toolbar) findViewById(R.id.main_toolbar);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        reveal_view = findViewById(R.id.reveal_view);
    }

    private void initEvent() {

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
//            AlertDialog builder = new AlertDialog.Builder(this)
//                    .setTitle("关于")
//                    .setIcon(R.mipmap.ic_launcher)
//                    .setMessage("NUC Andoid Lab\n乔云瑞")
//                    .setPositiveButton("确定", null)
//                    .create();
//            builder.show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}

