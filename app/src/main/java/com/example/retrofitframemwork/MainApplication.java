package com.example.retrofitframemwork;

import com.example.demo.excption_handler.CrashHandler;
import com.example.retrofitframemwork.interceptor.RequestInterceptor;
import com.framework.common.BaseApplication;
import com.framework.common.manager.NetWorkManager;

/**
 * @author zhangzhiqiang
 * @date 2019/4/19.
 * description：
 */
public class MainApplication extends BaseApplication{
    @Override
    public void init() {
        NetWorkManager.getInstance().init(new RequestInterceptor());
        CrashHandler.getInstance().init();
    }
}
