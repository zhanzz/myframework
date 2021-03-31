package com.framework.common.base_mvp;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.framework.common.data.ActivityLifeCycleEvent;

import io.reactivex.ObservableTransformer;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

/**
 * @author zhangzhiqiang
 * @date 2019/4/12.
 * description：
 */
public interface IBaseView {
    void showLoading();

    void hideLoading();

    void showErrorView();

    void hideErrorView();

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

    /**
     * 将取消交给生命周期处理
     * @return 添加是否成功，如果为否说明页面已结束，需手动处理结束
     */
    @Nullable Boolean addDisposable(Disposable disposable);

    void removeDisposable(Disposable disposable);

    void addReloadRequest(Runnable runnable);
}
