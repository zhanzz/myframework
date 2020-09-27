package com.example.demo.product_detail.fragment;

import com.framework.common.base_mvp.BasePresenter;
import com.framework.common.base_mvp.BaseFragment;
import com.example.demo.product_detail.presenter.WebViewPresenter;
import com.example.demo.product_detail.view.IWebViewView;
import com.example.demo.R;

public class WebViewFragment extends BaseFragment implements IWebViewView {
    private WebViewPresenter mPresenter;

    @Override
    public int getLayoutId() {
        return R.layout.fragment_web_view;
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
            mPresenter = new WebViewPresenter();
        }
        return mPresenter;
    }
}
