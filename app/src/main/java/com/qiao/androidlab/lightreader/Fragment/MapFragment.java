package com.qiao.androidlab.lightreader.Fragment;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.LocationSource;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.model.BitmapDescriptorFactory;
import com.amap.api.maps2d.model.CameraPosition;
import com.amap.api.maps2d.model.CircleOptions;
import com.amap.api.maps2d.model.GroundOverlayOptions;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.MyLocationStyle;
import com.qiao.androidlab.lightreader.Parts.MapPoint;
import com.qiao.androidlab.lightreader.R;
import com.qiao.androidlab.lightreader.RequestUtil.HttpUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * MapFragment
 *
 * @author: 乔云瑞
 * @time: 2016/3/20 17:08
 * <p/>
 * 显示地图
 */
public class MapFragment extends BaseFragment {

    private static final String URL = "http://juhezi.applinzi.com/function/queryAll.php";
    private static final String TAG = "MapFragment";

    private MapView mMapView;
    private List<MapPoint> datas = new ArrayList<>();
    private AMap aMap;
    private JSONObject mJSONObject;
    private String result;
    private ProgressBar mProgressBar;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 0x131) {
                mProgressBar.setVisibility(View.INVISIBLE);
                aMap.moveCamera(CameraUpdateFactory.newLatLng(datas.get(0).getLatLng()));
                aMap.moveCamera(CameraUpdateFactory.zoomTo(17));
                aMap.invalidate();
                for (int i = 0; i < datas.size(); i++) {
                    draw(datas.get(i).getLatLng());
                }
            }
        }
    };

    private final static LatLng BEIJING = new LatLng(39.8965, 116.4074);

    @Override
    public String getTitle() {
        return "地图";
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_map, container, false);
        mProgressBar = (ProgressBar) rootView.findViewById(R.id.map_progressBar);
        mMapView = (MapView) rootView.findViewById(R.id.map_mapView);
        mMapView.onCreate(savedInstanceState);
        return rootView;
    }

    /**
     * 初始化AMap对象
     */
    private void init() {
        if (aMap == null) {
            aMap = mMapView.getMap();
            aMap.setMapType(AMap.MAP_TYPE_NORMAL);
            setUpMap();
        }
    }

    /**
     * 对地图添加onMapIsAbroadListener
     */
    private void setUpMap() {
        aMap.setOnCameraChangeListener(new AMap.OnCameraChangeListener() {
            @Override
            public void onCameraChange(CameraPosition cameraPosition) {
                //Log.i(TAG, cameraPosition.zoom + "");
                //draw((int) cameraPosition.zoom);
            }

            @Override
            public void onCameraChangeFinish(CameraPosition cameraPosition) {

            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        init();
        getData();

    }

    private void showToast(String message) {
        Toast.makeText(getActivity().getApplicationContext(), message, Toast.LENGTH_SHORT).show();
        //ToastUtil.showShortToast(getApplicationContext(), "当前地图中心位置是否在国外: "+cameraPosition.isAbroad);
    }

    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mMapView.onSaveInstanceState(outState);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }

    /**
     * 绘制原点
     */
    /*public void draw(int zoom) {
        *//*aMap.clear();
        CircleOptions circleOptions = new CircleOptions();
        circleOptions.fillColor(Color.BLUE);
        circleOptions.center(BEIJING);
        circleOptions.visible(true);
        circleOptions.radius(2000);
        circleOptions.strokeColor(Color.RED);
        aMap.addCircle(circleOptions);*//*
        aMap.clear();
        GroundOverlayOptions groundOverlayOptions = new GroundOverlayOptions();
        groundOverlayOptions.image(BitmapDescriptorFactory.fromResource(R.mipmap.ic_camera_alt_black_48dp));
        groundOverlayOptions.position(BEIJING, getSize(zoom));
        aMap.addGroundOverlay(groundOverlayOptions);
    }*/
    private void draw(LatLng latLng) {
        GroundOverlayOptions groundOverlayOptions = new GroundOverlayOptions();
        groundOverlayOptions.image(BitmapDescriptorFactory.fromResource(R.mipmap.ic_camera_alt_black_48dp));
        groundOverlayOptions.position(latLng, 100);
        aMap.addGroundOverlay(groundOverlayOptions);
    }


    /**
     * 移动到北京
     */
    public void moveToBeiJing(View v) {
        aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(BEIJING, 14));
        aMap.postInvalidate();// 刷新地图
    }

    private int getSize(int zoom) {
        int size = 30;
        if (zoom < 19 && zoom >= 18) {
            size = 100;
        } else if (zoom < 18 && zoom >= 17) {
            size = 300;
        } else if (zoom < 17 && zoom >= 16) {
            size = 600;
        } else if (zoom < 16 && zoom >= 15) {
            size = 1000;
        } else if (zoom < 15 && zoom >= 14) {
            size = 1500;
        } else if (zoom < 14 && zoom >= 13) {
            size = 2000;
        } else if (zoom < 13 && zoom >= 12) {
            size = 4000;
        } else if (zoom < 12) {
            size = 7000;
        }
        return size;
    }

    /**
     * 绘制所有点
     */
    private void getData() {
        mProgressBar.setVisibility(View.VISIBLE);
        new Thread() {
            @Override
            public void run() {
                result = HttpUtil.sendPostRequest(URL, "");
                //Log.i(TAG, result);
                try {
                    mJSONObject = new JSONObject(result);
                    if (mJSONObject.get("code").equals("200")) {
                        //解析数据
                        parseAll(mJSONObject, datas);
                    } else {
                        showToast("获取数据失败");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    showToast("未知错误");
                } finally {
                    mHandler.sendEmptyMessage(0x131);
                }
            }
        }.start();
    }

    /**
     * 解析所有数据
     *
     * @param jsonObject
     * @param datas
     */
    private void parseAll(JSONObject jsonObject, List<MapPoint> datas) {
        int i = 0;
        datas.clear();
        try {
            while (jsonObject.getJSONArray("data").get(i) != null) {
                datas.add(parseData(jsonObject, i));
//                Log.i(TAG, i + "XXXX");
                i++;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        //Log.i(TAG, datas.toString());
    }

    /**
     * 解析单条数据
     *
     * @param jsonObject
     * @param index
     * @return
     */
    private MapPoint parseData(JSONObject jsonObject, int index) {
        MapPoint mapPoint = new MapPoint();
        try {
            /*
            serializableLightPic.setId(Integer.parseInt(jsonObject.getJSONArray("data").getJSONObject(index).get("id").toString()));
            serializableLightPic.setTitle(jsonObject.getJSONArray("data").getJSONObject(index).get("title").toString());
            serializableLightPic.setTime(jsonObject.getJSONArray("data").getJSONObject(index).get("time").toString());
            serializableLightPic.setDetail(jsonObject.getJSONArray("data").getJSONObject(index).get("detail").toString());
            serializableLightPic.setPath(jsonObject.getJSONArray("data").getJSONObject(index).get("url").toString());
            serializableLightPic.setLon();
            serializableLightPic.setLat();
            serializableLightPic.setAuthor(jsonObject.getJSONArray("data").getJSONObject(index).get("(SELECT username FROM users WHERE users.uid = storage.uid)").toString());
            */
            mapPoint.setLatLng(
                    new LatLng(Double.parseDouble(jsonObject.getJSONArray("data").getJSONObject(index).get("lat").toString()),
                            Double.parseDouble(jsonObject.getJSONArray("data").getJSONObject(index).get("lon").toString())));
            mapPoint.setState(MapPoint.STATE_NORMAL);
        } catch (JSONException e) {
            e.printStackTrace();
            showToast("解析数据出现错误");
        }
        return mapPoint;
    }
}
