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

    public static final int STATE_NORMAL = -1;
    public static final int STATE_SLIGHT = 1;   //轻微
    public static final int STATE_MEDIUM = 2;   //中等
    public static final int STATE_SERIOUS = 3;  //严重

    public static final int STATE_POLLUTE = 0;  //污染


    private int state;   //状态
    private LatLng latLng;  //坐标点

    public LatLng getLatLng() {
        return latLng;
    }

    public void setLatLng(LatLng latLng) {
        this.latLng = latLng;
    }

    public MapPoint(LatLng latLng, int state) {
        this.latLng = latLng;

        this.state = state;
    }

    public MapPoint() {

    }


    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

}
