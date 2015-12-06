package com.qiao.androidlab.lightreader.Activities;

/**
 * Created by Administrator on 2015/12/1.
 */

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.Toolbar;
import android.text.format.Time;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.SurfaceView;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.qiao.androidlab.lightreader.Parts.LightPic;
import com.qiao.androidlab.lightreader.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.zip.Inflater;

/**
 * 获取图片后展示函数的界面
 */
public class ShowActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private ImageView imageShow;
    private SurfaceView coordinateShow;
    private FloatingActionButton save;
    private String path;
    private Bitmap[] bitmap = new Bitmap[]{null, null, null, null};
    int picNum = 1;

    private AppCompatEditText titleEdit;
    private AppCompatEditText authorEdit;
    private AppCompatEditText detailEdit;

    private TextInputLayout titleEditLayout;
    private TextInputLayout authorEditLayout;
    private TextInputLayout detailEditLayout;

    private LightPic lightPic;
    private String title;
    private String author;
    private String time;
    private String detail;
    private Bitmap bm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_layout);
        initData();
        initView();
        initEvent();
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("解读");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        setShowImage();
    }

    private void initData() {

    }

    private void initView() {
        toolbar = (Toolbar) findViewById(R.id.show_toolbar);
        imageShow = (ImageView) findViewById(R.id.imageShow);
        coordinateShow = (SurfaceView) findViewById(R.id.coordinateShow);
        save = (FloatingActionButton) findViewById(R.id.save);
    }

    private void initEvent() {
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowMessageDialog();
                createLightPic();
            }
        });
    }

    /**
     * 设置获取到的图片
     */
    private void setShowImage() {
        picNum = getIntent().getIntExtra("picNum", 1);
        Log.i("HELLO", "picNum is " + picNum);
        int sign;
        for (sign = 1; sign <= picNum; sign++) {
            path = getIntent().getStringExtra("picPath" + sign);
            Log.i("HELLO", path);
            bitmap[sign] = BitmapFactory.decodeFile(path);
            Log.i("HELLO", bitmap[sign].toString());
        }
        Matrix matrix = new Matrix();
        matrix.postRotate(90);
        bitmap[picNum] = bitmap[picNum].createBitmap(bitmap[picNum], 0, 0, bitmap[picNum].getWidth(), bitmap[picNum].getHeight(), matrix, true);
        if (bitmap[picNum] != null) {
            imageShow.setImageBitmap(bitmap[picNum]);     //显示最后一张图片
        }
    }

    //展示对话框
    private void ShowMessageDialog() {
        View view = loadView();
        initPartView(view);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(view)
                .setTitle("请输入信息")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        getMessage();

                    }
                })
                .setNegativeButton("取消", null);
        builder.create().show();
    }

    /**
     * 获取从对话框输入的信息
     */
    private void getMessage() {

    }

    //构建一个LightPic对象
    private void createLightPic() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        time = dateFormat.format(new java.util.Date());
        bm = bitmap[picNum];
        lightPic = new LightPic(title, author, detail, time, bm);
    }

    private View loadView() {
        View view = null;
        LayoutInflater inflater = getLayoutInflater();
        view = inflater.inflate(R.layout.save_dialog_layout, null);
        return view;
    }

    /**
     * 初始化对话框中的组件
     */
    private void initPartView(View parent) {
        titleEdit = (AppCompatEditText) parent.findViewById(R.id.titleEditView);
        authorEdit = (AppCompatEditText) parent.findViewById(R.id.authorEditView);
        detailEdit = (AppCompatEditText) parent.findViewById(R.id.detailEditView);

        titleEditLayout = (TextInputLayout) parent.findViewById(R.id.dialogTitleEditLayout);
        authorEditLayout = (TextInputLayout) parent.findViewById(R.id.dialogAuthorEditLayout);
        detailEditLayout = (TextInputLayout) findViewById(R.id.dialogDetailEditLayout);
    }

}
