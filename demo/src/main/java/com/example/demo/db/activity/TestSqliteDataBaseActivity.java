package com.example.demo.db.activity;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;

import com.example.demo.R;
import com.example.demo.R2;
import com.example.demo.db.MySqlLiteOpenHelper;
import com.example.demo.db.SqliteManager;
import com.example.demo.db.presenter.TestSqliteDataBasePresenter;
import com.example.demo.db.view.ITestSqliteDataBaseView;
import com.framework.common.base_mvp.BaseActivity;
import com.framework.common.base_mvp.BasePresenter;
import com.framework.common.utils.LogUtil;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class TestSqliteDataBaseActivity extends BaseActivity implements ITestSqliteDataBaseView {
    private TestSqliteDataBasePresenter mPresenter;
    private Thread threaOne,threadTwo;
    private boolean start;
    @Override
    public int getLayoutId() {
        return R.layout.activity_test_sqlite_data_base;
    }

    @Override
    public void bindData() {

    }

    @Override
    public void initEvent() {

    }

    @Override
    public BasePresenter getPresenter() {
        if (mPresenter == null) {
            mPresenter = new TestSqliteDataBasePresenter();
        }
        return mPresenter;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    public SQLiteDatabase oneDb;
    @OnClick({R2.id.btn_start, R2.id.btn_stop})
    public void onClick(View view) {
        int i = view.getId();
        if (i == R.id.btn_start) {
            if(threaOne==null){
                threaOne = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        while (start){
                            LogUtil.e("dbaddress","one=正在打开");
                            synchronized (TestSqliteDataBaseActivity.this){
                                if(oneDb==null){
                                    oneDb = new MySqlLiteOpenHelper(getContext()).getWritableDatabase();
                                    TestSqliteDataBaseActivity.this.notifyAll();
                                }
                            }
                            LogUtil.e("dbaddress","one="+oneDb.hashCode());
                            SqliteManager.insert2(oneDb);
                        }
                    }
                });
            }
            if(threadTwo==null){
                threadTwo = new Thread(new Runnable() {
                    SQLiteDatabase db;
                    @Override
                    public void run() {
                        while (start){
                            LogUtil.e("dbaddress","two=正在打开");
                            synchronized (TestSqliteDataBaseActivity.this){
                                if(oneDb==null){
                                    try {
                                        TestSqliteDataBaseActivity.this.wait();
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                            if(db==null){
                                db  = new MySqlLiteOpenHelper(getContext()).getWritableDatabase();
                            }
                            LogUtil.e("dbaddress","two="+db.hashCode());
                            SqliteManager.insert2(db);
                        }
                    }
                });
            }
            start = true;
            threaOne.start();threadTwo.start();
        } else if (i == R.id.btn_stop) {
            start = false;
        }
    }

    public static void start(Context context) {
        Intent starter = new Intent(context, TestSqliteDataBaseActivity.class);
        context.startActivity(starter);
    }
}