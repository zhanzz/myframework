package com.framework.common.retrofit;

import android.util.Log;

import com.framework.common.data.Result;
import com.framework.common.exception.ApiException;
import com.framework.common.exception.CustomException;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
/**
 * Subscriber基类,可以在这里处理client网络连接状况
 * （比如没有，没有4g，没有联网，加载框处理等）
 * @author pengl
 */
public  abstract class ApiSubscriber<T,R> implements Observer<T>,Disposable{
    private Disposable disposable;
    private boolean isDisposable;
    public ApiSubscriber() {
    }

    @Override
    public void onError(Throwable e) {
        onFail(CustomException.handleException(e));
        if (disposable != null && !disposable.isDisposed()) {
            disposable.dispose();
        }
    }

    @Override
    public void onNext(T data) {
        if (disposable == null || !disposable.isDisposed()) {
            if(data == null)
                return;
            Result baseResp = (Result) data;
            int code = baseResp.getCode();

            if(code == 1){
                R model = (R) baseResp.getData();
                onSuccess(model,baseResp.getCode(),baseResp.getMessage());
            }else{
                onFail(new ApiException(code,baseResp.getMessage()));
            }
        }
    }

    @Override
    public void onComplete() {

    }

    @Override
    public void onSubscribe(Disposable d) {
        disposable = d;
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
        if(disposable!=null){
            disposable.dispose();
        }
        isDisposable = true;
    }

    @Override
    public boolean isDisposed() {
        return isDisposable;
    }
}