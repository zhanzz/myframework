package com.example.demo.viewpager_fragment.fragment;

import android.content.Intent;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.app.SharedElementCallback;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.demo.R;
import com.example.demo.R2;
import com.example.demo.viewpager_fragment.activity.BrowsePhotoActivity;
import com.example.demo.viewpager_fragment.adapter.TestSaveAdapter;
import com.example.demo.viewpager_fragment.presenter.PagerPresenter;
import com.example.demo.viewpager_fragment.presenter.TestPagerPresenter;
import com.example.demo.viewpager_fragment.view.IPagerView;
import com.facebook.drawee.view.SimpleDraweeView;
import com.framework.common.adapter.BaseAdapter;
import com.framework.common.base_mvp.BaseFragment;
import com.framework.common.base_mvp.BasePageFragment;
import com.framework.common.base_mvp.BasePresenter;
import com.framework.common.base_mvp.IPageBaseView;
import com.framework.common.data.LoadType;
import com.framework.common.utils.ListUtils;
import com.framework.common.utils.ViewUtil;
import com.framework.model.demo.PresellBean;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;

public class PagerFragment extends BasePageFragment<PresellBean.PresellProduct>{
    @BindView(R2.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R2.id.smartRefreshLayout)
    SmartRefreshLayout smartRefreshLayout;
    private TestSaveAdapter mAdapter;
    private TestPagerPresenter mPresenter;
    private RecyclerView.RecycledViewPool mRecyclerViewPool;
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("mData", (Serializable) mAdapter.getData());
        outState.putInt("pageIndex",mPresenter.getCurrentPage());
    }

    @Override
    protected void getSavedInstanceState(Bundle savedInstanceState) {
        ArrayList<PresellBean.PresellProduct> data = (ArrayList<PresellBean.PresellProduct>) savedInstanceState.getSerializable("mData");
        mPresenter.setData(data);
        mPresenter.setCurrentPage(savedInstanceState.getInt("pageIndex",1));
    }

    @Override
    protected TestPagerPresenter getPresenter() {
        if(mPresenter==null){
            mPresenter = new TestPagerPresenter(10);
        }
        return mPresenter;
    }

    @Override
    public void getParamData(Bundle bundle) {
        mPresenter.setId(bundle.getString("id"));
        if(ListUtils.isEmpty(mPresenter.getData())){
            mPresenter.setCurrentPage(2);
            mPresenter.setData((ArrayList<PresellBean.PresellProduct>) bundle.getSerializable("productList"));
        }
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_pager;
    }

    @NonNull
    @Override
    public BaseAdapter<PresellBean.PresellProduct, ? extends BaseViewHolder> getAdapter() {
        return mAdapter;
    }

    @Override
    public void bindData() {
        //testMerge添加的测试提交
        //int x = 4;
        mAdapter = new TestSaveAdapter(recyclerView);
        super.bindData();
        TextView tv = new TextView(getContext());
        tv.setText(mPresenter.getId());
        mAdapter.addHeaderView(tv);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setRecycledViewPool(mRecyclerViewPool);
        if (ListUtils.isEmpty(mPresenter.getData())) {//数据为空发起网络请求
            mPresenter.getFirst(LoadType.LOAD);
        }else{
            mAdapter.setNewData(mPresenter.getData());
            recyclerView.setAdapter(mAdapter);
        }
    }

    @Override
    public void initEvent() {
        super.initEvent();

        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                View iv = view.findViewById(R.id.itemIcon);
                if(iv instanceof SimpleDraweeView){
                    PresellBean.PresellProduct bean= mAdapter.getData().get(position);
                    ArrayList<String> list = new ArrayList();
                    list.add(bean.getProductAdsPic());
                    BrowsePhotoActivity.start(PagerFragment.this,0,list,iv);
                }
            }
        });

        setExitSharedElementCallback(new SharedElementCallback() {
            @Override
            public Parcelable onCaptureSharedElementSnapshot(View sharedElement, Matrix viewToGlobalMatrix, RectF screenBounds) {
                sharedElement.setVisibility(View.VISIBLE);
                return super.onCaptureSharedElementSnapshot(sharedElement, viewToGlobalMatrix, screenBounds);
            }
        });
    }

    @NonNull
    @Override
    protected SmartRefreshLayout getSmartRefreshLayout() {
        return smartRefreshLayout;
    }

    public void onReenter(int resultCode,Intent data) {
        // Do whatever with the data here
    }

    @NonNull
    @Override
    protected RecyclerView getRecyclerView() {
        return recyclerView;
    }

    public void setRecyclerViewPool(RecyclerView.RecycledViewPool recyclerViewPool) {
        mRecyclerViewPool = recyclerViewPool;
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
