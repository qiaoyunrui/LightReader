package com.qiao.androidlab.lightreader.Activities;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatImageButton;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.qiao.androidlab.lightreader.CameraUtil.CameraUtil;
import com.qiao.androidlab.lightreader.Parts.DrawSurfaceView;
import com.qiao.androidlab.lightreader.R;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by Administrator on 2015/11/30.
 */
public class CameraActivity extends AppCompatActivity {

    WindowManager windowManager;
    Display display;
    DisplayMetrics metrics;
    int screenHeight;
    int screenWidth;
    GestureDetector detector;
    private Toolbar toolbar;
    private RelativeLayout root;
    private SurfaceView surfaceView;
    private DrawSurfaceView drawSurfaceView;
    private AppCompatImageButton capture;
    private Intent intent;
    private CameraUtil cameraUtil;
    private Intent settingIntent;
    private SharedPreferences sharedPreferences;

    private Boolean light_source;   //光源 true false
    private int images_per_trial;   //拍照数量 1 2 3
    private int area_read = 1;   //识别区域 1 2 3
    private int exposure_time;   //曝光时间 1 2 3

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.camera_layout);
        setDic();
        initData();
        initView();
        initEvent();
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("相机");
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        drawSurfaceView.setScreenHeight(screenHeight);
        drawSurfaceView.setScreenWidth(screenWidth);
        cameraUtil = new CameraUtil(this, screenHeight, screenWidth, surfaceView, intent, this);
        detector = new GestureDetector(this, new GestureDetector.OnGestureListener() {
            @Override
            public boolean onDown(MotionEvent e) {
                drawSurfaceView.DrawCircle(e.getX(), e.getY());
                cameraUtil.focues(e.getX(), e.getY());
                return false;
            }

            @Override
            public void onShowPress(MotionEvent e) {
            }

            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                return false;
            }

            @Override
            public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
                return false;
            }

            @Override
            public void onLongPress(MotionEvent e) {


            }

            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                return false;
            }
        });
        UpdateDrawSurface();
        UpdateCameraUtil();
    }

    //将Activity上的触摸操作交给GestureDetector操作
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return detector.onTouchEvent(event);
    }

    @Override
    protected void onResume() {
        super.onResume();
        UpdateDrawSurface();
        UpdateCameraUtil();
        if (cameraUtil.isCameraNull()) {
            cameraUtil.initCamera();
            if (!cameraUtil.isSurfaceHolderNull()) {
                cameraUtil.setStartPreView();
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        cameraUtil.releseCamera();
    }

    private void initData() {
        windowManager = getWindowManager();
        display = windowManager.getDefaultDisplay();
        metrics = new DisplayMetrics();
        display.getMetrics(metrics);
        screenHeight = metrics.heightPixels;
        screenWidth = metrics.widthPixels;
//        Log.i("HELLO", screenWidth + "   --   " + screenHeight);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        intent = new Intent(this, ShowActivity.class);
        settingIntent = new Intent(this, SettingActivity.class);
    }


    private void initView() {
        toolbar = (Toolbar) findViewById(R.id.camera_toolbar);
        root = (RelativeLayout) findViewById(R.id.camera_root);
        surfaceView = (SurfaceView) findViewById(R.id.cameraSurface);
        drawSurfaceView = (DrawSurfaceView) findViewById(R.id.drawSurfaceView);
        capture = (AppCompatImageButton) findViewById(R.id.capture);
    }

    private void initEvent() {
        capture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cameraUtil.capture();
            }
        });
    }

    private void setDic() {
        File dic = new File(Environment.getExternalStorageDirectory().getPath() + "/光之解读者");
        if (!dic.exists() || !dic.isDirectory()) {
            dic.mkdir();
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_camera, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_setting) {
            startActivity(settingIntent);
            return true;
        }
        if (id == android.R.id.home) {
            onBackPressed();
            Log.i("HELLO", "BACK");
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * 更新DrawSurface的数据
     */
    private void UpdateDrawSurface() {
        area_read = Integer.parseInt(sharedPreferences.getString("area_read", DrawSurfaceView.LINE_AREA + ""));
        drawSurfaceView.setArea(area_read);
    }

    /**
     * 更新Camera工具类
     */
    private void UpdateCameraUtil() {
        light_source = sharedPreferences.getBoolean("light_source", CameraUtil.SUN_LIGHT);
        images_per_trial = Integer.parseInt(sharedPreferences.getString("images_per_trial", CameraUtil.ONE_IMAGE + ""));
        exposure_time = Integer.parseInt(sharedPreferences.getString("exposure_time", CameraUtil.SHORT_TIME_EXPOSURE + ""));
        cameraUtil.setLight_source(light_source);
        cameraUtil.setImages_per_trial(images_per_trial);
        cameraUtil.setExposure_time(exposure_time);
    }



}
