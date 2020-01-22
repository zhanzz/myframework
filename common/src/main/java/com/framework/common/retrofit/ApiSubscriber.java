package com.framework.common.retrofit;

import com.framework.common.data.Result;
import com.framework.common.data.operation.UserOperation;
import com.framework.common.exception.ApiException;
import com.framework.common.exception.CustomException;
import com.framework.common.utils.EventBusUtils;
import com.framework.common.manager.Events;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.internal.disposables.DisposableHelper;
/**
 * Subscriber基类,可以在这里处理client网络连接状况
 * （比如没有，没有4g，没有联网，加载框处理等）
 * @author pengl
 */
public  abstract class ApiSubscriber<T> extends AtomicReference<Disposable> implements Observer<Result<T>>,Disposable{
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

    @Override
    public void onNext(Result<T> data) {
        if (!isDisposed()) {
            try {
                if(data == null){
                    onFail(new ApiException(CustomException.EMPTY_DATA,"返回数据为空"));
                    return;
                }
                int code = data.getCode();
                if(isBusinessSuccess(code)){
                    T model = data.getData();
                    if(model==null){//是否需要为null报错误
                        ParameterizedType type = (ParameterizedType)getClass().getGenericSuperclass();
                        if(type!=null){
                            Type clazz = type.getActualTypeArguments()[0];
                            /**
                             * 父类.class.isAssignableFrom(子类.class)   (Assignable)可转让的
                             * 子类实例 instanceof 父类类型
                             */
                            if(clazz instanceof Class){
                                boolean isObject = ((Class)clazz).isAssignableFrom(Object.class);
                                if(!isObject){
                                    onFail(new ApiException(code,"data数据为空"));
                                    return;
                                }
                            }else if(clazz instanceof ParameterizedType){//List<T>
                                Type rawType = ((ParameterizedType)clazz).getRawType();
                                if(rawType != List.class){
                                    onFail(new ApiException(code,"data数据为空"));
                                    return;
                                }
                            }
                        }
                    }
                    onSuccess(model,data.getCode(),data.getMessage());
                }else{
                    if(isLoginExpired(code)&& UserOperation.getInstance().isLogin()){
                        UserOperation.getInstance().setToken("");
                        EventBusUtils.post(new Events.LoginOut());
                    }
                    onFail(new ApiException(code,data.getMessage()));
                }
            } catch (Throwable e) {
                Exceptions.throwIfFatal(e);
                get().dispose();
                onError(e);
            }
        }
    }

    private boolean isLoginExpired(int code) {
        return code==5;
    }

    @Override
    public void dispose() {
        if(!isDisposed()){
            DisposableHelper.dispose(this);
            onFail(new ApiException(CustomException.DISPOSED,"操作已取消"));
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
    public abstract void onSuccess(T data,int code,String msg);

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