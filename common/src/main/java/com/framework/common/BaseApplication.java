package com.framework.common;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.content.pm.SigningInfo;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkRequest;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.multidex.MultiDex;

import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import com.facebook.common.logging.FLog;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.imagepipeline.backends.okhttp3.OkHttpImagePipelineConfigFactory;
import com.facebook.imagepipeline.core.ImagePipelineConfig;
import com.facebook.imagepipeline.listener.RequestListener;
import com.facebook.imagepipeline.listener.RequestLoggingListener;
import com.framework.common.loading_view.CustomRefreshHeader;
import com.framework.common.manager.FrescoMemoryManager;
import com.framework.common.manager.LocalBroadcastActionManager;
import com.framework.common.manager.NetWorkManager;
import com.framework.common.receiver.INetChange;
import com.framework.common.receiver.NetChangeReceiver;
import com.framework.common.callBack.EmptyActivityLifecycleCallbacks;
import com.framework.common.utils.ListUtils;
import com.framework.common.utils.LogUtil;
import com.framework.common.utils.Platform;
import com.framework.common.utils.ToastUtil;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.DefaultRefreshFooterCreator;
import com.scwang.smartrefresh.layout.api.DefaultRefreshHeaderCreator;
import com.scwang.smartrefresh.layout.api.RefreshFooter;
import com.scwang.smartrefresh.layout.api.RefreshHeader;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.tencent.smtt.sdk.QbSdk;

import org.greenrobot.eventbus.EventBus;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
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

    public static BaseApplication getApp() {
        return mInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        //避免多进程多次初始化应用
        if (TextUtils.equals(getProcessName(this), getPackageName())) {
            init();
            //设置全局的Header构建器
            SmartRefreshLayout.setDefaultRefreshHeaderCreator(new DefaultRefreshHeaderCreator() {
                @Override
                public RefreshHeader createRefreshHeader(Context context, RefreshLayout layout) {
                    return new ClassicsHeader(context);
                }
            });
            //设置全局的Footer构建器
            SmartRefreshLayout.setDefaultRefreshFooterCreator(new DefaultRefreshFooterCreator() {
                @Override
                public RefreshFooter createRefreshFooter(Context context, RefreshLayout layout) {
                    //指定为经典Footer，默认是 BallPulseFooter
                    layout.setEnableLoadMore(false);
                    return new ClassicsFooter(context).setDrawableSize(20).setFinishDuration(300);
                }
            });
            initFresco();
            initX5WebView();
            //配置eventbus 只在DEBUG模式下，抛出异常，便于自测，同时又不会导致release环境的app崩溃
            if (BuildConfig.DEBUG_ENVIRONMENT) {
                EventBus.builder().throwSubscriberException(true).installDefaultEventBus();
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                ConnectivityManager manager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
                if(manager!=null){
                    manager.registerDefaultNetworkCallback(new ConnectivityManager.NetworkCallback() {
                        @Override
                        public void onLost(Network network) {//在子线程
                            onNetWorkChange(false);
                        }

                        @Override
                        public void onAvailable(Network network) {//在子线程
                            onNetWorkChange(true);
                        }
                    });
                }
            }else{
                NetChangeReceiver receiver = new NetChangeReceiver();
                IntentFilter intentFilter = new IntentFilter();
                intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
                registerReceiver(receiver, intentFilter);
            }
            registerActivityLifecycleCallbacks(new MyLifeCallBack());
        }
    }

    private void onNetWorkChange(boolean hasNet) {
        Intent intent = new Intent(LocalBroadcastActionManager.ACTION_NETWORK_CHANGE);
        intent.putExtra("hasNet",hasNet);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    private void initFresco() {
        Set<RequestListener> requestListeners = new HashSet<>();
        requestListeners.add(new RequestLoggingListener());
        ImagePipelineConfig.Builder builder1 = OkHttpImagePipelineConfigFactory
                .newBuilder(this, NetWorkManager.getOkHttpClient())
                .setDownsampleEnabled(true)//向下采样
                .setMemoryTrimmableRegistry(FrescoMemoryManager.getInstance());
        if (BuildConfig.DEBUG_ENVIRONMENT) {
            builder1.setRequestListeners(requestListeners);
            FLog.setMinimumLoggingLevel(FLog.VERBOSE);
        }
        ImagePipelineConfig config = builder1.build();
        Fresco.initialize(this, config);
    }

    private void initX5WebView(){
        QbSdk.PreInitCallback cb = new QbSdk.PreInitCallback() {
            @Override
            public void onViewInitFinished(boolean arg0) {
                // TODO Auto-generated method stub
                //x5內核初始化完成的回调，为true表示x5内核加载成功，否则表示x5内核加载失败，会自动切换到系统内核。
                Log.d("app", " onViewInitFinished is " + arg0);
            }

            @Override
            public void onCoreInitFinished() {
                // TODO Auto-generated method stub
            }
        };
        //x5内核初始化接口
        QbSdk.initX5Environment(getApplicationContext(),  cb);
    }

    public void init() {
        //参数可以填"MD5"、"SHA1"、"SHA256"
        checkSign("MD5");
    }

    public static String getProcessName(Context cxt) {
        int pid = android.os.Process.myPid();
        ActivityManager am = (ActivityManager) cxt.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> runningApps = am.getRunningAppProcesses();
        if (ListUtils.isEmpty(runningApps)) {
            return cxt.getPackageName();
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

    private void checkSign(String signType) {
        List<String> shas = new ArrayList<>();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            PackageInfo pkgInfo;
            try {
                pkgInfo = getPackageManager().getPackageInfo(getPackageName(), PackageManager.GET_SIGNING_CERTIFICATES);
                SigningInfo signingInfo = pkgInfo.signingInfo;
                for (Signature signature : signingInfo.getApkContentsSigners()) {
                    String sha = getSignValidString(signature.toByteArray(),signType);
                    shas.add(sha);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }else {
            try {
                PackageInfo pkgInfo = getPackageManager().getPackageInfo(getPackageName(), PackageManager.GET_SIGNATURES);
                Signature[] signatures = pkgInfo.signatures;
                for (Signature signature : signatures) {
                    String sha = getSignValidString(signature.toByteArray(),signType);
                    shas.add(sha);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (!ListUtils.isEmpty(shas)) {
            LogUtil.e("sign",shas.toString());
            if (!shas.contains("kRZ4/I7w9OYdkQvsyU6SDnkNXzw=")) { //不包含我们的签名，则退出应用

            }
        }
    }

    /**
     *
     * @param paramArrayOfByte
     * @param signType  //参数可以填"MD5"、"SHA1"、"SHA256"
     * @return
     * @throws NoSuchAlgorithmException
     */
    private static String getSignValidString(byte[] paramArrayOfByte, String signType) throws NoSuchAlgorithmException {
        MessageDigest localMessageDigest = MessageDigest.getInstance(signType);
        localMessageDigest.update(paramArrayOfByte);
        return bytesToHex(localMessageDigest.digest());  //转为16进制显示，实现略去
    }

    /**
     * 字节数组转16进制
     * @param bytes 需要转换的byte数组
     * @return  转换后的Hex字符串
     */
    public static String bytesToHex(byte[] bytes) {
        StringBuffer sb = new StringBuffer();
        for(int i = 0; i < bytes.length; i++) {
            String hex = Integer.toHexString(bytes[i] & 0xFF);
            if(hex.length() < 2){
                sb.append(0);
            }
            sb.append(hex);
        }
        return sb.toString();
    }
}
