package com.qiao.androidlab.lightreader.Fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.qiao.androidlab.lightreader.R;

/**
 * PersonFragment
 *
 * @author: 乔云瑞
 * @time: 2016/3/12 17:00
 */
public class PersonFragment extends BaseFragment {

    private View rootView;
    private TextView username;
    private Button logout;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_person, container, false);
        username = (TextView) rootView.findViewById(R.id.person_tv_username);
        logout = (Button) rootView.findViewById(R.id.person_btn_logout);
        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public String getTitle() {
        return "个人";
    }
}
