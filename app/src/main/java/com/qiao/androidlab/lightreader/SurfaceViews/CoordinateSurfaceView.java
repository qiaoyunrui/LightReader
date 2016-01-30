package com.qiao.androidlab.lightreader.SurfaceViews;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.List;

/**
 * CoordinateSurfaceView
 *
 * @author: 乔云瑞
 * @time: 2016/1/29 21:19
 */
public class CoordinateSurfaceView extends SurfaceView implements SurfaceHolder.Callback, Runnable {

    private static final long FRAME_TIME = 100;
    private static final long STROKE_WIDTH = 10;

    private SurfaceHolder mHolder;
    private Canvas mCanvas;
    //画笔
    private Paint mPaint;

    /**
     * 色素列
     */
    private List<Integer> pixelsList;

    /**
     * 用于绘制的线程
     */
    private Thread mThread;

    /**
     * 线程的控制开关
     */
    private boolean isRunning;

    //色素列是否准备好了，默认为否
    private boolean isReally = false;

    public CoordinateSurfaceView(Context context) {
        this(context, null);
    }

    public CoordinateSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    /**
     * 初始化
     */
    private void init() {
        mHolder = getHolder();
        mHolder.addCallback(this);
        //可获得焦点
        setFocusable(true);
        setFocusableInTouchMode(true);
        //设置常量
        setKeepScreenOn(true);
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setColor(Color.BLACK);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        Log.i("HELLO", "surfaceCreated");
        isRunning = true;
        mThread = new Thread(this);
        mThread.start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        Log.i("HELLO", "surfaceChanged");
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        Log.i("HELLO", "surfaceDestroyed");
        isRunning = false;
    }

    @Override
    public void run() {
        draw();
    }

    /**
     * 绘制曲线
     */
    private void drawGraph() {
        try {
            mCanvas = mHolder.lockCanvas();
            if (mCanvas != null) {
                mPaint.setColor(Color.BLUE);
                mPaint.setStyle(Paint.Style.STROKE);
                mPaint.setStrokeWidth(2);
                Path path = new Path();
                path.moveTo(0, pixelsList.get(0));
                for (int i = 1; i < pixelsList.size(); i += 10) {
                    path.lineTo(i, getHeight() - ((pixelsList.get(i) & 0x000000ff) / 300f * getHeight()));
                }
                mCanvas.drawPath(path, mPaint);
            }
        } catch (Exception e) {
        } finally {
            if (mCanvas != null) {
                mHolder.unlockCanvasAndPost(mCanvas);
            }
        }
    }

    /**
     * 设置像素列
     *
     * @param pixelsList
     */
    public void setPixelsList(List<Integer> pixelsList) {
        this.pixelsList = pixelsList;
    }

    /**
     * 绘制背景和坐标系以及曲线
     */
    private void draw() {
        try {
            mCanvas = mHolder.lockCanvas();
            if (mCanvas != null) {
                //draw somethging
                mCanvas.drawColor(Color.WHITE);
                int height = getHeight();
                int width = getWidth();
                int margin = 20;
                mPaint.setColor(Color.BLACK);
                mPaint.setStrokeWidth(STROKE_WIDTH);
                //绘制横坐标
                mCanvas.drawLine(margin - STROKE_WIDTH / 2, height - margin, width - margin, height - margin, mPaint);
                //绘制纵坐标
                mCanvas.drawLine(margin, margin, margin, height - margin, mPaint);
                Log.i("HELLO", "drawBackgroundAndCoordinate");

                mPaint.setColor(Color.BLUE);
                mPaint.setStyle(Paint.Style.STROKE);
                mPaint.setStrokeWidth(2);
                Path path = new Path();
                path.moveTo(0, pixelsList.get(0));
                for (int i = 1; i < pixelsList.size(); i++) {
                    path.lineTo(i, getHeight() - ((pixelsList.get(i) & 0x000000ff) / 300f * getHeight()));
                }
                mCanvas.drawPath(path, mPaint);
            }
        } catch (Exception e) {
        } finally {
            if (mCanvas != null) {
                mHolder.unlockCanvasAndPost(mCanvas);
            }
        }
    }

}
