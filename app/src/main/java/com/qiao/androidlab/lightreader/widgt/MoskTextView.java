package com.qiao.androidlab.lightreader.widgt;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

import com.qiao.androidlab.lightreader.app.App;

/**
 * MoskTextView
 *
 * @author: 乔云瑞
 * @time: 2016/3/15 11:05
 */
public class MoskTextView extends TextView {


    public MoskTextView(Context context) {
        this(context, null);
    }

    public MoskTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MoskTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setTypeface(App.mosk);
    }

}
