package com.example.demo.some_test.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import com.example.demo.R;
import com.example.demo.R2;
import com.example.demo.service.ForegroundService;
import com.example.demo.some_test.presenter.TestServiceAndNoticePresenter;
import com.example.demo.some_test.view.ITestServiceAndNoticeView;
import com.framework.common.base_mvp.BaseActivity;
import com.framework.common.base_mvp.BasePresenter;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class TestServiceAndNoticeActivity extends BaseActivity implements ITestServiceAndNoticeView {
    @BindView(R2.id.btn_start)
    Button btnStart;
    @BindView(R2.id.btn_end)
    Button btnEnd;
    private TestServiceAndNoticePresenter mPresenter;

    @Override
    public int getLayoutId() {
        return R.layout.activity_test_service_and_notice;
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
            mPresenter = new TestServiceAndNoticePresenter();
        }
        return mPresenter;
    }

    public static void start(Context context, Bundle bundle) {
        Intent starter = new Intent(context, TestServiceAndNoticeActivity.class);
        if(bundle!=null){
            starter.putExtras(bundle);
        }
        context.startActivity(starter);
    }

    @OnClick({R2.id.btn_start, R2.id.btn_end})
    public void onClick(View view) {
        int i = view.getId();
        if (i == R.id.btn_start) {
            Intent intent = new Intent(this, ForegroundService.class);
            intent.putExtra("cmd",0);//0,标记为前台服务,1,取消标记前台服务
            startService(intent);
        } else if (i == R.id.btn_end) {
            Intent intent = new Intent(this, ForegroundService.class);
            intent.putExtra("cmd",1);//0标记为前台服务,1,取消标记前台服务
            startService(intent);
        }
    }

    @Override
    protected void onDestroy() {
        Intent intent = new Intent(this, ForegroundService.class);
        intent.putExtra("cmd",2);//0,标记为前台服务,1,取消标记前台服务
        stopService(intent);
        super.onDestroy();
    }
}