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
import java.security.Policy;

/**
 * Created by Administrator on 2015/12/3.
 */
public class CameraUtil implements SurfaceHolder.Callback {

    public static final Boolean SUN_LIGHT = false;
    public static final Boolean LED_LIGHT = true;

    public static final int ONE_IMAGE = 1;
    public static final int TWO_IMAGE = 2;
    public static final int THREE_IMAGE = 3;

    public static final int SHORT_TIME_EXPOSURE = 1;
    public static final int MEDIUM_TIME_EXPOSURE = 2;
    public static final int LONG_TIME_EXPOSURE = 3;

    private SurfaceView mSurfaceView;
    private SurfaceHolder surfaceHolder;
    private Boolean light_source;   //光源 true false
    private int images_per_trial;   //拍照数量 1 2 3
    private int exposure_time;   //曝光时间 1 2 3
    private File tempFile;

//    private int shortTime = 0;  //曝光时间
//    private int mediumTime = 2;
//    private int longTime = 5;

    SharedPreferences sharedPreferences;
    Camera mCamera;
    int screenWidth = 1080, screenHeight = 1920;
    Intent intent;
    Context context;
    Activity activity;

    private Camera.PictureCallback pictureCallback = new Camera.PictureCallback() {
        @Override
        public void onPictureTaken(byte[] data, Camera camera) {
            Log.i("HELLO", "onPictureTaken 回调执行成功");
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

    public void setLight_source(boolean light_source) {
        this.light_source = light_source;
    }

    public void setImages_per_trial(int images_per_trial) {
        this.images_per_trial = images_per_trial;
    }

    public void setExposure_time(int exposure_time) {
        this.exposure_time = exposure_time;
    }

    /**
     * 初始化相机
     */
    public void initCamera() {
        Log.i("HELLO", screenHeight + "--" + screenWidth);
        try {
            mCamera = Camera.open();
            Camera.Parameters parameters = mCamera.getParameters();
            parameters.setPictureSize(screenHeight, screenWidth);
            parameters.setPreviewSize(screenHeight, screenWidth);
            parameters.setFlashMode(Camera.Parameters.ANTIBANDING_AUTO);
            parameters.setPictureFormat(ImageFormat.JPEG);
            parameters.setPreviewFpsRange(4, 10);
            parameters.setJpegQuality(100);
            if (light_source) {
                Log.i("HELLO","闪光灯");
                parameters.setFlashMode(Camera.Parameters.FLASH_MODE_ON);
            } else {
                parameters.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
            }
//            Log.i("HELLO",parameters.getMaxExposureCompensation() + "");
//            Log.i("HELLO",parameters.getMinExposureCompensation() + "");
            switch (exposure_time) {
                case SHORT_TIME_EXPOSURE:
                    break;
                case MEDIUM_TIME_EXPOSURE:
                    break;
                case LONG_TIME_EXPOSURE:
                    break;
            }
            mCamera.setParameters(parameters);
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
                        Log.i("HELLO", "autoFocus 回调执行成功");
                        try {
                            camera.takePicture(null, null, pictureCallback);
                        } catch (Exception e) {
                            e.printStackTrace();
                            Log.i("HELLO", "错误");
                        }

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
     * 对焦
     *  @param pointHeight
     * @param pointWidth
     */
    public void focues(float pointHeight, float pointWidth) {

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
