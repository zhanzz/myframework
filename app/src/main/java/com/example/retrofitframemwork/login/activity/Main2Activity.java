package com.example.retrofitframemwork.login.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.retrofitframemwork.R;
import com.example.retrofitframemwork.login.adapter.TestAdapter;
import com.example.retrofitframemwork.login.presenter.TestPresenter;
import com.example.retrofitframemwork.login.view.ITestView;
import com.example.retrofitframemwork.utils.Events;
import com.framework.common.adapter.HeaderAndFooterWrapper;
import com.framework.common.base_mvp.BaseActivity;
import com.framework.common.base_mvp.BaseAdapter;
import com.framework.common.base_mvp.BasePresenter;
import com.framework.common.manager.EventBusUtils;
import com.framework.common.utils.LogUtil;
import com.framework.common.utils.UIHelper;
import com.framework.common.utils.ViewUtil;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class Main2Activity extends BaseActivity implements ITestView {

    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    TestAdapter mAdapter;
    @BindView(R.id.tv_test)
    TextView tvTest;
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
        mHeaderAndFooterAdapter.setSmartRefreshLayout(refreshLayout);
        mHeaderAndFooterAdapter.setLoadEndText("暂无更多数据");
        mHeaderAndFooterAdapter.setEmptyView(ViewUtil.createEmptyView(this));
        List<String> data = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            data.add("string" + i);
        }
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(mHeaderAndFooterAdapter);
        mPresenter.refreshData(true);
    }

    @Override
    public void initEvent() {
        mHeaderAndFooterAdapter.setRefreshListener(new BaseAdapter.RefreshListener() {
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
        mAdapter.setDatas(data, currentPage);
        mHeaderAndFooterAdapter.loadComplete(data,mPresenter.PAGE_SIZE);
    }

    @Override
    public void onLoadFail() {
        mHeaderAndFooterAdapter.loadMoreFail();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LogUtil.e("onDestroy");
    }
}
