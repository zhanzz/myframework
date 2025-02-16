package com.framework.common.base_mvp;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.viewbinding.ViewBinding;

import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.framework.common.R;
import com.framework.common.data.ActivityLifeCycleEvent;
import com.framework.common.manager.PermissionManager;
import com.framework.common.receiver.INetChange;
import com.framework.common.utils.AppTools;
import com.framework.common.utils.DeviceUtils;
import com.framework.common.utils.ListUtils;
import com.framework.common.utils.LogUtil;
import com.framework.common.utils.ToastUtil;
import com.framework.common.utils.UIHelper;
import com.framework.common.widget.TitleView;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import javax.security.auth.login.LoginException;

import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Predicate;
import io.reactivex.subjects.BehaviorSubject;

public abstract class BaseActivity extends AppCompatActivity implements IBaseView,IStopAdd,INetChange{
    protected List<BasePresenter> mPresenters;
    private CompositeDisposable mCompositeDisposable;
    private final BehaviorSubject<ActivityLifeCycleEvent> lifecycleSubject = BehaviorSubject.create();
    private View mPartErrorView;
    private @Nullable Toolbar mToolbar;
    private List<Runnable> mReloadRequest;
    @SuppressWarnings("unchecked")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        lifecycleSubject.onNext(ActivityLifeCycleEvent.CREATE);
        if(isUseBinding()){
            setContentView(getViewBinding().getRoot());
        }else {
            setContentView(getLayoutId());
        }
        ButterKnife.bind(this);
        initSystemBar();
        BasePresenter presenter= getPresenter();
        if(presenter!=null){
            presenter.attachView(this);
        }
        addPresenters();
        if(!ListUtils.isEmpty(mPresenters)){
            for(BasePresenter apresenter:mPresenters){
                if(apresenter!=null){
                    apresenter.attachView(this);
                }
            }
        }
        Intent intent = getIntent();
        if(intent!=null){
            getParamData(intent);
        }
        bindData();
        initEvent();

        mToolbar = findViewById(R.id.toolbar);
        if(mToolbar!=null){
            mToolbar.setTitle("");
            setSupportActionBar(mToolbar);
            mToolbar.setNavigationOnClickListener(v -> onBackPressed());
        }
    }

    protected boolean isUseBinding(){
        return false;
    }

    private void initSystemBar() {
        Window window = getWindow();
        if(isFitSystemBar()){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE|View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.setStatusBarColor(getSystemBarColor());
            }else if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {//大于等于4.4
                window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            }
            /**
             * 可不要半透明效果
             * window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
             * getWindow().setStatusBarColor(Color.TRANSPARENT);
             */
        }else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {//大于等于5.0
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                window.setStatusBarColor(getSystemBarColor());// SDK21
            } else if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){//需要动态添加view来达到改变系统栏颜色
                window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                View mTopView = new View(this);
                int statusBarHeight = UIHelper.getStatusHeight();
                FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,statusBarHeight);
                params.gravity = Gravity.TOP;
                mTopView.setBackgroundColor(getSystemBarColor());
                ((ViewGroup)window.getDecorView()).addView(mTopView,params);
                ((ViewGroup.MarginLayoutParams) findViewById(android.R.id.content).getLayoutParams()).topMargin = statusBarHeight;
            }
        }
        setStatusDarkBar(AppTools.isLightColor(getSystemBarColor()));
    }

    public abstract int getLayoutId();

    public ViewBinding getViewBinding(){
        if(isUseBinding()){
            throw new IllegalArgumentException("必须返回Binding");
        }
        return null;
    }

    public void getParamData(Intent intent){

    }

    public abstract void bindData();

    public abstract void initEvent();

    @Override
    public void showLoading() {
        if(initLoadingAndErrorView()!=null){
            mLoadingAndErrorView.showLoading();
        }
    }

    @Override
    public void hideLoading() {
        if(initLoadingAndErrorView()!=null){
            mLoadingAndErrorView.hideLoading();
        }
    }

    @Override
    public void showLoadingDialog() {
        if(initLoadingAndErrorView()!=null){
            mLoadingAndErrorView.showLoadingDialog();
        }
    }

    @Override
    public void hideLoadingDialog() {
        if(initLoadingAndErrorView()!=null){
            mLoadingAndErrorView.hideLoadingDialog();
        }
    }

    @Override
    public void showErrorView() {
        if(initLoadingAndErrorView()!=null){
            mLoadingAndErrorView.showError();
        }
    }

    @Override
    public void hideErrorView() {
        if(initLoadingAndErrorView()!=null){
            mLoadingAndErrorView.hideError();
        }
    }

    @Nullable
    public Toolbar getToolbar() {
        return mToolbar;
    }

    @Override
    public void setTitle(CharSequence title) {
        super.setTitle(title);
        if(mToolbar!=null){
            TextView tv = mToolbar.findViewById(R.id.tv_title);
            if(tv!=null){
                tv.setText(title);
            }
        }
    }

    /**
     * 当点击错误页面重试时会调用此方法
     */
    public void loadPageData() {
        if(!ListUtils.isEmpty(mReloadRequest)){
            Iterator<Runnable> it = mReloadRequest.iterator();
            while(it.hasNext()){
                Runnable runnable = it.next();
                runnable.run();
                it.remove();
            }
        }
    }

    @Override
    public void showToast(String msg) {
        ToastUtil.show(this,msg);
    }

    @Override
    public void showToast(String msg, int gravity, int duration) {
        ToastUtil.show(this,msg,gravity,duration);
    }

    /**
     * 感觉比较有框架约束力
     * @return
     */
    protected abstract BasePresenter getPresenter();

    protected void addPresenters(){}

    protected void addToPresenters(BasePresenter child) {
        if(mPresenters==null){
            mPresenters = new ArrayList<>();
        }
        if(child!=null){
            mPresenters.add(child);
        }
    }

    @Override
    public void finish() {
        AppTools.hideSoftKey(this);
        super.finish();
    }

    @Override
    protected void onDestroy() {
        lifecycleSubject.onNext(ActivityLifeCycleEvent.DESTROY);
        if(mCompositeDisposable!=null){
            mCompositeDisposable.dispose();
        }
        BasePresenter presenter= getPresenter();
        if(presenter!=null){
            presenter.detachView();
        }
        if(!ListUtils.isEmpty(mPresenters)){
            for(BasePresenter apresenter:mPresenters){
                if(apresenter!=null){
                    apresenter.detachView();
                }
            }
        }
        if(mReloadRequest!=null){
            mReloadRequest.clear();
            mReloadRequest=null;
        }
        super.onDestroy();
    }

    @NonNull
    public <T> ObservableTransformer<T, T> bindUntilEvent(@NonNull final ActivityLifeCycleEvent event) {
        return new ObservableTransformer<T, T>() {
            @Override
            public ObservableSource<T> apply(Observable<T> sourceObservable) {
                Observable<ActivityLifeCycleEvent> compareLifecycleObservable =
                        lifecycleSubject.filter(new Predicate<ActivityLifeCycleEvent>() {
                            @Override
                            public boolean test(ActivityLifeCycleEvent activityEvent){
                                return activityEvent.equals(event);
                            }
                        }).take(1);
                return sourceObservable.takeUntil(compareLifecycleObservable);
            }
        };
    }

    private LoadingAndErrorView mLoadingAndErrorView;

    public LoadingAndErrorView initLoadingAndErrorView(){
        if(mLoadingAndErrorView==null&&!isDestroy()){
            mLoadingAndErrorView = new LoadingAndErrorView(this);
            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT);
            params.topMargin = getResources().getDimensionPixelSize(R.dimen.title_bar_height);
            addContentView(mLoadingAndErrorView,params);
            mLoadingAndErrorView.setActionListener(new LoadingAndErrorView.ActionListener() {
                @Override
                public void clickRetry() {
                    loadPageData();
                }
            });
        }
        return mLoadingAndErrorView;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (null != this.getCurrentFocus()) {
            /**
             * 点击空白位置 隐藏软键盘
             */
            InputMethodManager mInputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            return mInputMethodManager.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), 0);
        }
        return super.onTouchEvent(event);
    }

    @Override
    public @NonNull Context getContext() {
        return this;
    }

    /**
     * 只有在获取时才会实例化，相当于懒加载
     * @return
     */
    @Override
    public @NonNull CompositeDisposable getCompositeDisposable() {
        if(mCompositeDisposable==null){
            mCompositeDisposable = new CompositeDisposable();
        }
        return mCompositeDisposable;
    }

    @Override
    public Boolean addDisposable(Disposable disposable) {
        getCompositeDisposable().add(disposable);
        return true;
    }

    @Override
    public void removeDisposable(Disposable disposable) {
        if(disposable!=null){
            getCompositeDisposable().remove(disposable);
        }
    }

    public boolean isDestroy(){
        return lifecycleSubject.getValue()==ActivityLifeCycleEvent.DESTROY;
    }

    /**
     * 内容是否延伸到系统状态栏
     * @return
     */
    protected boolean isFitSystemBar(){
        return false;
    }

    protected int getSystemBarColor(){
        return getResources().getColor(R.color.systemBarColor);
    }

    /**
     * @param isDark 状态栏文字是否深色
     */
    protected void setStatusDarkBar(boolean isDark) {
        //设置状态栏颜色为深色
        DeviceUtils.setStatusBarDarkMode(this, isDark);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Window window = getWindow();
            View view = window.getDecorView();
            int systemUiVisibility = view.getSystemUiVisibility();
            if (isDark) {
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                systemUiVisibility |=View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
                view.setSystemUiVisibility(systemUiVisibility);
            } else {
                systemUiVisibility &=~View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
                view.setSystemUiVisibility(systemUiVisibility);
            }
        }
    }

    public void requestNeedPermissions(int requestCode, String... permission) {
        if (AppTools.isMarshmallowOrHigher()) {
            if (ListUtils.isEmpty(permission)) {
                return;
            }
            if (PermissionManager.getInstance().lacksPermissions(permission)) {
                requestPermissions(permission, requestCode);
            } else {
                passPermission(Arrays.asList(permission),requestCode);
            }
        } else {
            passPermission(Arrays.asList(permission),requestCode);
        }
    }

    public void requestNeedPermissions(String... permission) {
        //默认code
        requestNeedPermissions(260,permission);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        List<String> passPermission = new ArrayList<>(),failPermission = new ArrayList<>();
        for (int i = 0; i < permissions.length; i++) {
            String permission = permissions[i];
            int grantResult = grantResults[i];
            if (PackageManager.PERMISSION_GRANTED == grantResult) {
                passPermission.add(permission);
            } else {
                failPermission.add(permission);
                PermissionManager.getInstance().toastTip(getContext(), permission);
                /**
                 * ActivityCompat.shouldShowRequestPermissionRationale
                 * 如果应用之前请求过此权限但用户拒绝了请求，此方法将返回 true。如果用户在过去拒绝了权限请求，并在权限请求
                 * 系统对话框中选择了 Don't ask again 选项，此方法将返回 false。如果设备规范禁止应用具有该权限，此方法也会返回 false。
                 */
                if (!ActivityCompat.shouldShowRequestPermissionRationale(this,permissions[i])){
                    Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    Uri uri = Uri.fromParts("package", getPackageName(), null);
                    intent.setData(uri);
                    startActivityForResult(intent,requestCode);
                }
            }
        }
        if(!ListUtils.isEmpty(passPermission)){
            passPermission(passPermission,requestCode);
        }
        if(!ListUtils.isEmpty(failPermission)){
            failPermission(failPermission,requestCode);
        }
    }

    public void failPermission(@NonNull List<String> permissions,int requestCode) {
    }

    public void passPermission(@NonNull List<String> permissions,int requestCode) {
    }

    @Override
    public void onNetChange(boolean isConnect) {
        if(isDestroy()){
           return;
        }
        if(isConnect){
            hideTopTipView();
        }else {
            showTopTipView();
        }
    }

    public void hideTopTipView() {
        if (mPartErrorView != null) {
            mPartErrorView.setVisibility(View.GONE);
        }
    }

    public void showTopTipView() {
        if (mPartErrorView != null) {
            mPartErrorView.bringToFront();
            mPartErrorView.setVisibility(View.VISIBLE);
        } else {
            initTopNetTip();
            if(mPartErrorView==null){
                return;
            }
            mPartErrorView.bringToFront();
            mPartErrorView.setVisibility(View.VISIBLE);
        }
    }

    private void initTopNetTip() {
        if (mPartErrorView != null||isDestroy()) {
            return;
        }
        mPartErrorView = View.inflate(this,R.layout.layout_top_net_error,null);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,UIHelper.dip2px(44));
        params.topMargin = getResources().getDimensionPixelSize(R.dimen.title_bar_height);
        addContentView(mPartErrorView,params);
        mPartErrorView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 跳转到系统的设置界面
                Intent intent = new Intent(Settings.ACTION_SETTINGS);
                startActivity(intent);
                mPartErrorView.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public void addReloadRequest(Runnable runnable){
        if(getWindow()!=null){
            getWindow().getDecorView().postDelayed(new Runnable() {
                @Override
                public void run() {
                    if(mReloadRequest == null){
                        mReloadRequest = new ArrayList<>();
                    }
                    mReloadRequest.add(runnable);
                    showErrorView();
                }
            },16);
        }
    }
}