package com.qiao.androidlab.lightreader.Activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.qiao.androidlab.lightreader.Parts.LightPic;
import com.qiao.androidlab.lightreader.Parts.SerializableLightPic;
import com.qiao.androidlab.lightreader.R;
import com.qiao.androidlab.lightreader.ReadUtil.BitmapManage;
import com.qiao.androidlab.lightreader.SurfaceViews.CoordinateSurfaceView;

import java.util.List;

/**
 * Created by Administrator on 2015/12/2.
 */
public class CenterActivity extends AppCompatActivity {

    private static final String KEY = "LIGHT_PIC";

    private Toolbar toolbar;
    private TextView title;
    private TextView author;
    private TextView time;
    private TextView detail;
    private FloatingActionButton fab;
    private CoordinateSurfaceView coordinateView;
    private ImageView image;
    private SerializableLightPic lightPic;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.center_layout);
        initView();
        initActionbar();
        dealData();
    }

    /**
     * 处理数据
     */
    private void dealData() {
        Intent intent = getIntent();
        lightPic = (SerializableLightPic) intent.getSerializableExtra(KEY);
        if (lightPic != null) {
            title.setText(lightPic.getTitle());
            time.setText(lightPic.getTime());
            author.setText(lightPic.getAuthor());
            detail.setText(lightPic.getDetail());
            image.setImageBitmap(BitmapFactory.decodeFile(lightPic.getPath()));
            BitmapManage bitmapManage = new BitmapManage(new Bitmap[]{null, BitmapFactory.decodeFile(lightPic.getPath())});
            List<Integer> pixelsList = bitmapManage.manage();
            coordinateView.setPixelsList(pixelsList);
        }

    }

    private void initView() {
        toolbar = (Toolbar) findViewById(R.id.centerToolbar);
        title = (TextView) findViewById(R.id.centerTitle);
        time = (TextView) findViewById(R.id.centerTime);
        author = (TextView) findViewById(R.id.centerAuthor);
        detail = (TextView) findViewById(R.id.centerDetail);
        fab = (FloatingActionButton) findViewById(R.id.centerFabShare);
        coordinateView = (CoordinateSurfaceView) findViewById(R.id.centerSurface);
        image = (ImageView) findViewById(R.id.image);
    }

    private void initActionbar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("光谱");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == android.R.id.home) {
            onBackPressed();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
