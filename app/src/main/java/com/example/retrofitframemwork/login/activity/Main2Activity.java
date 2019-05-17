package com.example.retrofitframemwork.login.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Looper;
import android.support.constraint.ConstraintLayout;
import android.support.transition.AutoTransition;
import android.support.transition.TransitionManager;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;
import android.widget.TextView;

import com.example.retrofitframemwork.R;
import com.example.retrofitframemwork.login.adapter.TestAdapter;
import com.example.retrofitframemwork.login.presenter.TestPresenter;
import com.example.retrofitframemwork.login.view.ITestView;
import com.example.retrofitframemwork.utils.Events;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.request.BasePostprocessor;
import com.framework.common.adapter.BaseAdapter;
import com.framework.common.adapter.HeaderAndFooterWrapper;
import com.framework.common.base_mvp.BaseActivity;
import com.framework.common.base_mvp.BasePresenter;
import com.framework.common.manager.EventBusUtils;
import com.framework.common.utils.FrescoUtils;
import com.framework.common.utils.LogUtil;
import com.framework.common.utils.UIHelper;
import com.framework.common.utils.ViewUtil;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class Main2Activity extends BaseActivity implements ITestView {
    @BindView(R.id.constraintlayout)
    ConstraintLayout constraintLayout;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    TestAdapter mAdapter;
    @BindView(R.id.llSearch)
    TextView llSearch;
    @BindView(R.id.simpleDraweeView)
    SimpleDraweeView simpleDraweeView;
    private TestPresenter mPresenter;
    HeaderAndFooterWrapper mHeaderAndFooterAdapter;

    @Override
    public int getLayoutId() {
        return R.layout.activity_main2;
    }

    @Override
    public boolean isFitSystemBar() {
        return true;
    }

    @Override
    public void bindData() {
        //refreshLayout.setFitsSystemWindows(true);
        //mAdapter.setNewData(data);
        refreshLayout.addOnAttachStateChangeListener(new View.OnAttachStateChangeListener() {
            @Override
            public void onViewAttachedToWindow(View v) {
                refreshLayout.getRefreshHeader().getView().setPadding(0, UIHelper.getStatusHeight(), 0, 0);
            }

            @Override
            public void onViewDetachedFromWindow(View v) {

            }
        });
        //refreshLayout.setHeaderInsetStart(UIHelper.getStatusHeight());
        refreshLayout.setVisibility(View.INVISIBLE);
        mAdapter = new TestAdapter(recyclerView);
        mHeaderAndFooterAdapter = new HeaderAndFooterWrapper(mAdapter);
        mAdapter.setSmartRefreshLayout(refreshLayout);
        mAdapter.setRealControlMoreEnable(false);
        mHeaderAndFooterAdapter.setLoadEndText("暂无更多数据");
        mHeaderAndFooterAdapter.setEmptyView(ViewUtil.createEmptyView(this));
        List<String> data = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            data.add("string" + i);
        }
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(mAdapter);
        mPresenter.refreshData(true);
    }

    @Override
    public void initEvent() {
        mAdapter.setEnableLoadMore(false);
        mAdapter.setRefreshListener(new BaseAdapter.RefreshListener() {
            @Override
            public void onRefresh() {
                EventBusUtils.post(new Events.LoginOut());
                mPresenter.refreshData(false);
            }

            @Override
            public void onLoadMore() {
                mPresenter.loadMoreData();
            }
        });
    }

    @Override
    public void reloadData() {
        mPresenter.refreshData(true);
    }

    @Override
    protected BasePresenter getPresenter() {
        if (mPresenter == null) {
            mPresenter = new TestPresenter();
        }
        return mPresenter;
    }

    public static void start(Context context) {
        Intent starter = new Intent(context, Main2Activity.class);
        context.startActivity(starter);
    }

    @Override
    public void onPageData(List<String> data, int currentPage) {
        refreshLayout.setVisibility(View.VISIBLE);
        mAdapter.setOrAddData(data, currentPage, mPresenter.PAGE_SIZE);
        //mHeaderAndFooterAdapter.loadComplete(data,mPresenter.PAGE_SIZE);
    }

    @Override
    public void onLoadFail() {
        mAdapter.loadMoreFail();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }


    private void reduce() {
        //设置收缩状态时的布局
        ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) llSearch.getLayoutParams();
        layoutParams.width = UIHelper.dip2px(200);
        layoutParams.setMargins(UIHelper.dip2px(80), UIHelper.dip2px(10),
                UIHelper.dip2px(10), UIHelper.dip2px(10));
        llSearch.setLayoutParams(layoutParams);
        //开始动画
        beginDelayedTransition(constraintLayout);
    }

    /**
     * 开始动画
     */
    private void beginDelayedTransition(ViewGroup view) {
        AutoTransition autoTransition = new AutoTransition();
        autoTransition.setDuration(300);
        TransitionManager.beginDelayedTransition(view, autoTransition);
    }

    @OnClick(R.id.llSearch)
    public void onClick() {
        //reduce();
        loadBitMap();
    }

    private void loadBitMap() {
        FrescoUtils.showThumb("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1557985668377&di=de105d36589518d06bb621f76b851988&imgtype=0&src=http%3A%2F%2Fimg5.duitang.com%2Fuploads%2Fitem%2F201409%2F21%2F20140921190342_jdhBY.thumb.700_0.jpeg"
                , simpleDraweeView, new BasePostprocessor() {
                    @Override
                    public void process(Bitmap bitmap) {
                        Palette p = Palette.from(bitmap)
                                .setRegion(0,0,bitmap.getWidth(),bitmap.getHeight()/20)
                                .generate();
                        Palette.Swatch color = p.getDominantSwatch();
                        LogUtil.e("color=" + color);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (color == null) {
                                    llSearch.setBackgroundColor(Color.RED);
                                } else {
                                    llSearch.setBackgroundColor(color.getRgb());
                                }
                            }
                        });
                    }
                });
    }

    public boolean isMainThread() {
        return Looper.getMainLooper() == Looper.myLooper();
    }
}
