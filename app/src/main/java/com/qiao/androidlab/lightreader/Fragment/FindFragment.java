package com.qiao.androidlab.lightreader.Fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
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

import com.qiao.androidlab.lightreader.Activities.CenterActivity;
import com.qiao.androidlab.lightreader.Adapters.RecycleAdapterEx;
import com.qiao.androidlab.lightreader.Parts.MyAdapter;
import com.qiao.androidlab.lightreader.Parts.SerializableLightPic;
import com.qiao.androidlab.lightreader.R;

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

    private RecyclerView mRecyclerView;
    private List<SerializableLightPic> datas = new ArrayList<>();
    private RecycleAdapterEx adapter;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private ProgressBar mProgressBar;
    private SerializableLightPic test = new SerializableLightPic();
    private Intent mIntent;

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
        datas.add(test);
        datas.add(test);
        datas.add(test);
        datas.add(test);
        configRecycerView();
        initEvent();
//        Log.i(TAG, datas.toString());
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
    }

    @Override
    public String getTitle() {
        return "发现";
    }

    public void configRecycerView() {
        adapter = new RecycleAdapterEx(getActivity().getApplicationContext(), datas);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setAdapter(adapter);
    }
}
