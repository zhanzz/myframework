package com.example.demo.viewpager_fragment.fragment;

import com.framework.common.base_mvp.BasePresenter;
import com.framework.common.base_mvp.BaseFragment;
import com.example.demo.viewpager_fragment.presenter.TestMakePresenter;
import com.example.demo.viewpager_fragment.view.ITestMakeView;
import com.example.demo.R;

public class TestMakeFragment extends BaseFragment implements ITestMakeView {
    private TestMakePresenter mPresenter;

    @Override
    public int getLayoutId() {
        return R.layout.fragment_test_make;
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
            mPresenter = new TestMakePresenter();
        }
        return mPresenter;
    }
}
