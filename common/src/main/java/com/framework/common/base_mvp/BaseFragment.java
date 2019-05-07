package com.framework.common.base_mvp;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import com.framework.common.data.ActivityLifeCycleEvent;
import com.framework.common.utils.ToastUtil;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Predicate;
import io.reactivex.subjects.PublishSubject;
/**
 * @author zhangzhiqiang
 * @date 2019/4/23.
 * description：
 */
public abstract class BaseFragment extends Fragment implements IBaseView{
    private Unbinder unbinder;
    public final PublishSubject<ActivityLifeCycleEvent> lifecycleSubject = PublishSubject.create();
    private FrameLayout mContainer;
    private CompositeDisposable mCompositeDisposable;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(getLayoutId(),container,false);
        mContainer = new FrameLayout(getContext());
        mContainer.setLayoutParams(rootView.getLayoutParams());
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT);
        mContainer.addView(rootView,params);
        unbinder = ButterKnife.bind(this,mContainer);
        return mContainer;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        lifecycleSubject.onNext(ActivityLifeCycleEvent.CREATE);
        Bundle bundle = getArguments();
        if(bundle!=null){
            getParamData(bundle);
        }
        BasePresenter presenter= getPresenter();
        if(presenter!=null){
            presenter.attachView(this);
        }
        bindData();
        initEvent();
    }

    protected abstract BasePresenter getPresenter();

    public abstract int getLayoutId();

    public abstract void bindData();

    public abstract void initEvent();

    public void getParamData(Bundle bundle){}

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
    public void reloadData() {}

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
        ToastUtil.show(getContext(),msg);
    }

    @Override
    public void showToast(String msg, int gravity, int duration) {
        ToastUtil.show(getContext(),msg,gravity,duration);
    }

    @Override
    public void onDestroyView() {
        lifecycleSubject.onNext(ActivityLifeCycleEvent.DESTROY);
        if(mCompositeDisposable!=null){
            mCompositeDisposable.dispose();
        }
        BasePresenter presenter= getPresenter();
        if(presenter!=null){
            presenter.detachView();
        }
        unbinder.unbind();
        super.onDestroyView();
    }

    private LoadingAndErrorView mLoadingAndErrorView;

    public LoadingAndErrorView initLoadingAndErrorView(){
        if(mLoadingAndErrorView==null&&getContext()!=null){
            mLoadingAndErrorView = new LoadingAndErrorView(getContext());
            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT);
            //params.topMargin = getResources().getDimensionPixelSize(R.dimen.title_bar_height);
            mContainer.addView(mLoadingAndErrorView,params);
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
     * @return
     */
    @Override
    public CompositeDisposable getCompositeDisposable() {
        if(mCompositeDisposable==null){
            mCompositeDisposable = new CompositeDisposable();
        }
        return mCompositeDisposable;
    }
}
