package com.example.demo.incremental_updating.activity;

import android.content.Context;
import android.content.Intent;

import com.example.demo.incremental_updating.TestLinkSo;
import com.framework.common.base_mvp.BasePresenter;
import com.framework.common.base_mvp.BaseActivity;
import com.example.demo.incremental_updating.presenter.TestAddPresenter;
import com.example.demo.incremental_updating.view.ITestAddView;
import com.example.demo.R;
import com.framework.common.utils.LogUtil;

public class TestAddActivity extends BaseActivity implements ITestAddView {
    private TestAddPresenter mPresenter;

    @Override
    public int getLayoutId() {
        return R.layout.activity_test_add;
    }

    @Override
    public void bindData() {
        LogUtil.e("ndk",TestLinkSo.getThirdString());
    }

    @Override
    public void initEvent() {

    }

    @Override
    public BasePresenter getPresenter() {
        if (mPresenter == null) {
            mPresenter = new TestAddPresenter();
        }
        return mPresenter;
    }

    public static void start(Context context) {
        Intent starter = new Intent(context, TestAddActivity.class);
        context.startActivity(starter);
    }
}