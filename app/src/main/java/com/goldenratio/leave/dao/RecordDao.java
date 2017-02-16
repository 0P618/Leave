package com.goldenratio.leave.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * Created by Lxt ChenFengJY on 2017/2/14.
 * good good study ，day day up
 */

public class RecordDao {
    private DBHelper mDBHelper;
    private SQLiteDatabase db;

    public RecordDao(Context context) {
        //  mDBHelper = DBHelper.getInstance(context);
        mDBHelper = new DBHelper(context);
        db = mDBHelper.getWritableDatabase();
    }

    /**
     * 用于插入、删除、修改单条数据
     *
     * @param sql
     */
    public void exec(String sql) {
        db.beginTransaction();
        try {
            db.execSQL(sql);
            // 设置事务标志为成功，当结束事务时就会提交事务
            db.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
            Log.i("lxt", e.getMessage());
        } finally {
            // 结束事务
            db.endTransaction();
            db.close();
        }
    }

    /**
     * @param values
     */
    public void insertAllData(ContentValues values) {
        try {
            db.beginTransaction();
            db.insertOrThrow(DBHelper.TABLE_NAME, null, values);
            db.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
            Log.i("lxt", "insertAllData: " + e.getMessage());
        } finally {
            db.endTransaction();
        }
        Log.i("lxt", "insertAllData: " + values.toString());
    }

    /**
     * @param sql
     * @param selectionArgs 条件参数
     * @return
     */
    public Cursor queryData(String sql, String[] selectionArgs) {
        db = mDBHelper.getReadableDatabase();
        return db.rawQuery(sql, selectionArgs);
    }
}
