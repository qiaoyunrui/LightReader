package com.qiao.androidlab.lightreader.Fragment;

/**
 * CoordinateFragments
 *
 * @author: 乔云瑞
 * @time: 2016/1/30 18:47
 */

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.qiao.androidlab.lightreader.R;
import com.qiao.androidlab.lightreader.SurfaceViews.CoordinateSurfaceView;

import java.util.List;

/**
 * 用于绘制函数曲线的Fragment
 */
public class CoordinateFragments extends Fragment {

    private View rootView;
    private CoordinateSurfaceView mCoordinateSurfaceView;
    private List<Integer> pixelsList;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.coordinate_layout, container, false);
        mCoordinateSurfaceView = (CoordinateSurfaceView) rootView.findViewById(R.id.coordinateView);
        if (pixelsList != null) {
            mCoordinateSurfaceView.setPixelsList(pixelsList);
        }
        return rootView;
    }

    /**
     * 设置像素列
     */
    public void setPixelsList(List<Integer> pixelsList) {
        this.pixelsList = pixelsList;
    }

}
