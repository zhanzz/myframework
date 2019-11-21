package com.example.demo.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import androidx.annotation.Nullable;

import com.framework.common.utils.LogUtil;

/**
 * @author zhangzhiqiang
 * @date 2019/8/6.
 * description：
 */
public class MySqlLiteOpenHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "info";
    private static final int version = 1;

    public static final String TABLE_CATEGORY = "category";

    public static final String COLUMN_ID = "id";
    public static final String COLUMN_ADDTIME = "addtime";
    public static final String COLUMN_CONTENT = "concent";

    private static final String CREATE_TABLE_CATEGORY = "create table if not exists "
            + TABLE_CATEGORY
            + " ("
            + COLUMN_ID + " integer primary key autoincrement, "
            + COLUMN_ADDTIME
            +" text, "
            + COLUMN_CONTENT + " text);";
    public MySqlLiteOpenHelper(@Nullable Context context) {
        super(context, DB_NAME, null, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_CATEGORY);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        LogUtil.e("zhang","执行了onUpgrade");
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_CATEGORY); //如果更新数据库版本删除表从新开始
        onCreate(db);
    }
}
