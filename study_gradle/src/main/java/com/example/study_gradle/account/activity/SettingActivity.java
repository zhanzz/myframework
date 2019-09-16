package com.example.study_gradle.account.activity;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.framework.common.base_mvp.BasePresenter;
import com.framework.common.base_mvp.BaseActivity;
import com.example.study_gradle.account.presenter.SettingPresenter;
import com.example.study_gradle.account.view.ISettingView;
import com.example.study_gradle.R;

// 在支持路由的页面上添加注解(必选)
// 这里的路径需要注意的是至少需要有两级，/xx/xx
@Route(path = "/test/activity")
public class SettingActivity extends BaseActivity implements ISettingView {
    private SettingPresenter mPresenter;

    @Override
    public int getLayoutId() {
        return R.layout.activity_setting;
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
            mPresenter = new SettingPresenter();
        }
        return mPresenter;
    }
}