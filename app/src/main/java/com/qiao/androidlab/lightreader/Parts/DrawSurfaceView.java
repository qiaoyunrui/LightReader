package com.qiao.androidlab.lightreader.Parts;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.qiao.androidlab.lightreader.R;

/**
 * Created by Administrator on 2015/12/3.
 */
public class DrawSurfaceView extends SurfaceView implements SurfaceHolder.Callback {

    protected SurfaceHolder surfaceHolder;
    private int screenWidth = 1080;
    private int screenHeight = 1920;
    private int area_read;

    public DrawSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.DrawSurfaceView, 0, 0);
        try {
            area_read = a.getInt(R.styleable.DrawSurfaceView_area_read, 1);
            screenWidth = a.getInt(R.styleable.DrawSurfaceView_screen_width, 1080);
            screenHeight = a.getInt(R.styleable.DrawSurfaceView_screen_height, 1920);
        } finally {
            a.recycle();
        }
        surfaceHolder = getHolder();
        surfaceHolder.addCallback(this);
        surfaceHolder.setFormat(PixelFormat.TRANSLUCENT);
        setZOrderOnTop(true);
    }

    public void setArea(int area_read) {
        this.area_read = area_read;
    }

    public int getArea_read() {
        return area_read;
    }

    public void setScreenWidth(int screenWidth) {
        this.screenWidth = screenWidth;
    }

    public void setScreenHeight(int screenHeight) {
        this.screenHeight = screenHeight;
    }

    public int getScreenWidth() {
        return screenWidth;
    }

    public int getScreenHeight() {
        return screenHeight;
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        Paint paint = new Paint();
        paint.setStrokeWidth(5.0f);
        paint.setAlpha(100);
        Canvas canvas = holder.lockCanvas();
        switch (area_read) {
            case 1:
                canvas.drawLine(screenWidth / 2, 0, screenWidth / 2, screenHeight, paint);
                break;
            case 2:
                break;
            case 3:
                break;
            default:
                Log.i("ERROR", "识别区域错误");
        }
        holder.unlockCanvasAndPost(canvas);
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }


}
