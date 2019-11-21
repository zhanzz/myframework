package com.example.demo.some_test.activity;

import android.content.Intent;
import android.net.Uri;

import com.framework.common.base_mvp.BasePresenter;
import com.framework.common.base_mvp.BaseActivity;
import com.example.demo.some_test.presenter.UriPresenter;
import com.example.demo.some_test.view.IUriView;
import com.example.demo.R;
import com.framework.common.utils.LogUtil;

public class UriActivity extends BaseActivity implements IUriView {
    private UriPresenter mPresenter;

    @Override
    public int getLayoutId() {
        return R.layout.activity_uri;
    }

    @Override
    public void getParamData(Intent intent) {
        Uri uri = intent.getData();
        if(uri!=null){
            LogUtil.e("zhang","uri="+uri.toString());
            LogUtil.e("zhang","scheme="+uri.getScheme());
            LogUtil.e("zhang","path="+uri.getPath());
            LogUtil.e("zhang","PathSegments="+uri.getPathSegments());
            LogUtil.e("zhang","LastPathSegment="+uri.getLastPathSegment());
            LogUtil.e("zhang","params="+uri.getQueryParameterNames());
        }
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
            mPresenter = new UriPresenter();
        }
        return mPresenter;
    }
}