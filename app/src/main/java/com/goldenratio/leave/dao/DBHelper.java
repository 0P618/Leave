package com.goldenratio.leave.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Lxt ChenFengJY on 2017/2/14.
 * good good study ，day day up
 */

public class DBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "Leave";
    private static final int VERSION = 1;
    public static final String TABLE_NAME = "record";

    private static DBHelper mDBHelper;

    //单例模式--懒汉式
    public static synchronized DBHelper getInstance(Context context) {
        if (mDBHelper == null) {
            mDBHelper = new DBHelper(context.getApplicationContext());
        }
        return mDBHelper;
    }

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        String sql = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " (" +
                "[id] VARCHAR(50)," +
                "[start] VARCHAR(50)," +
                "[end] VARCHAR(50)," +
                "[remark] VARCHAR(50)," +
                "[type] VARCHAR(50)," +
                "[status] VARCHAR(20)," +
                "[created] VARCHAR(50)," +
                "[updated] VARCHAR(10));";
        //   "CONSTRAINT [] PRIMARY KEY ([id]))";
        sqLiteDatabase.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        String sql = "DROP TABLE IF EXISTS " + TABLE_NAME;
        sqLiteDatabase.execSQL(sql);
        onCreate(sqLiteDatabase);
    }
}
