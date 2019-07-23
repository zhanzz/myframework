package com.framework.common.base_mvp;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.framework.common.data.ActivityLifeCycleEvent;

import io.reactivex.ObservableTransformer;
import io.reactivex.disposables.CompositeDisposable;

/**
 * @author zhangzhiqiang
 * @date 2019/4/12.
 * description：
 */
public interface IBaseView {
    void showLoading();

    void hideLoading();

    void showErrorView();

    /**
     * 显示此loading的目的是不能点击内容视图
     */
    void showLoadingDialog();

    void hideLoadingDialog();

    <T> ObservableTransformer<T, T> bindUntilEvent(@NonNull final ActivityLifeCycleEvent event);

    void showToast(String msg);

    void showToast(String msg,int gravity,int duration);

    @Nullable Context getContext();

    @Nullable CompositeDisposable getCompositeDisposable();
}
