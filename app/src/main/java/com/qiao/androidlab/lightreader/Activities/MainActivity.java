package com.qiao.androidlab.lightreader.Activities;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.androidlab.qiao.guillotineview.animtor.GuillotineAnimtor;
import com.qiao.androidlab.lightreader.ClickListeners.MenuSelect;
import com.qiao.androidlab.lightreader.Parts.LightPic;
import com.qiao.androidlab.lightreader.Parts.MyAdapter;
import com.qiao.androidlab.lightreader.Parts.SerializableLightPic;
import com.qiao.androidlab.lightreader.R;
import com.qiao.androidlab.lightreader.db.DBCtrl;
import com.qiao.androidlab.lightreader.db.DBUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String KEY = "LIGHT_PIC";

    private Toolbar toolbar;
    private android.support.v7.widget.RecyclerView recyclerView;
    private android.support.design.widget.FloatingActionButton fab;
    private View reveal_view;
    private ProgressBar progressBar;    //圆形进度条
    private SwipeRefreshLayout swipeRefreshLayout;
    private ImageButton openButton;
    private ImageButton closeButton;
    private FrameLayout guillotineView;
    private TextView menuMain;
    private TextView menuPerson;
    private TextView menuFind;
    private TextView menuMap;

    private MenuSelect mMenuSelect;
    private List<LightPic> datas;
    private MyAdapter adapter;
    private Intent intent;
    private DBCtrl dbCtrl;
    private DBUtil dbUtil;
    private Handler handler;
    private GuillotineAnimtor mGuillotineAnimtor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle(null);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        setGuillotine();
        mMenuSelect = new MenuSelect(this, this, mGuillotineAnimtor);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        adapter = new MyAdapter(this, new ArrayList<LightPic>());
        recyclerView.setAdapter(adapter);
        checkCameraPremission();
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
    private void setGuillotine() {
        mGuillotineAnimtor = new GuillotineAnimtor.Builder()
                .setActionbar(toolbar)
                .setCloseButton(closeButton)
                .setOpenButton(openButton)
                .setGuillotineView(guillotineView)
                .build();
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
        dbUtil.closeDatabase(dbCtrl);   //关闭数据库
        adapter.setmDatas(datas);
    }

    private void initView() {
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        toolbar = (Toolbar) findViewById(R.id.main_toolbar);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        reveal_view = findViewById(R.id.reveal_view);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);
        openButton = (ImageButton) findViewById(R.id.hamburger);
        closeButton = (ImageButton) findViewById(R.id.guillotine_hamburger);
        guillotineView = (FrameLayout) findViewById(R.id.guillotine_view);
        menuMain = (TextView) findViewById(R.id.btn_menu_main);
        menuFind = (TextView) findViewById(R.id.btn_menu_find);
        menuPerson = (TextView) findViewById(R.id.btn_menu_person);
        menuMap = (TextView) findViewById(R.id.btn_menu_map);
    }

    private void initEvent() {
        /*错误：adapter是空的，原因是：*/
        adapter.setmOnItemClickListener(new MyAdapter.OnItemClickListener() {
            @Override
            public void OnItemClick(View view, int position) {
                /*Snackbar.make(recyclerView, "This is " + position, Snackbar.LENGTH_SHORT).setAction("OK", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                    }
                }).show();*/
                Intent intent = new Intent(MainActivity.this, CenterActivity.class);
                Bundle bundle = new Bundle();
//                Log.i("HELLO", adapter.getLightPic(position).getTitle());
                bundle.putSerializable(KEY, new SerializableLightPic(adapter.getLightPic(position)));
                intent.putExtras(bundle);
                MainActivity.this.startActivity(intent);
            }

            @Override
            public void OnItemLongClick(View view, int position) {
                dbCtrl = new DBCtrl(MainActivity.this);  //数据库已经创建
                dbUtil = new DBUtil();
                dbUtil.mOPenorCreateDatabase(dbCtrl);
                dbUtil.mDBDelete(position);
                dbUtil.closeDatabase(dbCtrl);
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
                    animShow.setDuration(250);
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

        menuFind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMenuSelect.handle(v.getId());
                setFabVisiable(false);
            }
        });
        menuMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMenuSelect.handle(v.getId());
                setFabVisiable(true);
            }
        });
        menuPerson.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMenuSelect.handle(v.getId());
                setFabVisiable(false);
            }
        });
        menuMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMenuSelect.handle(v.getId());
                setFabVisiable(false);
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

    /**
     * 检查相机权限
     */
    private void checkCameraPremission() {
        int checkCameraPermission = ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA);
        if (checkCameraPermission != PackageManager.PERMISSION_GRANTED) {    //没有权限
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.CAMERA}, 1);    //申请权限
        }
    }

    public void setFabVisiable(boolean isVisiable) {
        if (isVisiable) {
            fab.setVisibility(View.VISIBLE);
        } else {
            fab.setVisibility(View.INVISIBLE);
        }
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

