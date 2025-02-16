package com.example.retrofitframemwork.login.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;

import com.example.retrofitframemwork.R;
import com.framework.common.base_mvp.BaseActivity;
import com.framework.common.base_mvp.BasePresenter;
import com.framework.common.utils.LogUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class Main3Activity extends BaseActivity {
    @BindView(R.id.jump)
    Button jump;

    @Override
    public int getLayoutId() {
        return R.layout.activity_main3;
    }

    @Override
    public void bindData() {
        Uri uri = getIntent().getData();
        LogUtil.e("zhang","uri="+uri.toString());
        LogUtil.e("zhang","scheme="+uri.getScheme());
        LogUtil.e("zhang","path="+uri.getPath());
        LogUtil.e("zhang","host="+uri.getHost());
    }

    @Override
    public void initEvent() {

    }

    @Override
    protected BasePresenter getPresenter() {
        return null;
    }

    public static void start(Context context) {
        Intent starter = new Intent(context, Main3Activity.class);
        context.startActivity(starter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @OnClick(R.id.jump)
    public void onClick() {
        Main4Activity.start(this);
    }
}
