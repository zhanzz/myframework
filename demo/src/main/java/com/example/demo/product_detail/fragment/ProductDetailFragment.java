package com.example.demo.product_detail.fragment;

import com.framework.common.base_mvp.BasePresenter;
import com.framework.common.base_mvp.BaseFragment;
import com.example.demo.product_detail.presenter.ProductDetailPresenter;
import com.example.demo.product_detail.view.IProductDetailView;
import com.example.demo.R;

public class ProductDetailFragment extends BaseFragment implements IProductDetailView {
    private ProductDetailPresenter mPresenter;

    @Override
    public int getLayoutId() {
        return R.layout.fragment_product_detail;
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
            mPresenter = new ProductDetailPresenter();
        }
        return mPresenter;
    }
}
