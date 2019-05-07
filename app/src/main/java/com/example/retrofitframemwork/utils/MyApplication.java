package com.example.retrofitframemwork.utils;

import com.example.retrofitframemwork.interceptor.RequestInterceptor;
import com.framework.common.BaseApplication;
import com.framework.common.manager.NetWorkManager;

/**
 * @author zhangzhiqiang
 * @date 2019/4/19.
 * description：
 */
public class MyApplication extends BaseApplication {
    @Override
    public void init() {
        NetWorkManager.getInstance().init(new RequestInterceptor());
    }
}
