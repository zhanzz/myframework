package com.framework.common.base_mvp;

import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.framework.common.data.ActivityLifeCycleEvent;
import com.framework.common.utils.ListUtils;
import com.framework.common.utils.ToastUtil;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Predicate;
import io.reactivex.subjects.BehaviorSubject;

/**
 * @author zhangzhiqiang
 * @date 2019/9/11.
 * description：
 */
public abstract class BaseViewGroup extends FrameLayout implements IBaseView, IStopAdd, View.OnClickListener {
    private Unbinder unbinder;
    public final BehaviorSubject<ActivityLifeCycleEvent> lifecycleSubject = BehaviorSubject.create();
    private CompositeDisposable mCompositeDisposable;
    private List<Runnable> mReloadRequest;

    public BaseViewGroup(@NonNull Context context) {
        super(context);
        init();
    }

    public BaseViewGroup(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public BaseViewGroup(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public BaseViewGroup(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        inflate(getContext(), getLayoutId(), this);
        unbinder = ButterKnife.bind(this, this);
        lifecycleSubject.onNext(ActivityLifeCycleEvent.CREATE);
        getPresenter();//初始化presenter
        initEvent();
    }

    @Override
    protected void onDetachedFromWindow() {
        lifecycleSubject.onNext(ActivityLifeCycleEvent.DESTROY);
        if (mCompositeDisposable != null) {
            mCompositeDisposable.dispose();
        }
        BasePresenter presenter = getPresenter();
        if (presenter != null) {
            presenter.detachView();
        }
        if (mReloadRequest != null) {
            mReloadRequest.clear();
            mReloadRequest = null;
        }
        //unbinder.unbind();
        super.onDetachedFromWindow();
    }

    @Override
    protected void onAttachedToWindow() {
        lifecycleSubject.onNext(ActivityLifeCycleEvent.RESUME);
        resetCompositeDisposable();
        BasePresenter presenter = getPresenter();
        if (presenter != null) {
            presenter.attachView(this);
        }
        bindData();
        super.onAttachedToWindow();
    }

    private void resetCompositeDisposable() {
        if (mCompositeDisposable != null) {
            mCompositeDisposable.dispose();
            mCompositeDisposable = null;
        }
    }

    public abstract int getLayoutId();

    public abstract void bindData();

    public abstract void initEvent();

    protected abstract BasePresenter getPresenter();

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

    @Override
    public void hideErrorView() {
        if (initLoadingAndErrorView() != null) {
            mLoadingAndErrorView.hideError();
        }
    }

    /**
     * 当点击错误页面重试时会调用此方法
     */
    public void loadPageData() {
        if (!ListUtils.isEmpty(mReloadRequest)) {
            Iterator<Runnable> it = mReloadRequest.iterator();
            while (it.hasNext()) {
                Runnable runnable = it.next();
                runnable.run();
                it.remove();
            }
        }
    }

    private LoadingAndErrorView mLoadingAndErrorView;

    public LoadingAndErrorView initLoadingAndErrorView() {
        if (mLoadingAndErrorView == null && getContext() != null) {
            mLoadingAndErrorView = new LoadingAndErrorView(getContext());
            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            addView(mLoadingAndErrorView, params);
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
    public <T> ObservableTransformer<T, T> bindUntilEvent(@NonNull final ActivityLifeCycleEvent event) {
        return new ObservableTransformer<T, T>() {
            @Override
            public ObservableSource<T> apply(Observable<T> sourceObservable) {
                Observable<ActivityLifeCycleEvent> compareLifecycleObservable =
                        lifecycleSubject.filter(new Predicate<ActivityLifeCycleEvent>() {
                            @Override
                            public boolean test(ActivityLifeCycleEvent activityEvent) {
                                return activityEvent.equals(event);
                            }
                        });
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

    @Override
    public void onClick(View v) {
    }

    @Override
    public void addReloadRequest(Runnable runnable) {
        postDelayed(new Runnable() {
            @Override
            public void run() {
                if (mReloadRequest == null) {
                    mReloadRequest = new ArrayList<>();
                }
                mReloadRequest.add(runnable);
                showErrorView();
            }
        }, 16);
    }

}
