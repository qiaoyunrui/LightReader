package com.qiao.androidlab.lightreader.Activities;

/**
 * Created by Administrator on 2015/12/1.
 */

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.SurfaceView;
import android.view.View;
import android.widget.ImageView;

import com.qiao.androidlab.lightreader.R;

/**
 * 获取图片后展示函数的界面
 */
public class ShowActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private ImageView imageShow;
    private SurfaceView coordinateShow;
    private FloatingActionButton save;
    private String path;
    private Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_layout);
        initData();
        initView();
        initEvent();
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("解读");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        setShowImage();
    }

    private void initData() {

    }

    private void initView() {
        toolbar = (Toolbar) findViewById(R.id.show_toolbar);
        imageShow = (ImageView) findViewById(R.id.imageShow);
        coordinateShow = (SurfaceView) findViewById(R.id.coordinateShow);
        save = (FloatingActionButton) findViewById(R.id.save);
    }

    private void initEvent() {
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    /**
     * 设置获取到的图片
     */
    private void setShowImage() {
        if (true) {
            path = getIntent().getStringExtra("picPath");
            Log.i("HELLO", "path is " + path);
            bitmap = BitmapFactory.decodeFile(path);
            Matrix matrix = new Matrix();
            matrix.postRotate(90);
            bitmap = bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
            if (bitmap != null) {
                imageShow.setImageBitmap(bitmap);
            }

        } else {

        }

    }

    private boolean save() {
        return true;
    }


}
