package com.qiao.androidlab.lightreader.CameraUtil;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.ImageFormat;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

import com.qiao.androidlab.lightreader.Activities.CameraActivity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by Administrator on 2015/12/3.
 */
public class CameraUtil implements SurfaceHolder.Callback {

    private static final Boolean SUN_LIGHT = false;
    private static final Boolean LED_LIGHT = true;

    private static final int ONE_IMAGE = 1;
    private static final int TWO_IMAGE = 2;
    private static final int THREE_IMAGE = 3;

    private static final int LINE_AREA = 1;
    private static final int THIN_SQUARE_AREA = 2;
    private static final int THICK_SQUARE_AREA = 3;

    private static final int SHORT_TIME_EXPOSURE = 1;
    private static final int MEDIUM_TIME_EXPOSURE = 2;
    private static final int LONG_TIME_EXPOSURE = 3;

    private SurfaceView mSurfaceView;
    private SurfaceHolder surfaceHolder;
    private Boolean light_source;   //光源 true false
    private String images_per_trial;   //拍照数量 1 2 3
    private String area_read;   //识别区域 1 2 3
    private String exposure_time;   //曝光时间 1 2 3
    private File tempFile;

    SharedPreferences sharedPreferences;
    Camera mCamera;
    boolean isPreview = false;
    int screenWidth = 1080, screenHeight = 1920;
    byte[] mData;
    Intent intent;
    Context context;
    Activity activity;

    private Camera.PictureCallback pictureCallback = new Camera.PictureCallback() {
        @Override
        public void onPictureTaken(byte[] data, Camera camera) {
            if (data != null) {
                Log.i("HELLO", data.toString());
                tempFile = new File(Environment.getExternalStorageDirectory().getPath() + "/光之解读者", "temp.jpg");
                try {
                    FileOutputStream fos = new FileOutputStream(tempFile);
                    fos.write(data);
                    fos.close();
                } catch (FileNotFoundException e) {
                    Log.i("HELLO", "FileNotFoundException");
                    e.printStackTrace();
                } catch (IOException e) {
                    Log.i("HELLO", "IOException");
                    e.printStackTrace();
                }

            }

            if (tempFile != null) {
                Log.i("HELLO", "tempFile is " + tempFile.toString());
                intent.putExtra("picPath", tempFile.getAbsoluteFile().toString());
                context.startActivity(intent);
                activity.finish();
            }
            camera.stopPreview();
        }
    };

    /**
     * 首先要获取设置界面的属性
     *
     * @param context
     */
    public CameraUtil(Context context) {
        this.context = context;
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        light_source = sharedPreferences.getBoolean("light_source", SUN_LIGHT);
        images_per_trial = sharedPreferences.getString("images_per_trial", ONE_IMAGE + "");
        area_read = sharedPreferences.getString("area_read", LINE_AREA + "");
        exposure_time = sharedPreferences.getString("exposure_time", SHORT_TIME_EXPOSURE + "");
    }

    public CameraUtil(Context context, int screenHeight, int screenWidth, SurfaceView surfaceView, Intent intent, Activity activity) {
        this(context);
        this.mSurfaceView = surfaceView;
        this.screenHeight = screenHeight;
        this.screenWidth = screenWidth;
        this.intent = intent;
        this.activity = activity;
        surfaceHolder = mSurfaceView.getHolder();
        surfaceHolder.addCallback(this);
    }

    public void setmSurfaceView(SurfaceView surfaceView) {
        this.mSurfaceView = surfaceView;
    }

    public SurfaceView getSurfaceView() {
        return mSurfaceView;
    }

    public void setScreenHeight(int screenHeight) {
        this.screenHeight = screenHeight;
    }

    public void setScreenWidth(int screenWidth) {
        this.screenWidth = screenWidth;
    }

    public int getScreenWidth() {
        return screenWidth;
    }

    public int getScreenHeight() {
        return screenHeight;
    }

    public Intent getInent() {
        return intent;
    }

    public void setIntent(Intent intent) {
        this.intent = intent;
    }


    /**
     * 初始化相机
     */
    public void initCamera() {
        try {
            mCamera = Camera.open();
            Camera.Parameters parameters = mCamera.getParameters();
            parameters.setPictureSize(screenWidth, screenHeight);
            parameters.setPreviewSize(screenWidth, screenHeight);
            parameters.setPictureFormat(ImageFormat.JPEG);
            parameters.setPreviewFpsRange(4, 10);
        } catch (Exception e) {
            Log.i("ERROR", "CAMERA ERROR");
            e.printStackTrace();
        }
    }

    /**
     * 开始预览
     */
    public void setStartPreView() {
        try {
            mCamera.setPreviewDisplay(surfaceHolder);
            mCamera.setDisplayOrientation(90);
            mCamera.startPreview();
        } catch (IOException e) {
            e.printStackTrace();
            Log.i("ERROR", "setPreview ERROR！");
        }
    }

    /**
     * 拍照
     *
     * @return
     */
    public void capture() {
        if (mCamera != null) {
            mCamera.autoFocus(new Camera.AutoFocusCallback() {
                @Override
                public void onAutoFocus(boolean success, Camera camera) {
                    if (success) {
                        camera.takePicture(null, null, pictureCallback);
                    }
                }
            });
        }

    }

    /**
     * 释放相机
     */
    public void releseCamera() {
        if (mCamera != null) {
            mCamera.setPreviewCallback(null);
            mCamera.stopPreview();
            mCamera.release();
            mCamera = null;
        }
    }

    /**
     * 判断相机是否为空
     */
    public boolean isCameraNull() {
        return mCamera == null ? true : false;
    }

    public boolean isSurfaceHolderNull() {
        return surfaceHolder == null ? true : false;
    }


    /**
     * 自动对焦
     *
     * @param pointHeight
     * @param pointWidth
     */
    private void focues(int pointHeight, int pointWidth) {

    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        initCamera();
        setStartPreView();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        mCamera.stopPreview();
        setStartPreView();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        releseCamera();
    }


}
