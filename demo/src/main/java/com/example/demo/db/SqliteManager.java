package com.example.demo.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import androidx.annotation.Nullable;

import com.example.demo.db.DbBean;
import com.example.demo.db.MySqlLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author zhangzhiqiang
 * @date 2019/10/11.
 * description：
 */
public class SqliteManager {
    /**
     * https://www.sqlite.org/rescode.html#busy
     * https://www.cnblogs.com/lijingcheng/p/4454884.html 关于锁
     * 一个线程会对应一个SqliteSession对象，这个是用ThreadLocal对象来维持的
     * 一个（SqlLiteOpenHelper）数据库对应唯一一个sqliteDabase对象，
     * 以及唯一一个SQLiteConnectionPool对象，然后各个线程之间共享这个SQLiteConnectionPool对象
     * 多个SqlLiteOpenHelper表示有多个sqliteDabase对象及多个SQLiteConnectionPool对象
     * database is locked 
     * SqlLiteOpenHelper中的openDatabase第一次打开是一个耗时操作（大约几秒，数据库初始化需要时间）
     * 不同的SqlLiteOpenHelper同时调用openDatabase会导致此异常
     * 因此需要单实例SqlLiteOpenHelper
     * 当有两个SqlLiteOpenHelper对象时，当一个数据库被锁定，另一个访问等待超过5秒也会报此异常
     * attempt to re-open an already-closed object
     * 当一个数据库被关闭后还在执行操作就会报此异常
     */
    /**
     * https://www.cnblogs.com/monkeysayhi/p/7654460.html
     * 加上volatile关键字，防止指令重排序
     */
    private volatile static MySqlLiteOpenHelper sHelper;
    private AtomicInteger mOpenCounter = new AtomicInteger();//原子性操作
    private SQLiteDatabase mDatabase;

    public static MySqlLiteOpenHelper getInstance(Context context) {
        if (sHelper == null) {
            synchronized (MySqlLiteOpenHelper.class) {
                if (sHelper == null) {
                    sHelper = new MySqlLiteOpenHelper(context);
                }
            }
        }
        return sHelper;
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

    /** 增加数据 */
    public long insert(String table, ContentValues values) {
        return mDatabase.insert(table, null, values);
    }
    /**
     * 批量增加数据
     *
     * @param table
     * @param list
     */
    public void insertBatch(String table, List<DbBean> list) {
        String sql = "insert into " + MySqlLiteOpenHelper.TABLE_CATEGORY + "("
                + MySqlLiteOpenHelper.COLUMN_ID + ","
                + MySqlLiteOpenHelper.COLUMN_ADDTIME + ","
                + MySqlLiteOpenHelper.COLUMN_CONTENT + ") values(?,?)";
        SQLiteStatement state = mDatabase.compileStatement(sql);
        mDatabase.beginTransaction();
        for(int i=0;i<list.size();i++){
            DbBean itemBean=list.get(i);
            state.bindString(1, itemBean.getContent());
            state.bindString(2, itemBean.getAddtime());
            state.executeInsert();
        }
        mDatabase.setTransactionSuccessful(); //设置事务成功
        mDatabase.endTransaction();
    }

    /** 删除数据 */
    public boolean delete(String table, String whereClause, String[] whereArgs) {
        return mDatabase.delete(table, whereClause, whereArgs) > 0;
    }

    /** 搜索数据 */
    public Cursor select(String table, String[] columns, String selection,
                         String[] selectionArgs) {
        return mDatabase.query(true, table, columns, selection, selectionArgs, null,
                null, "sort desc", null);
    }

    /** 更新数据 */
    public boolean updata(String table, ContentValues values,
                          String whereClause, String[] whereArgs) {
        return mDatabase.update(table, values, whereClause, whereArgs) > 0;
    }

    public void clearValues(String table) {
        String sql = "DELETE FROM " + table;
        mDatabase.execSQL(sql);
    }

    public static void insert(SQLiteDatabase db){
        List<DbBean> list = new ArrayList<>();
        for(int i=0;i<1000;i++){
            list.add(new DbBean(System.currentTimeMillis()+"","content"+i));
        }
        String sql = "insert into " + MySqlLiteOpenHelper.TABLE_CATEGORY + "("
                + MySqlLiteOpenHelper.COLUMN_ADDTIME + ","
                + MySqlLiteOpenHelper.COLUMN_CONTENT + ") values(?,?)";
        SQLiteStatement state = db.compileStatement(sql);
        db.beginTransaction();
        for(int i=0;i<list.size();i++){
            DbBean itemBean=list.get(i);
            state.bindString(1, itemBean.getContent());
            state.bindString(2, itemBean.getAddtime());
            state.executeInsert();
        }
        db.setTransactionSuccessful(); //设置事务成功
        db.endTransaction();
    }

    public static void insert2(SQLiteDatabase db){
        for(int i=0;i<1000;i++){
            ContentValues values = new ContentValues();
            values.put(MySqlLiteOpenHelper.COLUMN_ADDTIME,System.currentTimeMillis()+"");
            values.put(MySqlLiteOpenHelper.COLUMN_CONTENT,"content"+i);
            db.insert(MySqlLiteOpenHelper.TABLE_CATEGORY, null, values);
        }
    }
}
