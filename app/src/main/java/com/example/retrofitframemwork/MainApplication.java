package com.example.retrofitframemwork;

import com.alibaba.android.arouter.launcher.ARouter;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.Feature;
import com.example.demo.excption_handler.CrashHandler;
import com.example.retrofitframemwork.interceptor.RequestInterceptor;
import com.framework.common.BaseApplication;
import com.framework.common.BuildConfig;
import com.framework.common.manager.NetWorkManager;
//import com.tencent.bugly.crashreport.CrashReport;

/**
 * @author zhangzhiqiang
 * @date 2019/4/19.
 * description：
 */
public class MainApplication extends BaseApplication{
    @Override
    public void init() {
        super.init();
        //让String类型的值初始化为空字符串(对象中的其它初始化值将会失效)
        JSON.DEFAULT_PARSER_FEATURE|= Feature.InitStringFieldAsEmpty.getMask();
        NetWorkManager.getInstance().init(new RequestInterceptor());
        //CrashReport.initCrashReport(getApplicationContext(), "dc45778196", BuildConfig.DEBUG_ENVIRONMENT);//bugly
        CrashHandler.getInstance().init();
        if (BuildConfig.DEBUG_ENVIRONMENT) {           // 这两行必须写在init之前，否则这些配置在init过程中将无效
            ARouter.openLog();     // 打印日志
            ARouter.openDebug();   // 开启调试模式(如果在InstantRun模式下运行，必须开启调试模式！线上版本需要关闭,否则有安全风险)
        }
        ARouter.init(this); // 尽可能早，推荐在Application中初始化
    }
}
