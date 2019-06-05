package com.example.demo.viewpager_fragment.view;

import com.framework.common.base_mvp.IBaseView;
import com.framework.model.demo.PresellBean;

public interface IPageFragmentView extends IBaseView {
    void onCategoryData(PresellBean bean);
}