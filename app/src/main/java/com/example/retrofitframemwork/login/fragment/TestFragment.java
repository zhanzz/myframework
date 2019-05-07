package com.example.retrofitframemwork.login.fragment;

import android.os.Bundle;

import com.example.retrofitframemwork.R;
import com.framework.common.base_mvp.BaseFragment;
import com.framework.common.base_mvp.BasePresenter;

/**
 * @author zhangzhiqiang
 * @date 2019/4/23.
 * description：
 */
public class TestFragment extends BaseFragment{
    @Override
    protected BasePresenter getPresenter() {
        return null;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_test;
    }

    @Override
    public void bindData() {

    }

    @Override
    public void initEvent() {

    }

    public static TestFragment newInstance(int position) {
        Bundle args = new Bundle();
        args.putInt("position",position);
        TestFragment fragment = new TestFragment();
        fragment.setArguments(args);
        return fragment;
    }
}
