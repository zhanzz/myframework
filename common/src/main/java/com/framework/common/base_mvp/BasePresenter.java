package com.framework.common.base_mvp;

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
    }

    @Override
    public void detachView() {
        if (this.mvpView != null) {
            this.mvpView.clear();
            this.mvpView = null;
        }
    }

    public T getMvpView(){
        return proxyView;
    }

    public boolean isViewAttached() {
        return this.mvpView != null && this.mvpView.get() != null;
    }

    public void checkViewAttached() {
        if (!isViewAttached()) throw new MvpViewNotAttachedException();
    }

    public static class MvpViewNotAttachedException extends RuntimeException {
        public MvpViewNotAttachedException() {
            super("Please call Presenter.attachView(MvpView) before" +
                    " requesting data to the Presenter");
        }
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
}
