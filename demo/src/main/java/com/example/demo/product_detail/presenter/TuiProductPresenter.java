package com.example.demo.product_detail.presenter;

import androidx.annotation.NonNull;

import com.example.demo.DemoApi;
import com.framework.common.base_mvp.BasePagePresenter;
import com.framework.common.base_mvp.BasePresenter;
import com.example.demo.product_detail.view.ITuiProductView;
import com.framework.common.data.Result;
import com.framework.common.retrofit.RetorfitUtil;
import com.framework.model.demo.ProductBean;

import java.util.List;
import java.util.Map;

import io.reactivex.Observable;

public class TuiProductPresenter extends BasePagePresenter<List<ProductBean>,ITuiProductView> {

    public TuiProductPresenter(int pageSize) {
        super(pageSize);
    }

    @Override
    protected Observable<Result<List<ProductBean>>> getObservable(@NonNull Map<String,Object> params) {
        return RetorfitUtil.getMallRetorfitApi(DemoApi.class).getHomeProducts(params);
    }

    @Override
    protected void onPageData(List<ProductBean> data, int mCurrentPage, int mPageSize) {
        getMvpView().onPageData(data,mCurrentPage,mPageSize);
    }
}