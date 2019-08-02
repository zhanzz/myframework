package com.framework.common.base_mvp;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import com.framework.common.R;
import com.framework.common.data.ActivityLifeCycleEvent;
import com.framework.common.utils.ToastUtil;
import com.framework.common.utils.UIHelper;
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
public abstract class BaseDialog extends DialogFragment implements IBaseView {
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
        setDialogStyle();
        Bundle bundle = getArguments();
        if (bundle != null) {
            getParamData(bundle);
        }
        BasePresenter presenter = getPresenter();
        if (presenter != null) {
            presenter.attachView(this);
        }
        bindData();
        initEvent();
    }

    private void setDialogStyle() {
        Window window = getDialog().getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.gravity = Gravity.CENTER;
        lp.dimAmount = 0.3f;//越大背景越黑
        lp.width = UIHelper.getDisplayWidth() * 3 / 4;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.windowAnimations = R.style.DialogAnimation;
        setLayoutParams(lp);
        window.setAttributes(lp);
        window.setBackgroundDrawable(new ColorDrawable(0x00000000));
    }

    /**
     * 可重写些方法设置自定义的参数
     * @param params
     */
    protected void setLayoutParams(WindowManager.LayoutParams params) {

    }

    protected abstract BasePresenter getPresenter();

    public abstract int getLayoutId();

    public abstract void bindData();

    public abstract void initEvent();

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
    public void showErrorView() {
        if (initLoadingAndErrorView() != null) {
            mLoadingAndErrorView.showError();
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

    @Override
    public Boolean addCompositeDisposable(Disposable disposable) {
        getCompositeDisposable().add(disposable);
        return true;
    }
}
