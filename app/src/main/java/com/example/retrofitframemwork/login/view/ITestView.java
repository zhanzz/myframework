package com.example.retrofitframemwork.login.view;

import android.graphics.Bitmap;

import com.framework.common.base_mvp.IBaseView;
import com.framework.model.UserBean;

import java.util.List;

/**
 * @author zhangzhiqiang
 * @date 2019/4/17.
 * description：
 */
public interface ITestView extends IBaseView {
    void onPageData(List<String> data,int currentPage);
    void onLoadFail();
}
