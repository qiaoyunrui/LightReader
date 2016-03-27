package com.qiao.androidlab.lightreader.SurfaceViews;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.CornerPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathEffect;
import android.graphics.Point;
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

    private static final long STROKE_WIDTH = 5;

    private SurfaceHolder mHolder;
    private Canvas mCanvas;
    private int bitmapHeight;
    //画笔
    private Paint mPaint;

    private int margin = 40;

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
     * 设置像素列
     *
     * @param pixelsList
     */
    public void setPixelsList(List<Integer> pixelsList) {
        this.pixelsList = pixelsList;
        //invalidate();
    }

    /**
     * 绘制背景和坐标系以及曲线
     */
    private void draw() {
        try {
            mCanvas = mHolder.lockCanvas();
            if (mCanvas != null) {
                //draw somethging
                drawBackground(mCanvas);
                drawCoordinate(mCanvas);
                drawGraph(mCanvas);
            }
        } catch (Exception e) {
        } finally {
            if (mCanvas != null) {
                mHolder.unlockCanvasAndPost(mCanvas);
            }
        }
    }

    /**
     * 绘制背景
     */
    private void drawBackground(Canvas mCanvas) {
        mCanvas.drawColor(Color.WHITE);
    }

    /**
     * 绘制坐标系
     *
     * @param mCanvas
     */
    private void drawCoordinate(Canvas mCanvas) {
        int height = getHeight();
        int width = getWidth();
        mPaint.setColor(Color.BLACK);
        mPaint.setStrokeWidth(STROKE_WIDTH);
        //绘纵坐标
        mCanvas.drawLine(margin, margin, margin, height - margin, mPaint);
        //绘制横坐标
        mCanvas.drawLine(margin - STROKE_WIDTH / 2, height - margin, width - margin, height - margin, mPaint);
        //绘制原点
        mPaint.setTextSize(30);
        mCanvas.drawText("0", margin / 2, height - margin / 2, mPaint);
        mCanvas.drawText("灰度值", margin, 2 * margin / 3, mPaint);
        mCanvas.drawText("距离", width - 3 * margin / 2, height - margin / 2, mPaint);
        //绘制上箭头
        mCanvas.drawLine(margin / 2, margin * 2, margin, margin, mPaint);
        mCanvas.drawLine(3 * margin / 2, margin * 2, margin, margin, mPaint);
        //绘制右箭头
        mCanvas.drawLine(width - 2 * margin, height - 3 * margin / 2, width - margin, height - margin, mPaint);
        mCanvas.drawLine(width - 2 * margin, height - margin / 2, width - margin, height - margin, mPaint);
        //绘制刻度
        mPaint.setStrokeWidth(10);
        mPaint.setColor(Color.RED);
        mCanvas.drawPoint(margin, height - margin - (height - 2 * margin) / 3, mPaint);
        mCanvas.drawPoint(margin, height - margin - 2 * (height - 2 * margin) / 3, mPaint);
        mPaint.setColor(Color.BLACK);
        mCanvas.drawText("100", margin / 2, height - margin - (height - 2 * margin) / 3, mPaint);
        mCanvas.drawText("200", margin / 2, height - margin - 2 * (height - 2 * margin) / 3, mPaint);
        Log.i("HELLO", "drawBackgroundAndCoordinate");
    }

    /**
     * 绘制曲线
     *
     * @param mCanvas
     */
    private void drawGraph(Canvas mCanvas) {
        mPaint.setColor(Color.BLUE);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(2);
        Path path = new Path();
        PathEffect pathEffect = new CornerPathEffect(30);   //设置圆角·
        mPaint.setPathEffect(pathEffect);
        path.moveTo(getGraphWidth(0), getGraphHeight(getGrayscale(pixelsList.get(0))));
        for (int i = 1; i < pixelsList.size(); i++) {
            path.lineTo(getGraphWidth(i), getGraphHeight(getGrayscale(pixelsList.get(i))));
//            Log.i("HELLO", getGrayscale(pixelsList.get(i)) + "");
        }
        mCanvas.drawPath(path, mPaint);
    }

    /**
     * 获取高度
     *
     * @param x
     * @return
     */
    private int getGraphHeight(int x) {
        return getHeight() - margin - ((getHeight() - 2 * margin) * x / 300);
    }

    /**
     * 获取宽度
     *
     * @param x
     * @return
     */
    private int getGraphWidth(int x) {
        return (getWidth() - margin * 2) * x / pixelsList.size() + margin;
    }

    /**
     * 获取灰度值
     */
    private int getGrayscale(int x) {
        int red = (x & 0x00ff0000) >> 16;
        int green = (x & 0x0000ff00) >> 8;
        int blue = x & 0x000000ff;
        return (int) (0.3 * red + 0.59 * green + 0.11 * blue);
    }


}
