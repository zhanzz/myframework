package com.example.demo.product_detail.fragment;

import android.view.View;
import android.webkit.WebView;

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
        WebView webView = getView().findViewById(R.id.webView);
        webView.loadUrl("https://www.jianshu.com/p/3682dde60dbf");
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
