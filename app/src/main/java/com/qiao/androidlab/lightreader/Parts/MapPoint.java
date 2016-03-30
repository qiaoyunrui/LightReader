package com.qiao.androidlab.lightreader.Parts;

import com.amap.api.maps2d.model.LatLng;

/**
 * MapPoint
 *
 * @author: 乔云瑞
 * @time: 2016/3/20 19:27
 * <p/>
 * 表示地图上的一个点
 */
public class MapPoint {

    public static final int STATE_NORMAL = 0;   //轻微
    public static final int STATE_MEDIUM = 1;   //中等
    public static final int STATE_SERIOUS = 2;  //严重

    private int state = STATE_MEDIUM;   //状态
    private LatLng latLng;  //坐标点
    private double PH = 0;

    public MapPoint(LatLng latLng, int state) {
        this.latLng = latLng;

        this.state = state;
    }

    public MapPoint() {

    }

    public LatLng getLatLng() {
        return latLng;
    }

    public void setLatLng(LatLng latLng) {
        this.latLng = latLng;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public double getPH() {
        return PH;
    }

    public void setPH(double PH) {
        this.PH = PH;
    }
}
