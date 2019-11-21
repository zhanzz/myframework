package com.framework.common.net;

import com.framework.common.exception.ApiException;
import com.framework.common.exception.CustomException;

import java.util.concurrent.atomic.AtomicReference;

import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.internal.disposables.DisposableHelper;

public  abstract class RequestBaseObserver<V> extends AtomicReference<Disposable> implements Observer<Object>,Disposable{

    public RequestBaseObserver() {}

    @Override
    public void onSubscribe(@NonNull Disposable d) {
        DisposableHelper.setOnce(this, d);
    }

    @Override
    public void onNext(Object t) {
        if (!isDisposed()) {
            onSuccess((V)t);
        }
    }

    @Override
    public void onError(Throwable e) {
        if (!isDisposed()) {
            lazySet(DisposableHelper.DISPOSED);
            try {
                onFail(CustomException.handleException(e));
            } catch (Throwable t) {
                t.printStackTrace();
            }
        }
    }

    /**
     * 错误回调
     */
    protected abstract void onFail(ApiException ex);

    @Override
    public void onComplete() {
        if (!isDisposed()) {
            lazySet(DisposableHelper.DISPOSED);
            try {
                //onComplete.run();
            } catch (Throwable e) {
            }
        }
    }


    /**
     * 上传、下载 需重写此方法，更新进度
     * @param percent 进度百分比 数
     */
    protected void onProgress(String percent){

    }

    /**
     * 请求成功 回调
     *
     * @param t 请求返回的数据
     */
    protected abstract void onSuccess(V t);

    @Override
    public void dispose() {
        DisposableHelper.dispose(this);
        onFail(new ApiException(CustomException.DISPOSED,"操作已取消"));
    }

    @Override
    public boolean isDisposed() {
        return get() == DisposableHelper.DISPOSED;
    }
}