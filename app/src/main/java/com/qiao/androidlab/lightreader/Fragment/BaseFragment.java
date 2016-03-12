package com.qiao.androidlab.lightreader.Fragment;

import android.app.Fragment;

/**
 * BaseFragment
 *
 * @author: 乔云瑞
 * @time: 2016/3/12 11:33
 */
public class BaseFragment extends Fragment {

    private String title = "title";

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
