package com.example.demo.keybord.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.example.demo.R;
import com.example.demo.R2;
import com.example.demo.keybord.fragment.TestInputFragment;
import com.example.demo.keybord.presenter.TestInputPresenter;
import com.example.demo.keybord.view.ITestInputView;
import com.framework.common.base_mvp.BaseActivity;
import com.framework.common.base_mvp.BasePresenter;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class TestInputActivity extends BaseActivity implements ITestInputView {
    private TestInputPresenter mPresenter;

    @Override
    public int getLayoutId() {
        return R.layout.activity_test_input;
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
            mPresenter = new TestInputPresenter();
        }
        return mPresenter;
    }

    public static void start(Context context) {
        Intent starter = new Intent(context, TestInputActivity.class);
        context.startActivity(starter);
    }

    @OnClick(R2.id.tv_show_dialog)
    public void onClick() {
        TestInputFragment.newInstance().showNow(getSupportFragmentManager(),"testInput");
    }
}