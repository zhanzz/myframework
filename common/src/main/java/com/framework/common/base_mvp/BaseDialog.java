package com.framework.common.base_mvp;

import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import com.framework.common.R;
import com.framework.common.data.ActivityLifeCycleEvent;
import com.framework.common.utils.ListUtils;
import com.framework.common.utils.ToastUtil;
import com.framework.common.utils.UIHelper;

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
import io.reactivex.subjects.PublishSubject;

/**
 * @author zhangzhiqiang
 * @date 2019/4/23.
 * description：
 */
public abstract class BaseDialog extends DialogFragment implements IBaseView,IStopAdd {
    private Unbinder unbinder;
    public final PublishSubject<ActivityLifeCycleEvent> lifecycleSubject = PublishSubject.create();
    private FrameLayout mContainer;
    private CompositeDisposable mCompositeDisposable;
    private List<Runnable> mReloadRequest;

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
        setStyle(STYLE_NO_TITLE,0);
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
        lp.dimAmount = 0.0f;//越大背景越黑
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
    public void hideErrorView() {
        if (initLoadingAndErrorView() != null) {
            mLoadingAndErrorView.hideError();
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
        if(mReloadRequest!=null){
            mReloadRequest.clear();
            mReloadRequest=null;
        }
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
                    loadPageData();
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
    public void addReloadRequest(Runnable runnable){
        if(mContainer!=null){
            mContainer.postDelayed(new Runnable() {
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

    //@android:style/Theme.Dialog 会导致ProgressBar变粗
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        return new CloseSoftKeyDialog(requireContext(),R.style.dialog_style);
    }

    /**
     * 更安全的show
     *
     * @param manager
     */
    public void show(@NonNull FragmentManager manager) {
        if (isAdded()) {
            return;
        }
        try {
            //在每个add事务前增加一个remove事务，防止连续的add
            manager.beginTransaction().remove(this).commit();
            super.show(manager, this.getClass().getSimpleName());
        } catch (Exception e) {
            //同一实例使用不同的tag会异常,这里捕获一下
            e.printStackTrace();
        }
    }

    @Override
    public void show(@NonNull FragmentManager manager, @Nullable String tag) {
        if (isAdded()) {
            return;
        }
        //不能使用不同的tag
        if (tag != null) {
            if (getTag()!= null && !tag.equals(getTag())) {
                showToast("同一fragment实例不能使用不同的tag");
                return;
            }
        }
        try {
            //在每个add事务前增加一个remove事务，防止连续的add
            manager.beginTransaction().remove(this).commit();
            super.show(manager, tag);
        } catch (Exception e) {
            //同一实例使用不同的tag会异常,这里捕获一下
            e.printStackTrace();
        }
    }
}
