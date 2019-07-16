package com.example.demo.vlayout.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.alibaba.android.vlayout.DelegateAdapter;
import com.alibaba.android.vlayout.LayoutHelper;
import com.alibaba.android.vlayout.VirtualLayoutManager;
import com.example.demo.R;
import com.example.demo.R2;
import com.example.demo.adapter.BannerAdapter;
import com.example.demo.adapter.BottomProductAdapter;
import com.example.demo.adapter.ColumAdapter;
import com.example.demo.adapter.Constants;
import com.example.demo.adapter.FloatAdapter;
import com.example.demo.adapter.MenuAdapter;
import com.example.demo.adapter.OnePlusMoreAdapter;
import com.example.demo.adapter.ScrollActAdapter;
import com.example.demo.adapter.ScrollTopAdapter;
import com.example.demo.adapter.StikyAdapter;
import com.example.demo.vlayout.presenter.StudyVlayoutPresenter;
import com.example.demo.vlayout.view.IStudyVlayoutView;
import com.framework.common.base_mvp.BaseActivity;
import com.framework.common.base_mvp.BasePresenter;
import com.framework.common.utils.ToastUtil;
import com.framework.model.demo.ActivityBean;
import com.framework.model.demo.ProductBean;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;

import java.util.LinkedList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class StudyVlayoutActivity extends BaseActivity implements IStudyVlayoutView {
    @BindView(R2.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R2.id.smartRefreshLayout)
    SmartRefreshLayout smartRefreshLayout;
    private StudyVlayoutPresenter mPresenter;
    DelegateAdapter delegateAdapter;
    RecyclerView.RecycledViewPool mScrollActViewPool;
    private BottomProductAdapter mBottomProductAdapter;
    @Override
    public int getLayoutId() {
        return R.layout.activity_study_vlayout;
    }

    @Override
    public void bindData() {
        mScrollActViewPool = new RecyclerView.RecycledViewPool();
        mScrollActViewPool.setMaxRecycledViews(0, 10);


        VirtualLayoutManager layoutManager = new VirtualLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        RecyclerView.RecycledViewPool viewPool = new RecyclerView.RecycledViewPool();
        viewPool.setMaxRecycledViews(Constants.TYPE_BOOTOM_PRODUCT, 10);
        recyclerView.setRecycledViewPool(viewPool);

        mBottomProductAdapter = new BottomProductAdapter();
        delegateAdapter = new DelegateAdapter(layoutManager, true);
        recyclerView.setAdapter(delegateAdapter);
        mPresenter.getHomeData(true);
    }

    @Override
    public void initEvent() {
        smartRefreshLayout.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                mPresenter.getMoreProducts();
            }

            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                mPresenter.getHomeData(false);
            }
        });
        smartRefreshLayout.setEnableLoadMore(false);
    }

    @Override
    public void reloadData() {
        mPresenter.getHomeData(true);
    }

    @Override
    public BasePresenter getPresenter() {
        if (mPresenter == null) {
            mPresenter = new StudyVlayoutPresenter();
        }
        return mPresenter;
    }


    @Override
    public void onHomeData(ActivityBean bean) {
        smartRefreshLayout.finishRefresh(true);
        List<ActivityBean.ActivityItemBean> list = bean.getFloorList();
        List<DelegateAdapter.Adapter> adapters = new LinkedList<>();
        adapters.add(new FloatAdapter());
        for(ActivityBean.ActivityItemBean activityItemBean:list){
            switch (activityItemBean.getTemplate()){
                case "banner":
                    adapters.add(new BannerAdapter(activityItemBean));
                    break;
                case "module":
                    adapters.add(new MenuAdapter(activityItemBean));
                    break;
                case "theme_l1_rt1_rb2":
                case "theme_l1_rt2_rb1":
                case "theme_lt2_lb2_r1":
                case "theme_l1_rt2_rb2":
                case "theme_lt2_lb1_r1":
                case "theme_lt1_lb2_r1":
                case "theme_l1_rt1_rb1":
                case "theme_lt1_lb1_r1":
                case "theme_1b1_l1_rt1_rb1":
                case "theme_1b1_l1_rt1_rb2":
                case "theme_1b4_lt1_lb1_rt1_rb1":
                    adapters.add(new OnePlusMoreAdapter(activityItemBean));
                    break;
                case "horizontal_act":
                case "act_title":
                    adapters.add(new ColumAdapter(activityItemBean));
                    break;
                case "scroll_act":
                    adapters.add(new ScrollActAdapter(mScrollActViewPool,activityItemBean));
                    break;
            }
        }
        smartRefreshLayout.setEnableLoadMore(true);
        adapters.add(mBottomProductAdapter);
        smartRefreshLayout.resetNoMoreData();
        adapters.add(12,new ScrollTopAdapter());
        adapters.add(13,new StikyAdapter());
        mPresenter.getMoreProducts();
        delegateAdapter.setAdapters(adapters);
        delegateAdapter.notifyDataSetChanged();
    }

    @Override
    public void onProductList(List<ProductBean> data, int mCurrentPage) {
        mBottomProductAdapter.addProduct(data,mCurrentPage);
        if(data.size()<mPresenter.PAGE_SIZE){
            smartRefreshLayout.finishLoadMoreWithNoMoreData();
        }else{
            smartRefreshLayout.finishLoadMore(true);
        }
    }

    @Override
    public void onProductFail(int mCurrentPage) {
        smartRefreshLayout.finishLoadMore(false);
    }

    public static void start(Context context) {
        Intent starter = new Intent(context, StudyVlayoutActivity.class);
        context.startActivity(starter);
    }
}