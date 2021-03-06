package com.qiao.androidlab.lightreader.Parts;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.qiao.androidlab.lightreader.R;

/**
 * Created by Administrator on 2015/12/3.
 */
public class DrawSurfaceView extends SurfaceView implements SurfaceHolder.Callback {

    public static final int LINE_AREA = 1;
    public static final int THIN_SQUARE_AREA = 2;
    public static final int THICK_SQUARE_AREA = 3;
    protected SurfaceHolder surfaceHolder;
    private int screenWidth = 1080;
    private int screenHeight = 1920;
    private int area_read;
    private int smallOffset = 80;
    private int bigOffset = 150;
    private int radius = 100;

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

    public int getScreenWidth() {
        return screenWidth;
    }

    public void setScreenWidth(int screenWidth) {
        this.screenWidth = screenWidth;
    }

    public int getScreenHeight() {
        return screenHeight;
    }

    public void setScreenHeight(int screenHeight) {
        this.screenHeight = screenHeight;
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        Paint paint = new Paint();
        paint.setStrokeWidth(5.0f);
        paint.setAntiAlias(true);
        Canvas canvas = holder.lockCanvas();
        switch (area_read) {
            case LINE_AREA:
                canvas.drawLine(screenWidth / 2, 0, screenWidth / 2, screenHeight, paint);
                break;
            case THIN_SQUARE_AREA:
                canvas.drawLine(screenWidth / 2 - smallOffset, 0, screenWidth / 2 - smallOffset, screenHeight, paint);
                canvas.drawLine(screenWidth / 2 + smallOffset, 0, screenWidth / 2 + smallOffset, screenHeight, paint);
                break;
            case THICK_SQUARE_AREA:
                canvas.drawLine(screenWidth / 2 - bigOffset, 0, screenWidth / 2 - bigOffset, screenHeight, paint);
                canvas.drawLine(screenWidth / 2 + bigOffset, 0, screenWidth / 2 + bigOffset, screenHeight, paint);
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

    public void DrawCircle(float pointX, float pointY) {
        Paint paintBlue = new Paint();
        Paint paintBlack = new Paint();
        paintBlue.setStrokeWidth(5.0f);
        paintBlue.setColor(Color.BLUE);
        paintBlue.setStyle(Paint.Style.STROKE);
        paintBlack.setStrokeWidth(5.0f);
        paintBlack.setColor(Color.BLACK);
        paintBlack.setAntiAlias(true);
        paintBlue.setAntiAlias(true);
        Canvas canvas = surfaceHolder.lockCanvas();
        canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR); //清除画布内容
        canvas.drawCircle(pointX, pointY, radius, paintBlue);
        switch (area_read) {
            case LINE_AREA:
                canvas.drawLine(screenWidth / 2, 0, screenWidth / 2, screenHeight, paintBlack);
                break;
            case THIN_SQUARE_AREA:
                canvas.drawLine(screenWidth / 2 - smallOffset, 0, screenWidth / 2 - smallOffset, screenHeight, paintBlack);
                canvas.drawLine(screenWidth / 2 + smallOffset, 0, screenWidth / 2 + smallOffset, screenHeight, paintBlack);
                break;
            case THICK_SQUARE_AREA:
                canvas.drawLine(screenWidth / 2 - bigOffset, 0, screenWidth / 2 - bigOffset, screenHeight, paintBlack);
                canvas.drawLine(screenWidth / 2 + bigOffset, 0, screenWidth / 2 + bigOffset, screenHeight, paintBlack);
                break;
            default:
                Log.i("ERROR", "识别区域错误");
        }
        surfaceHolder.unlockCanvasAndPost(canvas);

    }


}
