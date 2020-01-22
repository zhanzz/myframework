package com.example.demo.pagelist.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.RecyclerView;

import com.example.demo.R;
import com.example.demo.R2;
import com.example.demo.pagelist.LoadingState;
import com.example.demo.pagelist.ResultBean;
import com.example.demo.pagelist.presenter.PageListPresenter;
import com.example.demo.pagelist.view.IPageListView;
import com.framework.common.base_mvp.BaseActivity;
import com.framework.common.base_mvp.BasePresenter;
import com.framework.model.demo.ProductBean;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.example.demo.pagelist.ListViewModel;
import com.example.demo.pagelist.MyPageListAdapter;
import butterknife.BindView;
import butterknife.ButterKnife;

public class PageListActivity extends BaseActivity implements IPageListView {
    @BindView(R2.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R2.id.smartRefreshLayout)
    SmartRefreshLayout smartRefreshLayout;
    private PageListPresenter mPresenter;

    private ListViewModel mListViewModel;
    private MyPageListAdapter mAdapter = new MyPageListAdapter();
    @Override
    public int getLayoutId() {
        return R.layout.activity_page_list;
    }

    @Override
    public void bindData() {
        recyclerView.setAdapter(mAdapter);
        mListViewModel = ViewModelProviders.of(this).get(ListViewModel.class);
        mListViewModel.setMvp(this);
        mListViewModel.result.observe(this, result -> {
            if(result!=null) {
                if (result.isInit()) {
                    if (result.getLoadingState() == LoadingState.Empty) {
                        //初始化加载数据为空，设置recyclerView空视图
                        recyclerView.post(new Runnable() {
                            @Override
                            public void run() {
                                //mAdapter.setEmptyView();
                            }
                        });
                    }
                    //更新下拉刷新的状态
                    smartRefreshLayout.post(new Runnable() {
                        @Override
                        public void run() {
                            //smartRefreshLayout.isRefreshing = (result.loadingState == LoadingState.Loading)
                        }
                    });
                } else {
                    //recyclerView.post { adapter.setLoadingState(result.loadingState) }
                }
            }
        });
        mListViewModel.livePagedList.observe(this, productBeans -> mAdapter.submitList(productBeans));
    }

    @Override
    public void initEvent() {

    }

    @Override
    public BasePresenter getPresenter() {
        if (mPresenter == null) {
            mPresenter = new PageListPresenter();
        }
        return mPresenter;
    }

    public static void start(Context context) {
        Intent starter = new Intent(context, PageListActivity.class);
        context.startActivity(starter);
    }
}