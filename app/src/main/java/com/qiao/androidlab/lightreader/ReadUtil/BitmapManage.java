package com.qiao.androidlab.lightreader.ReadUtil;

/**
 * BitmapManage
 *
 * @author: 乔云瑞
 * @time: 2016/1/29 16:43
 */

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * 图片处理类
 */
public class BitmapManage {

    private final static int DEFAULT_DISTANCE = 0;  //默认处理宽度

    private Bitmap bitmaps[] = new Bitmap[]{null, null, null, null};  //要处理的图片
    private int bitmapHeight = 0;   //图片高度
    private int bitmapWidth = 0;    //图片宽度
    private int manageDistance = DEFAULT_DISTANCE; //处理宽度
    private List<Integer> pixelsList;   //像素数组


    public BitmapManage(Bitmap[] bitmaps) {
        this.bitmaps = bitmaps;
        getBitmapHeightAndWidth();
        pixelsList = new ArrayList<>();
    }

    /**
     * 获取图片的长度和宽度
     */
    private void getBitmapHeightAndWidth() {
        if (bitmaps[1] != null) {
            bitmapHeight = bitmaps[1].getHeight();
            bitmapWidth = bitmaps[1].getWidth();
            Log.i("HELLO", "bitmapHeight is " + bitmapHeight);
            Log.i("HELLO", "bitmapWidth is " + bitmapWidth);
        } else {
            Log.i("HELLO", "bitmap is null");
        }
    }

    /**
     * 设置图片处理宽度
     *
     * @param distance
     */
    public void setManageDistance(int distance) {
        manageDistance = Math.min(distance, bitmapWidth);   //获取最小值
    }

    /**
     * 处理图片
     */
    public List<Integer> manage() {
        for (int j = 0; j < bitmapHeight; j++) {   //高度循环
            int sumX = 0;
            int numOfBitmap = 0;
            for (int i = 1; i < bitmaps.length; i++) { //依次循环每张图片
                if (bitmaps[i] != null) {
                    numOfBitmap++;
                    int sum = 0;
                    for (int z = -manageDistance / 2; z <= manageDistance / 2; z++) {     //处理距离内循环
                        sum += bitmaps[i].getPixel(bitmapWidth / 2 + z, j);
                    }
                    sumX += sum / (manageDistance == 0 ? 1 : manageDistance);
                }
            }
            pixelsList.add(sumX / numOfBitmap);
        }
        return pixelsList;
    }
}
