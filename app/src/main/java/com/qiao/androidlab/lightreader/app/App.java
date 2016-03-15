package com.qiao.androidlab.lightreader.app;

import android.app.Application;
import android.graphics.Typeface;

/**
 * App
 *
 * @author: 乔云瑞
 * @time: 2016/3/15 11:08
 */
public class App extends Application {
    private static final String MOSK_FONT_PATH = "fonts/Mosk Light 300.ttf";
    public static Typeface mosk;

    @Override
    public void onCreate() {
        super.onCreate();
        initTypeface();
    }

    private void initTypeface() {
        mosk = Typeface.createFromAsset(getAssets(), MOSK_FONT_PATH);
    }
}
