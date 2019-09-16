package com.example.retrofitframemwork;

import com.alibaba.android.arouter.launcher.ARouter;
import com.example.demo.excption_handler.CrashHandler;
import com.example.retrofitframemwork.interceptor.RequestInterceptor;
import com.framework.common.BaseApplication;
import com.framework.common.BuildConfig;
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
        if (BuildConfig.DEBUG_ENVIRONMENT) {           // 这两行必须写在init之前，否则这些配置在init过程中将无效
            ARouter.openLog();     // 打印日志
            ARouter.openDebug();   // 开启调试模式(如果在InstantRun模式下运行，必须开启调试模式！线上版本需要关闭,否则有安全风险)
        }
        ARouter.init(this); // 尽可能早，推荐在Application中初始化
    }
}
