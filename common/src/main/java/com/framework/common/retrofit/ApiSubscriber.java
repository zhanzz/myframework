package com.framework.common.retrofit;

import android.util.Log;

import com.framework.common.data.Result;
import com.framework.common.exception.ApiException;
import com.framework.common.exception.CustomException;
import java.util.concurrent.atomic.AtomicReference;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.exceptions.CompositeException;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.internal.disposables.DisposableHelper;
import io.reactivex.plugins.RxJavaPlugins;
/**
 * Subscriber基类,可以在这里处理client网络连接状况
 * （比如没有，没有4g，没有联网，加载框处理等）
 * @author pengl
 */
public  abstract class ApiSubscriber<R> extends AtomicReference<Disposable> implements Observer<Result<R>>,Disposable{
    @Override
    public void onError(Throwable e) {
        if (!isDisposed()) {
            lazySet(DisposableHelper.DISPOSED);
            try {
                onFail(CustomException.handleException(e));
            } catch (Throwable t) {
            }
        }
    }

    @Override
    public void onNext(Result<R> data) {
        if (!isDisposed()) {
            try {
                if(data == null)
                    return;
                Result baseResp = data;
                int code = baseResp.getCode();

                if(isBusinessSuccess(code)){
                    R model = (R) baseResp.getData();
                    onSuccess(model,baseResp.getCode(),baseResp.getMessage());
                }else{
                    onFail(new ApiException(code,baseResp.getMessage()));
                }
            } catch (Throwable e) {
                Exceptions.throwIfFatal(e);
                get().dispose();
                onError(e);
            }
        }
    }

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
    public void onSubscribe(Disposable d) {
        DisposableHelper.setOnce(this, d);
    }

    /**
     * 错误回调
     */
    protected abstract void onFail(ApiException ex);

    /**
     * 成功回调
     * @param data
     */
    public abstract void onSuccess(R data,int code,String msg);

    @Override
    public void dispose() {
        DisposableHelper.dispose(this);
        onFail(new ApiException(CustomException.DISPOSED,"操作已取消"));
    }

    @Override
    public boolean isDisposed() {
        return get() == DisposableHelper.DISPOSED;
    }

    /**
     * 业务成功
     * @param code 后台返回状态码
     * @return
     */
    private boolean isBusinessSuccess(int code){
        if(code == 1 || code==0){
            return true;
        }
        return false;
    }
}