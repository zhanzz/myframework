package com.example.demo.product_detail.fragment;

import androidx.recyclerview.widget.RecyclerView;

import com.example.demo.product_detail.adapter.TestAdapter;
import com.example.demo.product_detail.adapter.TuiProductAdapter;
import com.framework.common.base_mvp.BasePresenter;
import com.framework.common.base_mvp.BaseFragment;
import com.example.demo.product_detail.presenter.TuiProductPresenter;
import com.example.demo.product_detail.view.ITuiProductView;
import com.example.demo.R;
import com.framework.common.data.LoadType;
import com.framework.model.demo.ProductBean;

import java.util.List;

public class TuiProductFragment extends BaseFragment implements ITuiProductView {
    private TuiProductPresenter mPresenter;
    TuiProductAdapter adapter;
    @Override
    public int getLayoutId() {
        return R.layout.fragment_tui_product;
    }

    @Override
    public void bindData() {
        RecyclerView recyclerView = getView().findViewById(R.id.recyclerView);
        adapter = new TuiProductAdapter(recyclerView);
        recyclerView.setAdapter(adapter);
        mPresenter.getFirst(LoadType.LOAD);
    }

    @Override
    public void initEvent() {

    }

    @Override
    public BasePresenter getPresenter() {
        if (mPresenter == null) {
            mPresenter = new TuiProductPresenter(12);
        }
        return mPresenter;
    }

    @Override
    public void onPageData(List<ProductBean> data, int currentPage, int pageSize) {
        adapter.setOrAddData(data,currentPage,pageSize);
    }

    @Override
    public void onPageFail(int code, String msg, int currentPage) {
        adapter.loadMoreFail(currentPage);
        if(currentPage<=1){
            addReloadRequest(new Runnable() {
                @Override
                public void run() {
                    mPresenter.getFirst(LoadType.LOAD);
                }
            });
        }
    }
}
