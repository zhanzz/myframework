package com.example.demo;

import com.framework.common.exception.ApiException;
import com.framework.common.retrofit.ApiSubscriber;

/**
 * @author zhangzhiqiang
 * @date 2019/11/28.
 * description：
 */
public class MyApiSubscriber<T> extends ApiSubscriber<T> {
    @Override
    protected void onFail(ApiException ex) {
        System.out.println("onFail="+ex.getDisplayMessage());
    }

    @Override
    public void onSuccess(T data, int code, String msg) {
        System.out.println("onSuccess="+msg);
    }
}
