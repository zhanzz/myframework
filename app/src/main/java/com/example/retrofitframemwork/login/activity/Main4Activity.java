package com.example.retrofitframemwork.login.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

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
    private Main4Presenter mPresenter;

    @Override
    public int getLayoutId() {
        return R.layout.activity_main4;
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
            mPresenter = new Main4Presenter();
        }
        return mPresenter;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @OnClick(R.id.btn_4jump)
    public void onClick() {
        Main2Activity.start(this);
    }

    public static void start(Context context) {
        Intent starter = new Intent(context, Main4Activity.class);
        context.startActivity(starter);
    }
}