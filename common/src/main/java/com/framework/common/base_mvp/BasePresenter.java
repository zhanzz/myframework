package com.framework.common.base_mvp;

import com.framework.common.callBack.RxNetCallBack;
import com.framework.common.data.LoadType;
import com.framework.common.data.Result;
import com.framework.common.manager.EventBusUtils;
import com.framework.common.net.RxNet;

import java.lang.ref.WeakReference;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;

/**
 * @author zhangzhiqiang
 * @date 2019/4/17.
 * description：
 */
public class BasePresenter<T extends IBaseView> implements Presenter<T>{
    private WeakReference<T> mvpView;
    private T proxyView;
    private Map<Integer,Disposable> mRequestMap;
    @Override
    public void attachView(T view) {
        this.mvpView = new WeakReference<T>(view);
        MvpViewInvocationHandler invocationHandler = new MvpViewInvocationHandler();
        // 在这里采用动态代理
        proxyView = (T) Proxy.newProxyInstance(
                view.getClass().getClassLoader(), view.getClass()
                        .getInterfaces(), invocationHandler);
        if(isRegisterEventBus()){
            EventBusUtils.register(this);
        }
    }

    private <T> Disposable request(Observable<Result<T>> observable,LoadType loadType,RxNetCallBack<T> callBack){
        return RxNet.request(observable,getMvpView(),loadType,callBack);
    }

    private <T> Disposable requestNoRepeat(Observable<Result<T>> observable,int requestId,LoadType loadType,RxNetCallBack<T> callBack){
        if(mRequestMap==null){
            mRequestMap = new HashMap<>();
        }
        Disposable disposable = mRequestMap.get(requestId);
        if(disposable!=null){
            disposable.dispose();
        }
        disposable = RxNet.request(observable,getMvpView(),loadType,callBack);
        mRequestMap.put(requestId,disposable);
        return disposable;
    }

    @Override
    public void detachView() {
        if(isRegisterEventBus()){
            EventBusUtils.unregister(this);
        }
        if(mRequestMap!=null){
            mRequestMap.clear();
        }
        if (this.mvpView != null) {
            this.mvpView.clear();
            this.mvpView = null;
        }
    }

    protected T getMvpView(){
        return proxyView;
    }

    protected boolean isViewAttached() {
        return this.mvpView != null && this.mvpView.get() != null;
    }

    private class MvpViewInvocationHandler implements InvocationHandler {

        private MvpViewInvocationHandler() {}

        @Override
        public Object invoke(Object arg0, Method method, Object[] arg2) throws Throwable {
            if (isViewAttached()) {
                return method.invoke(mvpView.get(), arg2);
            }
            return null;
        }
    }

    /**
     * 是否需要注册EventBus
     * @return
     */
    protected boolean isRegisterEventBus(){
        return false;
    }
}
