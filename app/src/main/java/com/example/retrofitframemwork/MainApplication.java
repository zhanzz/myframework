package com.example.retrofitframemwork;

import com.example.retrofitframemwork.interceptor.RequestInterceptor;
import com.facebook.react.ReactApplication;
import com.facebook.react.ReactNativeHost;
import com.facebook.react.ReactPackage;
import com.facebook.react.shell.MainReactPackage;
import com.facebook.soloader.SoLoader;
import com.framework.common.BaseApplication;
import com.framework.common.manager.NetWorkManager;
import com.swmansion.gesturehandler.react.RNGestureHandlerPackage;

import java.util.Arrays;
import java.util.List;

/**
 * @author zhangzhiqiang
 * @date 2019/4/19.
 * description：
 */
public class MainApplication extends BaseApplication implements ReactApplication {

    @Override
    public void onCreate() {
        super.onCreate();
        SoLoader.init(this, /* native exopackage */ false);
    }

    @Override
    public void init() {
        NetWorkManager.getInstance().init(new RequestInterceptor());
    }

    @Override
    public ReactNativeHost getReactNativeHost() {
        return mReactNativeHost;
    }

    private final ReactNativeHost mReactNativeHost = new ReactNativeHost(this) {
        @Override
        public boolean getUseDeveloperSupport() {
            return BuildConfig.DEBUG;
        }

        @Override
        protected List<ReactPackage> getPackages() {
            return Arrays.<ReactPackage>asList(
                    new MainReactPackage(),
                    new RNGestureHandlerPackage()
            );
        }

        @Override
        protected String getJSMainModuleName() {
            return "index";
        }
    };
}
