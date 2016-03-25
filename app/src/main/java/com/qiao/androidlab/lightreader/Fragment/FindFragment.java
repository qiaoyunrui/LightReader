package com.qiao.androidlab.lightreader.Fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.qiao.androidlab.lightreader.Activities.CenterActivity;
import com.qiao.androidlab.lightreader.Adapters.RecycleAdapterEx;
import com.qiao.androidlab.lightreader.Parts.MyAdapter;
import com.qiao.androidlab.lightreader.Parts.SerializableLightPic;
import com.qiao.androidlab.lightreader.R;
import com.qiao.androidlab.lightreader.RequestUtil.HttpUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * FindFragment
 *
 * @author: 乔云瑞
 * @time: 2016/3/12 19:33
 */
public class FindFragment extends BaseFragment {

    private static final String TAG = "FindFragment";
    private static final String KEY = "LIGHT_PIC";
    private static final String URL = "http://juhezi.applinzi.com/function/queryAll.php";

    private RecyclerView mRecyclerView;
    private List<SerializableLightPic> datas = new ArrayList<>();
    private RecycleAdapterEx adapter;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private ProgressBar mProgressBar;
    private SerializableLightPic test = new SerializableLightPic();
    private Intent mIntent;


    private String result;
    private JSONObject mJSONObject;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 0x127) {
//                mSwipeRefreshLayout.setRefreshing(false);    //设置进度条旋转
                adapter.notifyDataSetChanged();
                mProgressBar.setVisibility(View.INVISIBLE);
            }
            if (msg.what == 0x000) {
                showToast("获取数据失败");
                mProgressBar.setVisibility(View.INVISIBLE);
            }
        }
    };


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_find, container, false);
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.find_recycler);
        mProgressBar = (ProgressBar) rootView.findViewById(R.id.find_progressBar);
        mSwipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.find_swipe);
        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        configRecycerView();
        initEvent();
        getData();
    }

    private void initEvent() {
        adapter.setOnItemClickListener(new RecycleAdapterEx.OnItemClickListener() {
            @Override
            public void OnItemClick(View view, int position) {
                mIntent = new Intent(getActivity().getApplicationContext(), CenterActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable(KEY, adapter.getSerializableLightPic(position));
                mIntent.putExtras(bundle);
                mIntent.putExtra("URL", true);
                getActivity().startActivity(mIntent);
            }

            @Override
            public void OnItemLongClick(View view, int position) {

            }
        });

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                adapter.notifyDataSetChanged();
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    @Override
    public String getTitle() {
        return "发现";
    }

    /**
     * 配置RecyclerView
     */
    public void configRecycerView() {
        adapter = new RecycleAdapterEx(getActivity().getApplicationContext(), datas);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setAdapter(adapter);
    }

    /**
     * 从服务器上获取数据
     */
    public void getData() {
//        mSwipeRefreshLayout.setRefreshing(true);    //设置进度条旋转
        mProgressBar.setVisibility(View.VISIBLE);
        new Thread() {
            @Override
            public void run() {
                result = HttpUtil.sendPostRequest(URL, "");
                Log.i(TAG, result);
                try {
                    mJSONObject = new JSONObject(result);
                    if (mJSONObject.get("code").equals("200")) {
                        //解析数据
                        parseAll(mJSONObject, datas);
                        handler.sendEmptyMessage(0x127);
                    } else {
                        handler.sendEmptyMessage(0x000);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    handler.sendEmptyMessage(0x000);
                }
            }
        }.start();

    }

    private void showToast(String message) {
        Toast.makeText(getActivity().getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

    /**
     * 解析单条数据
     *
     * @param jsonObject
     * @param index
     * @return
     */
    private SerializableLightPic parseData(JSONObject jsonObject, int index) {
        SerializableLightPic serializableLightPic = new SerializableLightPic();
        try {
            serializableLightPic.setId(Integer.parseInt(jsonObject.getJSONArray("data").getJSONObject(index).get("id").toString()));
            serializableLightPic.setTitle(jsonObject.getJSONArray("data").getJSONObject(index).get("title").toString());
            serializableLightPic.setTime(jsonObject.getJSONArray("data").getJSONObject(index).get("time").toString());
            serializableLightPic.setDetail(jsonObject.getJSONArray("data").getJSONObject(index).get("detail").toString());
            serializableLightPic.setPath(jsonObject.getJSONArray("data").getJSONObject(index).get("url").toString());
            serializableLightPic.setLon(Double.parseDouble(jsonObject.getJSONArray("data").getJSONObject(index).get("lon").toString()));
            serializableLightPic.setLat(Double.parseDouble(jsonObject.getJSONArray("data").getJSONObject(index).get("lat").toString()));
            serializableLightPic.setAuthor(jsonObject.getJSONArray("data").getJSONObject(index).get("(SELECT username FROM users WHERE users.uid = storage.uid)").toString());
        } catch (JSONException e) {
            e.printStackTrace();
            showToast("解析数据出现错误");
        }
        return serializableLightPic;
    }

    /**
     * 全部解析
     */
    private void parseAll(JSONObject jsonObject, List<SerializableLightPic> datas) {
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
    }

}
