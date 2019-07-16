package com.example.retrofitframemwork.login.activity;

import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.widget.TextView;

import com.example.retrofitframemwork.R;
import com.example.retrofitframemwork.login.presenter.SettingPresenter;
import com.example.retrofitframemwork.login.service.UpdateService;
import com.example.retrofitframemwork.login.view.ISettingView;
import com.framework.common.base_mvp.BaseActivity;
import com.framework.common.base_mvp.BasePresenter;
import com.framework.common.utils.LogUtil;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SettingActivity extends BaseActivity implements ISettingView {
    @BindView(R.id.tv_show_progress)
    TextView tvShowProgress;
    private SettingPresenter mPresenter;

    @Override
    public int getLayoutId() {
        return R.layout.activity_setting;
    }

    @Override
    public void bindData() {
        Intent intent = new Intent(this,UpdateService.class);
        bindService(intent,conn, Service.BIND_AUTO_CREATE);
    }

    @Override
    public void initEvent() {

    }

    @Override
    public BasePresenter getPresenter() {
        if (mPresenter == null) {
            mPresenter = new SettingPresenter();
        }
        return mPresenter;
    }

    ServiceConnection conn = new ServiceConnection() {
        /**
         * 与服务器端交互的接口方法 绑定服务的时候被回调，在这个方法获取绑定Service传递过来的IBinder对象，
         * 通过这个IBinder对象，实现宿主和Service的交互。
         */
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            // 获取Binder
            UpdateService.LocalBinder binder = (UpdateService.LocalBinder) service;
            binder.setProgressListener(new UpdateService.ProgressListener() {
                @Override
                public void onProgress(int progress) {
                    tvShowProgress.setText(String.valueOf(progress));
                }
            });
            Service mService = binder.getService();
            LogUtil.e("bind="+mService);
        }
        /**
         * 当取消绑定的时候被回调。但正常情况下是不被调用的，它的调用时机是当Service服务被意外销毁时，
         * 例如内存的资源不足时这个方法才被自动调用。
         */
        @Override
        public void onServiceDisconnected(ComponentName name) {
            //mService=null;
        }
    };

    @Override
    protected void onDestroy() {
        if(conn!=null){
            unbindService(conn);
        }
        super.onDestroy();
    }

    public static void start(Context context, Bundle bundle) {
        Intent starter = new Intent(context, SettingActivity.class);
        if(bundle!=null){
           starter.putExtras(bundle);
        }
        context.startActivity(starter);
    }
}