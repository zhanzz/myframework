package com.example.retrofitframemwork.splash.activity;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

import com.example.retrofitframemwork.login.activity.MainActivity;
import com.framework.common.base_mvp.BasePresenter;
import com.framework.common.base_mvp.BaseActivity;
import com.example.retrofitframemwork.splash.presenter.SplashPresenter;
import com.example.retrofitframemwork.splash.view.ISplashView;
import com.example.retrofitframemwork.R;
import com.framework.common.utils.CountDownTimer;

public class SplashActivity extends BaseActivity implements ISplashView {
    private SplashPresenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            WindowManager.LayoutParams lp = this.getWindow().getAttributes();
            lp.layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES;
            this.getWindow().setAttributes(lp);
        }
        View decorView = getWindow().getDecorView();
        int systemUiVisibility = decorView.getSystemUiVisibility();
        int flags = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN;
        systemUiVisibility |= flags;
        getWindow().getDecorView().setSystemUiVisibility(systemUiVisibility);
        super.onCreate(savedInstanceState);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_splash;
    }

    @Override
    public void bindData() {
        MainActivity.start(getContext());
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