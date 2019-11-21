package com.example.demo.pagelist;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.paging.ItemKeyedDataSource;

import com.example.demo.DemoApi;
import com.framework.common.base_mvp.IBaseView;
import com.framework.common.callBack.RxNetCallBack;
import com.framework.common.data.LoadType;
import com.framework.common.net.RxNet;
import com.framework.common.retrofit.RetorfitUtil;
import com.framework.model.demo.ProductBean;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.disposables.Disposable;

/**
 * @author zhangzhiqiang
 * @date 2019/11/15.
 * description：
 */
public class DataSourceByItem extends ItemKeyedDataSource<Integer, ProductBean> implements IDataSource{
    private Disposable mProductDispose;
    private int mCurrentPage=1;
    private IBaseView mvp;
    @Override
    public void loadInitial(@NonNull LoadInitialParams<Integer> params, @NonNull LoadInitialCallback<ProductBean> callback) {
        Map<String,String> sparams = new HashMap<>();
        sparams.put("pageSize",String.valueOf(params.requestedLoadSize) );
        sparams.put("startPage", String.valueOf(mCurrentPage));
        sparams.put("urlType","2");
        mProductDispose = RxNet.request(RetorfitUtil.getRetorfitApi(DemoApi.class).getHomeProducts(sparams),getMvp(), LoadType.NONE, new RxNetCallBack<List<ProductBean>>() {
            @Override
            public void onSuccess(List<ProductBean> data, int code, String msg) {
                mCurrentPage ++;
                callback.onResult(data);
            }

            @Override
            public void onFailure(int code, String msg) {
                //getMvp().onProductFail(mCurrentPage);
            }
        });
    }

    @Override
    public void loadAfter(@NonNull LoadParams<Integer> params, @NonNull LoadCallback<ProductBean> callback) {

    }

    @Override
    public void loadBefore(@NonNull LoadParams<Integer> params, @NonNull LoadCallback<ProductBean> callback) {

    }

    @NonNull
    @Override
    public Integer getKey(@NonNull ProductBean item) {
        return null;
    }

    @NotNull
    @Override
    public MutableLiveData<ResultBean> getResultBean() {
        return null;
    }

    @Override
    public void refresh() {

    }

    @Override
    public void retry() {

    }

    @NotNull
    @Override
    public IBaseView getMvp() {
        return mvp;
    }

    @Override
    public void setMvp(@NotNull IBaseView iBaseView) {
        mvp = iBaseView;
    }
}
