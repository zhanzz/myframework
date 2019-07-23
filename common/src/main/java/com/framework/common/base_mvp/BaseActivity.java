package com.framework.common.base_mvp;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;
import com.framework.common.R;
import com.framework.common.data.ActivityLifeCycleEvent;
import com.framework.common.manager.PermissionManager;
import com.framework.common.receiver.INetChange;
import com.framework.common.utils.AppTools;
import com.framework.common.utils.DeviceUtils;
import com.framework.common.utils.ListUtils;
import com.framework.common.utils.ToastUtil;
import com.framework.common.utils.UIHelper;
import com.framework.common.widget.TitleView;
import java.util.ArrayList;
import java.util.List;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Predicate;
import io.reactivex.subjects.PublishSubject;

public abstract class BaseActivity extends AppCompatActivity implements IBaseView,INetChange{
    private static final int REQUEST_CODE = 1000;
    protected List<BasePresenter> mPresenters;
    private CompositeDisposable mCompositeDisposable;
    public final PublishSubject<ActivityLifeCycleEvent> lifecycleSubject = PublishSubject.create();
    private View mPartErrorView;

    @SuppressWarnings("unchecked")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        lifecycleSubject.onNext(ActivityLifeCycleEvent.CREATE);
        setContentView(getLayoutId());
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
        TitleView titleView = findViewById(R.id.title_bar);
        if (titleView!=null){
            titleView.getLeftBackTextTv().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onBackPressed();
                }
            });
        }
    }

    private void initSystemBar() {
        Window window = getWindow();
        if(isFitSystemBar()){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {//大于等于4.4
                window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            }
        }else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {//大于等于5.0
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                window.setStatusBarColor(getSystemBarColor());// SDK21
            } else {//需要动态添加view来达到改变系统栏颜色
                View mTopView = new View(this);
                int statusBarHeight = UIHelper.getStatusHeight();
                FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,statusBarHeight);
                params.gravity = Gravity.TOP;
                mTopView.setBackgroundColor(getSystemBarColor());
                ((ViewGroup)window.getDecorView()).addView(mTopView,params);
                ((ViewGroup.MarginLayoutParams) findViewById(android.R.id.content).getLayoutParams()).topMargin = statusBarHeight;
            }
            setStatusDarkBar(AppTools.isLightColor(getSystemBarColor()));
        }
    }

    public abstract int getLayoutId();

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

    /**
     * 当点击错误页面重试时会调用此方法
     */
    public void reloadData() {

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
        mPresenters.add(child);
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
                            public boolean test(ActivityLifeCycleEvent activityEvent) throws Exception {
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
                    reloadData();
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
    public Context getContext() {
        return this;
    }

    /**
     * 只有在获取时才会实例化，相当于懒加载
     * @return
     */
    @Override
    public CompositeDisposable getCompositeDisposable() {
        if(mCompositeDisposable==null){
            mCompositeDisposable = new CompositeDisposable();
        }
        return mCompositeDisposable;
    }

    public boolean isDestroy(){
        boolean isDestory = false;
        isDestory = isFinishing();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            isDestory = super.isDestroyed();
        }
        return isDestory;
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
    private void setStatusDarkBar(boolean isDark) {
        //设置状态栏颜色为深色
        DeviceUtils.setStatusBarDarkMode(this, isDark);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Window window = getWindow();
            if (isDark) {
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            } else {
                window.clearFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            }
        }
    }

    public void requestNeedPermissions(String ... permission) {
        if (AppTools.isMarshmallowOrHigher()) {
            if (ListUtils.isEmpty(permission)) {
                return;
            }
            if (PermissionManager.getInstance().lacksPermissions(permission)) {
                requestPermissions(permission, REQUEST_CODE);
            } else {
                for (String per : permission) {
                    passPermission(per);
                }
            }
        } else {
            for (String per : permission) {
                passPermission(per);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE) {
            for (int i = 0; i < permissions.length; i++) {
                String permission = permissions[i];
                int grantResult = grantResults[i];
                if (PackageManager.PERMISSION_GRANTED == grantResult) {
                    passPermission(permission);
                } else {
                    failPermission(permission);
                    PermissionManager.getInstance().toastTip(this, permission);
                }
            }
        }
    }

    public void failPermission(String permission){}

    public void passPermission(String permission){}

    @Override
    public void onNetChange(boolean isConnect) {
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
}