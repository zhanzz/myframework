package com.example.demo.vlayout.view;

import com.framework.common.base_mvp.IBaseView;
import com.framework.model.demo.ActivityBean;
import com.framework.model.demo.ProductBean;

import java.util.List;

public interface IStudyVlayoutView extends IBaseView {
    void onHomeData(ActivityBean bean);

    void onProductList(List<ProductBean> data, int mCurrentPage);

    void onProductFail(int mCurrentPage);
}