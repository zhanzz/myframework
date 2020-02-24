package com.example.demo.pagelist.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
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
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

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
                    switch (result.getLoadingState()){
                        case Empty:
                            //初始化加载数据为空，设置recyclerView空视图
                            recyclerView.post(new Runnable() {
                                @Override
                                public void run() {
                                    //mAdapter.setEmptyView();
                                }
                            });
                            break;
                        case Loading:
                            showLoading();
                            break;
                        case Normal:
                            hideLoading();
                            hideErrorView();
                            break;
                        case Failed:
                            showErrorView();
                            break;

                    }
                    //更新下拉刷新的状态
                    if(result.getLoadingState()!=LoadingState.Loading){
                        smartRefreshLayout.finishRefresh(result.getLoadingState()==LoadingState.Normal);
                    }
                } else {
                    //recyclerView.post { adapter.setLoadingState(result.loadingState) }
                }
            }
        });
        mListViewModel.livePagedList.observe(this, productBeans -> mAdapter.submitList(productBeans));

        /*数据的获取过程--> ViewModel中LivePagedListBuilder的构建调用create方法-->创建ComputableLiveData中创建了一个LiveData用于监听活动
          状态--》当LiveData添加监听后当页面是活动状态时会调用onActive--》onActive中会在后台执行mRefreshRunnable-->执行compute
            -->创建DataSource生成PageList实例，实例的构造方法中调用dispatchLoadInitial->调用loadInitial开始同步加载数据后
            在主线程回调用PageList的监听mReceiver中的onPageResult将数据交由PageStorage再根据初始加载，加载下一页，加载上一页
            分别回调用PageList中的onInitialized、onPageAppended、onPagePrepended将通知在AsyncPagedListDiffer中PageList添加
            的mPagedListCallback再通知适配器数据变更，从而显示出来
        */
    }

    @Override
    public void loadPageData() {
        mListViewModel.refresh();
    }

    @Override
    public void initEvent() {
        smartRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                mListViewModel.refresh();
            }
        });
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