package com.qiao.androidlab.lightreader.Activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatImageButton;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
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

    private Toolbar toolbar;
    private RelativeLayout root;
    private SurfaceView surfaceView;
    private DrawSurfaceView drawSurfaceView;
    private AppCompatImageButton capture;
    private Intent intent;
    private CameraUtil cameraUtil;
    WindowManager windowManager;
    Display display;
    DisplayMetrics metrics;
    int screenHeight;
    int screenWidth;
    private File tempFile;

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
    }

    @Override
    protected void onResume() {
        super.onResume();
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
        intent = new Intent(this, ShowActivity.class);
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
}
