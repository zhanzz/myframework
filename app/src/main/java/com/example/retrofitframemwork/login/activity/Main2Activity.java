package com.example.retrofitframemwork.login.activity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.example.retrofitframemwork.R;
import com.example.retrofitframemwork.login.adapter.TestAdapter;
import com.example.retrofitframemwork.login.presenter.TestPresenter;
import com.example.retrofitframemwork.login.view.ITestView;
import com.framework.common.BaseApplication;
import com.framework.common.base_mvp.BaseActivity;
import com.framework.common.base_mvp.BaseAdapter;
import com.framework.common.base_mvp.BasePresenter;
import com.framework.common.utils.ViewUtil;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

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
    private TestPresenter mPresenter;

    @Override
    public int getLayoutId() {
        return R.layout.activity_main2;
    }

    @Override
    public void bindData() {
        refreshLayout.setVisibility(View.INVISIBLE);
        mAdapter = new TestAdapter(recyclerView);
        mAdapter.setSmartRefreshLayout(refreshLayout);
        mAdapter.setLoadEndText("暂无更多数据");
        mAdapter.setEmptyView(ViewUtil.createEmptyView(this));
        List<String> data = new ArrayList<>();
        for(int i=0;i<5;i++){
            data.add("string"+i);
        }
        //mAdapter.setNewData(data);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(mAdapter);
        mPresenter.refreshData(true);
    }

    @Override
    public void initEvent() {
        mAdapter.setRefreshListener(new BaseAdapter.RefreshListener() {
            @Override
            public void onRefresh() {
                mPresenter.refreshData(false);
            }

            @Override
            public void onLoadMore() {
                mPresenter.loadMoreData();
            }
        });
    }

    @Override
    protected BasePresenter getPresenter() {
        if(mPresenter==null){
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
        mAdapter.setOrAddData(data,currentPage,mPresenter.PAGE_SIZE);
    }

    @Override
    public void onLoadFail() {
        mAdapter.loadMoreFail();
    }
}
