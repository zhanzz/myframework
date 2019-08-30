package com.example.demo.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author zhangzhiqiang
 * @date 2019/8/6.
 * description：
 */
public class MySqlLiteOpenHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "info";
    private static final int version = 1;
    /**
     * https://www.cnblogs.com/monkeysayhi/p/7654460.html
     * 加上volatile关键字，防止指令重排序
     */
    private volatile static MySqlLiteOpenHelper sHelper;
    private AtomicInteger mOpenCounter = new AtomicInteger();//原子性操作
    private SQLiteDatabase mDatabase;

    public MySqlLiteOpenHelper getInstance(Context context) {
        if (sHelper == null) {
            synchronized (MySqlLiteOpenHelper.class) {
                if (sHelper == null) {
                    sHelper = new MySqlLiteOpenHelper(context);
                }
            }
        }
        return sHelper;
    }

    private MySqlLiteOpenHelper(@Nullable Context context) {
        super(context, DB_NAME, null, version);
    }

    //打开数据库方法
    public synchronized  SQLiteDatabase openDatabase() {
        if (mOpenCounter.incrementAndGet() == 1) {
            // Opening new database
            try {
                mDatabase = sHelper.getWritableDatabase();
            } catch (Exception e) {
                mDatabase = sHelper.getReadableDatabase();
            }
        }
        return mDatabase;
    }

    //关闭数据库方法
    public synchronized void closeDatabase() {
        if (mOpenCounter.decrementAndGet() == 0) {
            // Closing database
            mDatabase.close();
        }
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
