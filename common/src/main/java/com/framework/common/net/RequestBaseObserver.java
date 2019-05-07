package com.framework.common.net;

import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;

public  abstract class RequestBaseObserver<V> implements Observer<Object>,Disposable{

    protected Disposable disposable;
    private boolean isDisposable;
    public RequestBaseObserver() {}

    @Override
    public void onSubscribe(@NonNull Disposable d) {
        this.disposable = d;
    }

    @Override
    public void onNext(Object t) {
        if (disposable == null || !disposable.isDisposed()) {
            onSuccess((V)t);
        }
    }

    @Override
    public void onError(Throwable e) {
        if (disposable != null && !disposable.isDisposed()) {
            disposable.dispose();
        }
    }

    @Override
    public void onComplete() {

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
        if (disposable != null && !disposable.isDisposed()) {
            disposable.dispose();
        }
        isDisposable = true;
    }

    @Override
    public boolean isDisposed() {
        if (disposable != null) {
            return disposable.isDisposed();
        }
        return isDisposable;
    }
}