package com.example.retrofitframemwork.login.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.slidingpanelayout.widget.SlidingPaneLayout;

import com.example.retrofitframemwork.R;
import com.example.retrofitframemwork.login.presenter.Main4Presenter;
import com.example.retrofitframemwork.login.view.IMain4View;
import com.framework.common.base_mvp.BaseActivity;
import com.framework.common.base_mvp.BasePresenter;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class Main4Activity extends BaseActivity implements IMain4View {
    @BindView(R.id.btn_4jump)
    Button btn4jump;
    @BindView(R.id.slidingPanelLayout)
    SlidingPaneLayout slidingPanelLayout;
    @BindView(R.id.iv_matrix)
    ImageView ivMatrix;
    private Main4Presenter mPresenter;

    @Override
    public int getLayoutId() {
        return R.layout.activity_main4;
    }

    @Override
    public void bindData() {
        slidingPanelLayout.setSliderFadeColor(Color.TRANSPARENT);//设置面板的侧滑渐变色
        slidingPanelLayout.setPanelSlideListener(new SlidingPaneLayout.PanelSlideListener() {
            @Override
            public void onPanelSlide(View panel, float slideOffset) {
                //
            }

            @Override
            public void onPanelOpened(View panel) {

            }

            @Override
            public void onPanelClosed(View panel) {

            }
        });
    }

    @Override
    public void initEvent() {

    }

    @Override
    public BasePresenter getPresenter() {
        if (mPresenter == null) {
            mPresenter = new Main4Presenter();
        }
        return mPresenter;
    }

    @OnClick(R.id.btn_4jump)
    public void onClick() {
        Main2Activity.start(this);
    }

    public static void start(Context context) {
        Intent starter = new Intent(context, Main4Activity.class);
        context.startActivity(starter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}