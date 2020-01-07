package com.framework.common.base_mvp;

import java.util.List;

/**
 * @author zhangzhiqiang
 * @date 2019/9/12.
 * description：
 */
public interface IPageBaseView<T> extends IBaseView {
    void onPageData(List<T> data, int currentPage, int pageSize);
    void onPageFail(int code, String msg,int currentPage);
}
