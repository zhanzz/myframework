package com.example.reactnative.home.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;

import com.example.reactnative.packages.ScanPackage;
import com.facebook.react.ReactInstanceManager;
import com.facebook.react.ReactRootView;
import com.facebook.react.common.LifecycleState;
import com.facebook.react.modules.core.DefaultHardwareBackBtnHandler;
import com.facebook.react.shell.MainReactPackage;
import com.framework.common.BuildConfig;
import com.framework.common.base_mvp.BasePresenter;
import com.framework.common.base_mvp.BaseActivity;
import com.example.reactnative.home.presenter.ReactCategoryPresenter;
import com.example.reactnative.home.view.IReactCategoryView;
import com.example.reactnative.R;
import com.swmansion.gesturehandler.react.RNGestureHandlerEnabledRootView;
import com.swmansion.gesturehandler.react.RNGestureHandlerPackage;

public class RnMainActivity extends BaseActivity implements IReactCategoryView{//接口实现内部依赖会报无法访问错误
    private ReactCategoryPresenter mPresenter;
    private final int OVERLAY_PERMISSION_REQ_CODE = 1;  // 任写一个值
    private ReactRootView mReactRootView;
    private ReactInstanceManager mReactInstanceManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //用于开发中的报错显示在悬浮窗中
        if (BuildConfig.DEBUG_ENVIRONMENT && Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!Settings.canDrawOverlays(this)) {
                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                        Uri.parse("package:" + getPackageName()));
                startActivityForResult(intent, OVERLAY_PERMISSION_REQ_CODE);
            }
        }
        //mReactRootView = new ReactRootView(this);
        mReactRootView = new RNGestureHandlerEnabledRootView(this);
        mReactInstanceManager = ReactInstanceManager.builder()
                .setApplication(getApplication())
                .setCurrentActivity(this)
                .setBundleAssetName("index.android.bundle")
                .setJSMainModulePath("index")
                .addPackage(new MainReactPackage())
                .addPackage(new RNGestureHandlerPackage())
                .addPackage(new ScanPackage())
                .setUseDeveloperSupport(BuildConfig.DEBUG_ENVIRONMENT)
                .setInitialLifecycleState(LifecycleState.RESUMED)
                .build();
        // 注意这里的moduleName必须对应“index.js”中的
        // “AppRegistry.registerComponent()”的第一个参数
        mReactRootView.startReactApplication(mReactInstanceManager, "ExampleNavigation", null);

        setContentView(mReactRootView);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_react_category;
    }

    @Override
    public void bindData() {
    }

    @Override
    public void initEvent() {

    }

    @Override
    public BasePresenter getPresenter() {
        if (mPresenter == null) {
            mPresenter = new ReactCategoryPresenter();
        }
        return mPresenter;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == OVERLAY_PERMISSION_REQ_CODE) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (!Settings.canDrawOverlays(this)) {
                    // SYSTEM_ALERT_WINDOW permission not granted
                }
            }
        }
        mReactInstanceManager.onActivityResult( this, requestCode, resultCode, data );
    }



    @Override
    protected void onPause() {
        super.onPause();

        if (mReactInstanceManager != null) {
            mReactInstanceManager.onHostPause(this);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mReactInstanceManager != null) {
            mReactInstanceManager.onHostResume(this, new DefaultHardwareBackBtnHandler() {
                @Override
                public void invokeDefaultOnBackPressed() {
                    RnMainActivity.super.onBackPressed();
                }
            });
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (mReactInstanceManager != null) {
            mReactInstanceManager.onHostDestroy(this);
        }
        if (mReactRootView != null) {
            mReactRootView.unmountReactApplication();
            mReactRootView = null;
        }
    }

    @Override
    public void onBackPressed() {
        if (mReactInstanceManager != null) {
            mReactInstanceManager.onBackPressed();
        } else {
            super.onBackPressed();
        }
    }

    public static void start(Context context) {
        Intent starter = new Intent(context, RnMainActivity.class);
        context.startActivity(starter);
    }
}