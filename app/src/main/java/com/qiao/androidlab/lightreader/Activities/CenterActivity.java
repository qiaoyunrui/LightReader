package com.qiao.androidlab.lightreader.Activities;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.qiao.androidlab.lightreader.Parts.LightPic;
import com.qiao.androidlab.lightreader.Parts.SerializableLightPic;
import com.qiao.androidlab.lightreader.R;
import com.qiao.androidlab.lightreader.ReadUtil.BitmapManage;
import com.qiao.androidlab.lightreader.RequestUtil.HttpUtil;
import com.qiao.androidlab.lightreader.RequestUtil.UploadUtil;
import com.qiao.androidlab.lightreader.SurfaceViews.CoordinateSurfaceView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.logging.Handler;

/**
 * Created by Administrator on 2015/12/2.
 */
public class CenterActivity extends AppCompatActivity {

    private static final String URL = "http://juhezi.applinzi.com/function/uploadTest.php";
    private static final String URLX = "http://juhezi.applinzi.com/function/upload.php";
    private static final String KEY = "LIGHT_PIC";
    private static final String TAG = "CenterActivity";
    private static final String SHARED_PREFERENCE_SIGN = "com.juhezi.com";
    private static final String USER_ID = "userid";

    Bitmap bitmap;
    private Toolbar toolbar;
    private TextView title;
    private TextView author;
    private TextView time;
    private TextView detail;
    private FloatingActionButton fab;
    private CoordinateSurfaceView coordinateView;
    private ImageView image;
    private ProgressBar mProgressBar;
    private SerializableLightPic lightPic;
    private BitmapManage bitmapManage;

    private String result;
    private String resultX;
    private double lon = 0;
    private double lat = 0;
    private android.os.Handler mHandler = new android.os.Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 0x130) {
                mProgressBar.setVisibility(View.INVISIBLE);
                try {
                    JSONObject jsonObject = new JSONObject(resultX);
                    if (jsonObject.get("code").equals("200")) {
                        showToast("上传成功");
                        finish();
                    } else {
                        showToast("上传失败");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    showToast("上传失败");
                }
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.center_layout);
        initView();
        initActionbar();
        dealData();
        initEvent();
    }

    /**
     * 处理数据
     */
    private void dealData() {
        Intent intent = getIntent();
        lightPic = (SerializableLightPic) intent.getSerializableExtra(KEY);
        boolean isURL = intent.getBooleanExtra("URL", false);
        if (lightPic != null) {
            title.setText(lightPic.getTitle());
            time.setText(lightPic.getTime());
            author.setText(lightPic.getAuthor());
            detail.setText(lightPic.getDetail());
            if (isURL) {
                //从网络上加载图片
                fab.setVisibility(View.INVISIBLE);  //设置上传按钮不可见
                Glide.with(this).load(lightPic.getPath()).asBitmap().into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                        bitmap = resource;
                        image.setImageBitmap(bitmap);
                        bitmapManage = new BitmapManage(new Bitmap[]{null, bitmap});
                        List<Integer> pixelsList = bitmapManage.manage();
                        coordinateView.setPixelsList(pixelsList);
                    }
                });
            } else {
                image.setImageBitmap(BitmapFactory.decodeFile(lightPic.getPath()));
                bitmapManage = new BitmapManage(new Bitmap[]{null, BitmapFactory.decodeFile(lightPic.getPath())});
                List<Integer> pixelsList = bitmapManage.manage();
                coordinateView.setPixelsList(pixelsList);
            }

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
        mProgressBar = (ProgressBar) findViewById(R.id.center_progressBar);
    }

    private void initEvent() {
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "click");
                upload(lightPic.getPath(), lightPic.getTitle(), lightPic.getTime(), lightPic.getDetail());
            }
        });
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

    /**
     * 上传图片
     */
    private void upload(final String path, final String title, final String time, final String detail) {
        mProgressBar.setVisibility(View.VISIBLE);
        new Thread() {
            @TargetApi(Build.VERSION_CODES.M)
            @Override
            public void run() {
                try {
                    result = UploadUtil.upload(new URL(URL), new File(path));
                    result = HttpUtil.getJsonString(result);
                    Log.i(TAG, result);
                    LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                    if (ActivityCompat.checkSelfPermission(CenterActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                            ActivityCompat.checkSelfPermission(CenterActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                                        Manifest.permission.ACCESS_COARSE_LOCATION},
                                1);
                        Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                        lon = location.getLongitude();
                        lat = location.getLatitude();
                    }
                    SharedPreferences sharedPreferences =
                            CenterActivity.this.getSharedPreferences(SHARED_PREFERENCE_SIGN, Context.MODE_PRIVATE);
                    int uid = sharedPreferences.getInt(USER_ID, 0);
                    String para = "title=" + title
                            + "&uid=" + uid
                            + "&time=" + time
                            + "&detail=" + detail
                            + "&url=" + result
                            + "&lon=" + lon
                            + "&lat=" + lat;
                    Log.i(TAG, para);
                    resultX = HttpUtil.sendPostRequest(URLX, para);
                    Log.i(TAG, resultX);
                    mHandler.sendEmptyMessage(0x130);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                    Log.i(TAG, "未知错误");
                }

            }
        }.start();
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

}
