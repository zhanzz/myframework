package com.example.demo.viewpager_fragment.fragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.example.demo.R;
import com.example.demo.R2;
import com.example.demo.viewpager_fragment.activity.BrowsePhotoActivity;
import com.example.demo.viewpager_fragment.adapter.TestSaveAdapter;
import com.example.demo.viewpager_fragment.presenter.PagerPresenter;
import com.example.demo.viewpager_fragment.view.IPagerView;
import com.facebook.drawee.view.SimpleDraweeView;
import com.framework.common.adapter.BaseAdapter;
import com.framework.common.base_mvp.BaseFragment;
import com.framework.common.base_mvp.BasePresenter;
import com.framework.common.utils.ListUtils;
import com.framework.common.utils.ViewUtil;
import com.framework.model.demo.PresellBean;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import butterknife.BindView;

public class PagerFragment extends BaseFragment implements IPagerView {
    @BindView(R2.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R2.id.smartRefreshLayout)
    SmartRefreshLayout smartRefreshLayout;
    private TestSaveAdapter mAdapter;
    private PagerPresenter mPresenter;

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("mData", (Serializable) mAdapter.getData());
        outState.putInt("pageIndex",mPresenter.getPageIndex());
    }

    @Override
    protected void getSavedInstanceState(Bundle savedInstanceState) {
        ArrayList<PresellBean.PresellProduct> data = (ArrayList<PresellBean.PresellProduct>) savedInstanceState.getSerializable("mData");
        mPresenter.setData(data);
        mPresenter.setPageIndex(savedInstanceState.getInt("pageIndex",1));
    }

    @Override
    protected BasePresenter getPresenter() {
        if(mPresenter==null){
            mPresenter = new PagerPresenter();
        }
        return mPresenter;
    }

    @Override
    public void getParamData(Bundle bundle) {
        mPresenter.setId(bundle.getString("id"));
        if(ListUtils.isEmpty(mPresenter.getData())){
            mPresenter.setData((ArrayList<PresellBean.PresellProduct>) bundle.getSerializable("productList"));
        }
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_pager;
    }

    @Override
    public void bindData() {
        mAdapter = new TestSaveAdapter(recyclerView);
        mAdapter.setLoadEndText("暂无更多数据");
        mAdapter.setEmptyView(ViewUtil.createEmptyView(getContext()));
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        if (ListUtils.isEmpty(mPresenter.getData())) {//数据为空发起网络请求
            mPresenter.refreshData(true);
        }else{
            mAdapter.setNewData(mPresenter.getData());
            recyclerView.setAdapter(mAdapter);
        }
    }

    @Override
    public void initEvent() {
        mAdapter.setSmartRefreshLayout(smartRefreshLayout);
        mAdapter.setRefreshListener(new BaseAdapter.RefreshListener() {
            @Override
            public void onRefresh() {
                mPresenter.refreshData(false);
            }

            @Override
            public void onLoadMore() {
                mPresenter.getMoreList();
            }
        });

        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                View iv = view.findViewById(R.id.itemIcon);
                if(iv instanceof SimpleDraweeView){
                    PresellBean.PresellProduct bean= mAdapter.getData().get(position);
                    ArrayList<String> list = new ArrayList();
                    list.add(bean.getProductAdsPic());
                    BrowsePhotoActivity.start(getActivity(),0,list,iv);
                }
            }
        });
    }

    @Override
    public void onProductList(List<PresellBean.PresellProduct> list, int pageIndex) {
        mAdapter.setOrAddData(list,pageIndex,mPresenter.PAGE_SIZE);
        if(recyclerView.getAdapter()==null){
            recyclerView.setAdapter(mAdapter);
        }
    }

    @Override
    public void onFailList(int pageIndex) {
        mAdapter.loadMoreFail(pageIndex);
        if(ListUtils.isEmpty(mAdapter.getData())){
            showErrorView();
        }
    }

    @Override
    public void reloadData() {
        mPresenter.refreshData(true);
    }

    public static PagerFragment newInstance(String id, ArrayList<PresellBean.PresellProduct> productList) {
        Bundle args = new Bundle();
        args.putString("id", id);
        args.putSerializable("productList", productList);
        PagerFragment fragment = new PagerFragment();
        fragment.setArguments(args);
        return fragment;
    }
}
