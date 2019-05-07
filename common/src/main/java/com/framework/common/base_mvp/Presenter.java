package com.framework.common.base_mvp;

public interface Presenter<T extends IBaseView> {
    void attachView(T view);

    void detachView();
}