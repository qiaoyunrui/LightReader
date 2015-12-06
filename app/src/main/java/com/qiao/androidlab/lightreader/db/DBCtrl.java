package com.qiao.androidlab.lightreader.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Administrator on 2015/12/2.
 */
public class DBCtrl extends SQLiteOpenHelper {

    private static final String DE_NAME = "lightpic.db";
    private static final int VERSION = 1;
    private static final String SQL_CREATE = "CREATE TABLE light_info(" +
            "id INTEGER PRIMARY KEY AUTOINCREMENT NULL," +
            "title VARCHAR(50)," +
            "author VARCHAR(20)," +
            "time VARCHAR(20)," +
            "detail VARCHAR(255)," +
            "picUri VARCHAR(50))";
    private static final String SQL_DROP = "DROP TABLE IF EXISTS light_info";

    public DBCtrl(Context context) {
        super(context, DE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DROP);
        db.execSQL(SQL_CREATE);
    }

}
