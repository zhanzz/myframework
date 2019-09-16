package com.framework.common.base_mvp;

/**
 * @author zhangzhiqiang
 * @date 2019/9/12.
 * description：
 */
public interface IPageBaseView<T> extends IBaseView {
    void onPageData(T data,int currentPage,int pageSize);
    void onPageFail(int code, String msg,int currentPage);
}
