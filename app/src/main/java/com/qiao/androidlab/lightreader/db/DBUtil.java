package com.qiao.androidlab.lightreader.db;

/**
 * Created by Administrator on 2015/12/2.
 */

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

import com.qiao.androidlab.lightreader.Parts.LightPic;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 数据库工具类
 */
public class DBUtil {

    private static final String SQL_INSERT = "INSERT INTO light_info VALUES(null,?,?,?,?,?)";
    private static final String SQL_DETELE = "DELETE FROM light_info WHERE id = ?";
    private static final String SQL_SELECT = "SELECT * FROM light_info";
    private static final int SUCCESS = 1;
    private static final int FAIL = 0;
    private SQLiteDatabase mdb;
    private String[] indexInsert;
    private String[] indexDelete;
    private LightPic mLightPic = null;

    /**
     * 创建或打开数据库
     *
     * @param dbCtrl
     * @return 返回一个数据库
     */
    public void mOPenorCreateDatabase(DBCtrl dbCtrl) {
        mdb = dbCtrl.getReadableDatabase();
    }

    /**
     * 向数据库中插入信息
     *
     * @param lightPic
     * @param picUri
     * @return
     */
    public int mDBInsert(LightPic lightPic, Uri picUri) {

        if (mdb == null || lightPic == null || picUri == null) {
            return FAIL;
        }
        indexInsert = new String[]{lightPic.getTitle(), lightPic.getAuthor(),
                lightPic.getTime(), lightPic.getDetail(), picUri.toString()};
        mdb.execSQL(SQL_INSERT, indexInsert);
        return SUCCESS;
    }

    /**
     * 关闭数据库
     *
     * @param dbCtrl
     */
    public void closeDatabase(DBCtrl dbCtrl) {
        dbCtrl.close();
        if (mdb != null) {
            mdb.close();
        }
    }

    /**
     * 数据库删除某一项
     *
     * @param id
     * @return
     */
    public int mDBDelete(int id) {
        if (mdb == null) {
            return FAIL;
        }
        indexDelete = new String[]{id + ""};
        mdb.execSQL(SQL_DETELE, indexDelete);
        return SUCCESS;
    }


    /**
     * 获取数据库中所有的信息
     *
     * @param context
     * @return
     * @throws IOException
     */
    public List<LightPic> mDBSelect(Context context) throws IOException {
        List<LightPic> lightList = new ArrayList<>();
        Cursor cursor = mdb.rawQuery(SQL_SELECT, null);
        while (cursor.moveToNext()) {
            mLightPic = new LightPic();
            mLightPic.setId(cursor.getInt(0));
            mLightPic.setTitle(cursor.getString(1));
            mLightPic.setAuthor(cursor.getString(2));
            mLightPic.setTime(cursor.getString(3));
            mLightPic.setDetail(cursor.getString(4));
            mLightPic.setBm(BitmapFactory.decodeFile(cursor.getString(5)));
            lightList.add(mLightPic);
        }
        cursor.close();
        return lightList;
    }

}
