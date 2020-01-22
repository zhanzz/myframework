package com.example.demo.viewpager_fragment.presenter;

import com.example.demo.DemoApi;
import com.example.demo.viewpager_fragment.view.IPagerView;
import com.framework.common.base_mvp.BasePresenter;
import com.framework.common.callBack.RxNetCallBack;
import com.framework.common.data.LoadType;
import com.framework.common.net.RxNet;
import com.framework.common.retrofit.RetorfitUtil;
import com.framework.model.demo.PresellBean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import io.reactivex.disposables.Disposable;

/**
 * @author zhangzhiqiang
 * @date 2019/5/24.
 * description：
 */
public class PagerPresenter extends BasePresenter<IPagerView> {
    public final static int PAGE_SIZE=10;
    private int pageIndex = 1;
    private String id;
    private ArrayList<PresellBean.PresellProduct> mData;
    private Disposable mLastDisposable;
    public void refreshData(boolean isLoading){
        pageIndex = 1;
        getProducts(isLoading);
    }

    public void getMoreList(){
        getProducts(false);
    }

    private void getProducts(boolean isLoading){
        Map<String, Object> params = new HashMap<>();
        params.put("pageSize",PAGE_SIZE);
        params.put("startPage",pageIndex);
        params.put("activityId",id);
        if(mLastDisposable!=null){
            mLastDisposable.dispose();
        }
        mLastDisposable = RxNet.request(RetorfitUtil.getMallRetorfitApi(DemoApi.class).loadCategoryData(params),
                getMvpView(), isLoading ? LoadType.LOAD:LoadType.NONE, new RxNetCallBack<PresellBean>() {
                    @Override
                    public void onSuccess(PresellBean data, int code, String msg) {
                        getMvpView().onPageData(data.getProductList(),pageIndex,PAGE_SIZE);
                        pageIndex++;
                    }

                    @Override
                    public void onFailure(int code, String msg) {
                        getMvpView().onPageFail(code,msg,pageIndex);
                    }
                });
    }

    public int getPageIndex() {
        return pageIndex;
    }

    public void setPageIndex(int pageIndex) {
        this.pageIndex = pageIndex;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ArrayList<PresellBean.PresellProduct> getData() {
        return mData;
    }

    public void setData(ArrayList<PresellBean.PresellProduct> mData) {
        this.mData = mData;
    }
}
