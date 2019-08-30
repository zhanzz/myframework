package com.framework.common.base_mvp;

import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.framework.common.data.ActivityLifeCycleEvent;
import com.framework.common.manager.PermissionManager;
import com.framework.common.utils.AppTools;
import com.framework.common.utils.ListUtils;
import com.framework.common.utils.ToastUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Predicate;
import io.reactivex.subjects.PublishSubject;

/**
 * @author zhangzhiqiang
 * @date 2019/4/23.
 * description：
 */
public abstract class BaseFragment extends Fragment implements IBaseView, View.OnClickListener {
    private Unbinder unbinder;
    public final PublishSubject<ActivityLifeCycleEvent> lifecycleSubject = PublishSubject.create();
    private FrameLayout mContainer;
    private CompositeDisposable mCompositeDisposable;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(getLayoutId(), container, false);
        mContainer = new FrameLayout(getContext());
        if (rootView.getLayoutParams() != null) {
            mContainer.setLayoutParams(rootView.getLayoutParams());
        }
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        mContainer.addView(rootView, params);
        unbinder = ButterKnife.bind(this, mContainer);
        return mContainer;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        lifecycleSubject.onNext(ActivityLifeCycleEvent.CREATE);
        BasePresenter presenter = getPresenter();
        if (presenter != null) {
            presenter.attachView(this);
        }
        Bundle bundle = getArguments();
        if (bundle != null) {
            getParamData(bundle);
        }
        if (savedInstanceState != null) {
            getSavedInstanceState(savedInstanceState);
        }
        bindData();
        initEvent();
    }

    protected void getSavedInstanceState(Bundle savedInstanceState) {
    }

    public abstract int getLayoutId();

    public abstract void bindData();

    public abstract void initEvent();

    protected abstract BasePresenter getPresenter();

    public void getParamData(Bundle bundle) {
    }

    @Override
    public void showLoading() {
        if (initLoadingAndErrorView() != null) {
            mLoadingAndErrorView.showLoading();
        }
    }

    @Override
    public void hideLoading() {
        if (initLoadingAndErrorView() != null) {
            mLoadingAndErrorView.hideLoading();
        }
    }

    @Override
    public void showLoadingDialog() {
        if (initLoadingAndErrorView() != null) {
            mLoadingAndErrorView.showLoadingDialog();
        }
    }

    @Override
    public void hideLoadingDialog() {
        if (initLoadingAndErrorView() != null) {
            mLoadingAndErrorView.hideLoadingDialog();
        }
    }

    @Override
    public void showErrorView() {
        if (initLoadingAndErrorView() != null) {
            mLoadingAndErrorView.showError();
        }
    }

    /**
     * 当点击错误页面重试时会调用此方法
     */
    public void reloadData() {
    }

    @Override
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

    @Override
    public void showToast(String msg) {
        ToastUtil.show(getContext(), msg);
    }

    @Override
    public void showToast(String msg, int gravity, int duration) {
        ToastUtil.show(getContext(), msg, gravity, duration);
    }

    @Override
    public void onDestroyView() {
        lifecycleSubject.onNext(ActivityLifeCycleEvent.DESTROY);
        if (mCompositeDisposable != null) {
            mCompositeDisposable.dispose();
        }
        BasePresenter presenter = getPresenter();
        if (presenter != null) {
            presenter.detachView();
        }
        unbinder.unbind();
        super.onDestroyView();
    }

    private LoadingAndErrorView mLoadingAndErrorView;

    public LoadingAndErrorView initLoadingAndErrorView() {
        if (mLoadingAndErrorView == null && getContext() != null) {
            mLoadingAndErrorView = new LoadingAndErrorView(getContext());
            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            //params.topMargin = getResources().getDimensionPixelSize(R.dimen.title_bar_height);
            mContainer.addView(mLoadingAndErrorView, params);
            mLoadingAndErrorView.setActionListener(new LoadingAndErrorView.ActionListener() {
                @Override
                public void clickRetry() {
                    reloadData();
                }
            });
        }
        return mLoadingAndErrorView;
    }

    /**
     * 只有在获取时才会实例化，相当于懒加载
     *
     * @return
     */
    @Override
    public CompositeDisposable getCompositeDisposable() {
        if (mCompositeDisposable == null) {
            mCompositeDisposable = new CompositeDisposable();
        }
        return mCompositeDisposable;
    }

    @Nullable
    @Override
    public Boolean addCompositeDisposable(Disposable disposable) {
        getCompositeDisposable().add(disposable);
        return true;
    }

    @Override
    public void onClick(View v) {
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
        requestNeedPermissions(250,permission);
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
}
