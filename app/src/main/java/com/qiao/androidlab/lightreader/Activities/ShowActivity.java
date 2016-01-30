package com.qiao.androidlab.lightreader.Activities;

/**
 * Created by Administrator on 2015/12/1.
 */

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.qiao.androidlab.lightreader.Adapters.ShowFragmentStatePagerAdapter;
import com.qiao.androidlab.lightreader.Fragment.CoordinateFragments;
import com.qiao.androidlab.lightreader.Fragment.ImageFragment;
import com.qiao.androidlab.lightreader.Parts.LightPic;
import com.qiao.androidlab.lightreader.R;
import com.qiao.androidlab.lightreader.ReadUtil.BitmapManage;
import com.qiao.androidlab.lightreader.SurfaceViews.CoordinateSurfaceView;
import com.qiao.androidlab.lightreader.db.DBCtrl;
import com.qiao.androidlab.lightreader.db.DBUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * 获取图片后展示函数的界面
 */
public class ShowActivity extends AppCompatActivity {

    private static final String TITLE_1 = "图片";
    private static final String TITLE_2 = "图像";
    int picNum = 1;
    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private RelativeLayout showRoot;
    private FloatingActionButton save;
    private String path;
    private Bitmap[] bitmap = new Bitmap[]{null, null, null, null};
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

    private DBCtrl dbCtrl;
    private DBUtil dbUtil;

    private List<String> titles;
    private List<Fragment> fragments;

    private ImageFragment imageFragment;
    private CoordinateFragments coordinateFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_layout);
        initView();
        initTabAndPage();
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("光谱");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        initData();
        initEvent();
        setShowImage();
        manageBitmap();
    }

    /**
     * 设置Viewpager和TabLayout
     */
    private void initTabAndPage() {

        titles = new ArrayList<>();
        fragments = new ArrayList<>();
        titles.add(TITLE_1);
        titles.add(TITLE_2);
        tabLayout.addTab(tabLayout.newTab().setText(TITLE_1));
        tabLayout.addTab(tabLayout.newTab().setText(TITLE_2));
        imageFragment = new ImageFragment();
        coordinateFragment = new CoordinateFragments();
        fragments.add(imageFragment);
        fragments.add(coordinateFragment);
        ShowFragmentStatePagerAdapter adapter =
                new ShowFragmentStatePagerAdapter(getSupportFragmentManager(), titles, fragments);
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setTabsFromPagerAdapter(adapter);
    }

    /**
     * 处理图像并绘制
     */
    private void manageBitmap() {
        BitmapManage bitmapManage = new BitmapManage(bitmap);
        List<Integer> pixelsList = bitmapManage.manage();
        coordinateFragment.setPixelsList(pixelsList);
    }

    /**
     * 连接数据库
     */
    private void initData() {
        dbCtrl = new DBCtrl(this);
        dbUtil = new DBUtil();
        dbUtil.mOPenorCreateDatabase(dbCtrl);
    }

    private void initView() {
        toolbar = (Toolbar) findViewById(R.id.show_toolbar);
        showRoot = (RelativeLayout) findViewById(R.id.showRoot);
        save = (FloatingActionButton) findViewById(R.id.save);
        tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        viewPager = (ViewPager) findViewById(R.id.viewPager);
    }

    private void initEvent() {
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowMessageDialog();
            }
        });
    }

    /**
     * 设置获取到的图片
     */
    private void setShowImage() {   //设置获取到的照片
        if (true) { //相机拍照获得的照片
            picNum = getIntent().getIntExtra("picNum", 1);
            int sign;
            for (sign = 1; sign <= picNum; sign++) {
                path = getIntent().getStringExtra("picPath" + sign);
                bitmap[sign] = BitmapFactory.decodeFile(path);
            }
            Matrix matrix = new Matrix();
            matrix.postRotate(90);
            for (sign = 1; sign <= picNum; sign++) {
                bitmap[sign] = bitmap[sign].createBitmap(bitmap[sign], 0, 0, bitmap[sign].getWidth(), bitmap[sign].getHeight(), matrix, true);
            }
            if (bitmap[picNum] != null) {
                imageFragment.setImage(bitmap[picNum]); //显示最后一张照片
            }
            bm = bitmap[picNum];    //设置将要存储的图片
        } else {    //从相册获得的照片
            /**
             * 1.通过getData获取得到的Uri
             * 2.将Uri转化为bitmap
             * 3.ImageView设置图像
             */
        }
    }

    //展示对话框
    private void ShowMessageDialog() {
        View view = loadView();
        initPartView(view);
        setTextLength(titleEditLayout, 10);
        setTextLength(authorEditLayout, 10);
        setTextLength(detailEditLayout, 100);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(view)
                .setTitle("请输入信息")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        getMessage();
                        createLightPic();
                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mmZ");
                        String picName = dateFormat.format(new java.util.Date());
                        File file = new File(Environment.getExternalStorageDirectory().getPath() + "/光之解读者", picName + ".jpg");    //设置保存图片的路径
                        FileOutputStream outStream = null;
                        try {
                            //打开指定文件对应的输出
                            outStream = new FileOutputStream(file);
                            //把位图输出到指定文件中
                            bm.compress(Bitmap.CompressFormat.JPEG, 100, outStream);
                            outStream.close();
                            Snackbar.make(showRoot, "保存成功", Snackbar.LENGTH_SHORT).show();
                        } catch (IOException e) {
                            e.printStackTrace();
                            Snackbar.make(showRoot, "未知错误", Snackbar.LENGTH_SHORT).show();
                        }
                        Uri picUri = Uri.parse(file.toString());    //将File转化为Uri
                        dbUtil.mDBInsert(lightPic, picUri);  //将这条记录存入数据库
                        //Intent intent = new Intent(ShowActivity.this, MainActivity.class);
                        //startActivity(intent);
                        finish();
                        /**
                         * 1.将图片保存在本地。oK
                         * 2.将对象存入数据库 OK
                         * 3.回到主页面
                         */
                    }
                })
                .setNegativeButton("取消", null);
        builder.create().show();
    }

    /**
     * 获取从对话框输入的信息
     */
    private void getMessage() {
        title = titleEdit.getText().toString();
        author = authorEdit.getText().toString();
        detail = detailEdit.getText().toString();
    }

    /**
     * 构建一个LightPic对象
     */
    private void createLightPic() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        time = dateFormat.format(new java.util.Date());
        lightPic = new LightPic(title, author, detail, time, bm);
    }

    /**
     * 加载对话框视图
     *
     * @return
     */
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
        detailEditLayout = (TextInputLayout) parent.findViewById(R.id.dialogDetailEditLayout);
    }

    /**
     * 设置输入框输入的最大长度
     *
     * @param textInputLayout
     * @param length
     */
    private void setTextLength(final TextInputLayout textInputLayout, final int length) {
        textInputLayout.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > length) {
                    textInputLayout.setError("不能大于" + length + "个字符");     //设置错误信息
                    textInputLayout.setErrorEnabled(true);     //显示错误信息
                } else {
                    textInputLayout.setErrorEnabled(false);     //隐藏错误信息
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == android.R.id.home) {
            onBackPressed();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


}
