package com.example.retrofitframemwork.splash.activity;

import com.example.retrofitframemwork.login.activity.MainActivity;
import com.framework.common.base_mvp.BasePresenter;
import com.framework.common.base_mvp.BaseActivity;
import com.example.retrofitframemwork.splash.presenter.SplashPresenter;
import com.example.retrofitframemwork.splash.view.ISplashView;
import com.example.retrofitframemwork.R;

public class SplashActivity extends BaseActivity implements ISplashView {
    private SplashPresenter mPresenter;

    @Override
    public int getLayoutId() {
        return R.layout.activity_splash;
    }

    @Override
    public void bindData() {
        MainActivity.start(this);
        finish();
    }

    @Override
    public void initEvent() {

    }

    @Override
    public BasePresenter getPresenter() {
        if (mPresenter == null) {
            mPresenter = new SplashPresenter();
        }
        return mPresenter;
    }
}