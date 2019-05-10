package com.framework.common.base_mvp;

import com.framework.common.data.EventMessage;
import com.framework.common.manager.EventBusUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.lang.ref.WeakReference;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
/**
 * @author zhangzhiqiang
 * @date 2019/4/17.
 * description：
 */
public class BasePresenter<T extends IBaseView> implements Presenter<T>{
    private WeakReference<T> mvpView;
    protected T proxyView;

    @SuppressWarnings("unchecked")
    @Override
    public void attachView(T view) {
        this.mvpView = new WeakReference<T>(view);
        MvpViewInvocationHandler invocationHandler = new MvpViewInvocationHandler(this.mvpView.get());
        // 在这里采用动态代理
        proxyView = (T) Proxy.newProxyInstance(
                view.getClass().getClassLoader(), view.getClass()
                        .getInterfaces(), invocationHandler);
        if(isRegisterEventBus()){
            EventBusUtils.register(this);
        }
    }

    @Override
    public void detachView() {
        if(isRegisterEventBus()){
            EventBusUtils.unregister(this);
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

        private IBaseView mvpView;

        public MvpViewInvocationHandler(IBaseView mvpView) {
            this.mvpView = mvpView;
        }

        @Override
        public Object invoke(Object arg0, Method method, Object[] arg2) throws Throwable {
            if (isViewAttached()) {
                return method.invoke(mvpView, arg2);
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
