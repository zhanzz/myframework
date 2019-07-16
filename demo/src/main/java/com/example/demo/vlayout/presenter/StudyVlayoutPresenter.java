package com.example.demo.vlayout.presenter;

import com.example.demo.DemoApi;
import com.framework.common.base_mvp.BasePresenter;
import com.example.demo.vlayout.view.IStudyVlayoutView;
import com.framework.common.callBack.RxNetCallBack;
import com.framework.common.data.LoadType;
import com.framework.common.net.RxNet;
import com.framework.common.retrofit.RetorfitUtil;
import com.framework.model.demo.ActivityBean;
import com.framework.model.demo.ProductBean;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.disposables.Disposable;

public class StudyVlayoutPresenter extends BasePresenter<IStudyVlayoutView> {
    private int mCurrentPage=1;
    public static final int PAGE_SIZE = 10;
    private Disposable mProductDispose;
    public void getHomeData(boolean showLoad){
        mCurrentPage = 1;
        if(mProductDispose!=null){
            mProductDispose.dispose();
        }
        RxNet.request(RetorfitUtil.getRetorfitApi(DemoApi.class).getHomeData(), getMvpView(),
                showLoad? LoadType.LOAD:LoadType.NONE, new RxNetCallBack<ActivityBean>() {
            @Override
            public void onSuccess(ActivityBean data, int code, String msg) {
                getMvpView().onHomeData(data);
            }

            @Override
            public void onFailure(int code, String msg) {
                getMvpView().showErrorView();
            }
        });
    }

    public void getMoreProducts() {
        if(mProductDispose!=null){
            mProductDispose.dispose();
        }
        Map<String,String> params = new HashMap<>();
        params.put("pageSize", String.valueOf(PAGE_SIZE));
        params.put("startPage", String.valueOf(mCurrentPage));
        params.put("urlType","2");
        mProductDispose = RxNet.request(RetorfitUtil.getRetorfitApi(DemoApi.class).getHomeProducts(params), getMvpView(), LoadType.NONE, new RxNetCallBack<List<ProductBean>>() {
            @Override
            public void onSuccess(List<ProductBean> data, int code, String msg) {
                getMvpView().onProductList(data,mCurrentPage);
                mCurrentPage ++;
            }

            @Override
            public void onFailure(int code, String msg) {
                getMvpView().onProductFail(mCurrentPage);
            }
        });
    }
}