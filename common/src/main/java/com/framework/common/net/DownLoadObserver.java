package com.framework.common.net;

import com.framework.common.callBack.FileCallBack;
import com.framework.common.exception.ApiException;
import com.framework.common.exception.CustomException;
import com.framework.common.utils.LogUtil;

import java.util.concurrent.atomic.AtomicReference;

import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.internal.disposables.DisposableHelper;
import okhttp3.ResponseBody;

public  abstract class DownLoadObserver<V extends ResponseBody> extends AtomicReference<Disposable> implements Observer<V>,Disposable{
    private FileCallBack fileCallBack;
    public DownLoadObserver(FileCallBack fileCallBack) {
        this.fileCallBack = fileCallBack;
    }

    @Override
    public void onSubscribe(@NonNull Disposable d) {
        DisposableHelper.setOnce(this, d);
    }

    @Override
    public void onNext(V t) {
        if (!isDisposed()) {
            fileCallBack.saveFile(t);
        }
    }

    @Override
    public void onError(Throwable e) {
        if (!isDisposed()) {
            lazySet(DisposableHelper.DISPOSED);
            try {
                LogUtil.e("zhang","onError="+e.getMessage());
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

    @Override
    public void dispose() {
        DisposableHelper.dispose(this);
        LogUtil.e("zhang","dispose=操作已取消");
        //onFail(new ApiException(CustomException.DISPOSED,"操作已取消"));
    }

    @Override
    public boolean isDisposed() {
        return get() == DisposableHelper.DISPOSED;
    }
}