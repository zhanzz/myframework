package com.framework.common;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.text.TextUtils;

import com.facebook.common.logging.FLog;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.imagepipeline.backends.okhttp3.OkHttpImagePipelineConfigFactory;
import com.facebook.imagepipeline.core.ImagePipelineConfig;
import com.facebook.imagepipeline.listener.RequestListener;
import com.facebook.imagepipeline.listener.RequestLoggingListener;
import com.framework.common.loading_view.CustomRefreshHeader;
import com.framework.common.manager.FrescoMemoryManager;
import com.framework.common.manager.NetWorkManager;
import com.framework.common.receiver.NetChangeReceiver;
import com.framework.common.callBack.EmptyActivityLifecycleCallbacks;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.DefaultRefreshHeaderCreator;
import com.scwang.smartrefresh.layout.api.RefreshHeader;
import com.scwang.smartrefresh.layout.api.RefreshLayout;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author zhangzhiqiang
 * @date 2019/4/14.
 * description：
 */
public class BaseApplication extends Application {
    private static BaseApplication mInstance;
    public static BaseApplication getApp(){
        return mInstance;
    }
    //static 代码段可以防止内存泄露
    static {
        //设置全局的Header构建器
        SmartRefreshLayout.setDefaultRefreshHeaderCreator(new DefaultRefreshHeaderCreator() {
            @Override
            public RefreshHeader createRefreshHeader(Context context, RefreshLayout layout) {
                return new CustomRefreshHeader(context);
            }
        });
    }
    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        //避免多进程多次初始化应用
        if(TextUtils.equals(getProcessName(this),getPackageName())){
            init();
            initFresco();
            NetChangeReceiver receiver = new NetChangeReceiver();
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
            registerReceiver(receiver, intentFilter);
            registerActivityLifecycleCallbacks(new MyLifeCallBack());
        }
    }

    private void initFresco() {
        Set<RequestListener> requestListeners = new HashSet<>();
        requestListeners.add(new RequestLoggingListener());
        ImagePipelineConfig.Builder builder1 = OkHttpImagePipelineConfigFactory
                .newBuilder(this, NetWorkManager.getInstance().getOkHttpClient())
                .setDownsampleEnabled(true)//向下采样
                .setMemoryTrimmableRegistry(FrescoMemoryManager.getInstance());
        if(BuildConfig.DEBUG_ENVIRONMENT){
            builder1.setRequestListeners(requestListeners);
            FLog.setMinimumLoggingLevel(FLog.VERBOSE);
        }
        ImagePipelineConfig config = builder1.build();
        Fresco.initialize(this,config);
    }

    public void init(){

    }

    public static String getProcessName(Context cxt) {
        int pid = android.os.Process.myPid();
        ActivityManager am = (ActivityManager) cxt.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> runningApps = am.getRunningAppProcesses();
        if (runningApps == null) {
            return null;
        }
        for (ActivityManager.RunningAppProcessInfo procInfo : runningApps) {
            if (procInfo.pid == pid) {
                return procInfo.processName;
            }
        }
        return null;
    }

    private class MyLifeCallBack extends EmptyActivityLifecycleCallbacks {
        @Override
        public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
            com.framework.common.manager.ActivityManager.getInstance().addActivity(activity);
        }

        @Override
        public void onActivityDestroyed(Activity activity) {
            com.framework.common.manager.ActivityManager.getInstance().removeActivity(activity);
        }
    }
}
