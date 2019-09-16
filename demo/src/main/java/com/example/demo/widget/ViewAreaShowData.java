package com.example.demo.widget;

import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.example.demo.R;
import com.example.demo.R2;
import com.framework.common.base_mvp.BasePresenter;
import com.framework.common.base_mvp.BaseViewGroup;
import com.framework.common.data.ActivityLifeCycleEvent;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * @author zhangzhiqiang
 * @date 2019/9/11.
 * description：
 */
public class ViewAreaShowData extends BaseViewGroup implements IViewAreaShowData {
    ViewAreaShowDataPresenter mPresenter;
    @BindView(R2.id.tv_show_num)
    TextView tvShowNum;

    private int count;

    public ViewAreaShowData(@NonNull Context context) {
        super(context);
    }

    public ViewAreaShowData(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public ViewAreaShowData(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public ViewAreaShowData(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public int getLayoutId() {
        return R.layout.layout_view_area__show_data;
    }

    @Override
    public void bindData() {
        Observable.interval(3, 2, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.io())
                .compose(this.<Long>bindUntilEvent(ActivityLifeCycleEvent.DESTROY))
                .observeOn(AndroidSchedulers.mainThread()) // 由于interval默认在新线程，所以我们应该切回主线程
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(@io.reactivex.annotations.NonNull Long aLong) throws Exception {
                        count++;
                        tvShowNum.setText(String.valueOf(count));
                    }
                });
    }

    @Override
    public void initEvent() {

    }

    @Override
    protected BasePresenter getPresenter() {
        if (mPresenter == null) {
            mPresenter = new ViewAreaShowDataPresenter();
        }
        return mPresenter;
    }

    @Override
    public void showData() {

    }

    @OnClick({R2.id.btn_start, R2.id.btn_end})
    public void onClick(View view) {
        int i = view.getId();
        if (i == R.id.btn_start) {
            lifecycleSubject.onNext(ActivityLifeCycleEvent.RESUME);
            BasePresenter presenter = getPresenter();
            if (presenter != null) {
                presenter.attachView(this);
            }
            bindData();
        } else if (i == R.id.btn_end) {
            lifecycleSubject.onNext(ActivityLifeCycleEvent.DESTROY);
            BasePresenter presenter = getPresenter();
            if (presenter != null) {
                presenter.detachView();
            }
        }
    }
}
