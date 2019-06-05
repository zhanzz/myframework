package com.example.demo.viewpager_fragment.presenter;

import com.example.demo.DemoApi;
import com.framework.common.base_mvp.BasePresenter;
import com.example.demo.viewpager_fragment.view.IPageFragmentView;
import com.framework.common.callBack.RxNetCallBack;
import com.framework.common.data.LoadType;
import com.framework.common.net.RxNet;
import com.framework.common.retrofit.RetorfitUtil;
import com.framework.model.demo.PresellBean;

import java.util.HashMap;
import java.util.Map;

public class PageFragmentPresenter extends BasePresenter<IPageFragmentView> {

    public void getCategory(){
        Map<String, Object> params = new HashMap<>();
        params.put("pageSize",PagerPresenter.PAGE_SIZE);
        params.put("startPage","1");
        params.put("activityId","");

        RxNet.request(RetorfitUtil.getMallRetorfitApi(DemoApi.class).loadCategoryData(params),
                getMvpView(), LoadType.LOAD, new RxNetCallBack<PresellBean>() {
                    @Override
                    public void onSuccess(PresellBean data, int code, String msg) {
                        getMvpView().onCategoryData(data);
                    }

                    @Override
                    public void onFailure(int code, String msg) {
                        getMvpView().showErrorView();
                    }
                });
    }
}