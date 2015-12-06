package com.qiao.androidlab.lightreader.db;

/**
 * Created by Administrator on 2015/12/2.
 */

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

import com.qiao.androidlab.lightreader.Parts.LightPic;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 数据库工具类
 */
public class DBUtil {

    private SQLiteDatabase mdb;
    private static final String SQL_INSERT = "INSERT INTO light_info VALUES(null,?,?,?,?,?)";
    private static final String SQL_DETELE = "DELETE FROM light_info WHERE id = ?";
    private static final String SQL_SELECT = "SELECT * FROM light_info";
    private String[] indexInsert;
    private String[] indexDelete;
    private static final int SUCCESS = 1;
    private static final int FAIL = 0;
    private LightPic mLightPic = null;

    /**
     * 创建或打开数据库
     *
     * @param dbCtrl
     * @return 返回一个数据库
     */
    public void mOPenorCreateDatabase(DBCtrl dbCtrl) {
        mdb = dbCtrl.getReadableDatabase();
//        return mdb;
    }

    public int mDBInsert(LightPic lightPic, Uri picUri) {

        if (mdb == null || lightPic == null || picUri == null) {
            return FAIL;
        }
        indexInsert = new String[]{lightPic.getTitle(), lightPic.getAuthor(),
                lightPic.getTime(), lightPic.getDetail(), picUri.toString()};
        mdb.execSQL(SQL_INSERT, indexInsert);
        return SUCCESS;
    }

    public int mDBDelete(int id) {
        if (mdb == null) {
            return FAIL;
        }
        indexDelete = new String[]{id + ""};
        mdb.execSQL(SQL_DETELE, indexDelete);
        return SUCCESS;
    }

    public List<LightPic> mDBSelect(Context context) throws IOException {
        List<LightPic> lightList = new ArrayList<>();
        Cursor cursor = mdb.rawQuery(SQL_SELECT, null);
//        Log.i("HELLO",cursor.toString());
        while (cursor.moveToNext()) {
            mLightPic.setId(cursor.getInt(0));
            mLightPic.setTitle(cursor.getString(1));
            mLightPic.setAuthor(cursor.getString(2));
            mLightPic.setTime(cursor.getString(3));
            mLightPic.setDetail(cursor.getString(4));
            mLightPic.setBm(MediaStore.Images.Media.getBitmap(context.getContentResolver(),
                    Uri.parse(Uri.encode(cursor.getString(5)))));
            lightList.add(mLightPic);
        }

        return lightList;
    }

}
