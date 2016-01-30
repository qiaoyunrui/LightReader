package com.qiao.androidlab.lightreader.Fragment;

/**
 * ImageFragment
 *
 * @author: 乔云瑞
 * @time: 2016/1/30 18:43
 */

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.qiao.androidlab.lightreader.R;

/**
 * 用于展示图片的Fragment
 */
public class ImageFragment extends Fragment {

    private View rootView;
    private ImageView imageView;
    private Bitmap bitmap;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.image_layout, container, false);
        if (bitmap != null) {
            ImageView imageView = (ImageView) rootView.findViewById(R.id.showImageView);
            imageView.setImageBitmap(bitmap);
        }
        return rootView;
    }

    public void setImage(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

}
