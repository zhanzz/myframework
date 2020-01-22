package com.example.jetpack.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.jetpack.DemoApi;
import com.framework.common.callBack.RxNetCallBack;
import com.framework.common.data.LoadType;
import com.framework.common.data.LoadViewType;
import com.framework.common.net.RxNet;
import com.framework.common.retrofit.RetorfitUtil;
import com.framework.model.demo.PresellBean;
import com.framework.model.demo.ProductAndFilterListBean;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

/**
 * @author zhangzhiqiang
 * @date 2020/1/6.
 * description：
 */
public class OneViewModel extends ViewModel {
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    public final MutableLiveData<LoadViewType> loading = new MutableLiveData<>();
    public final MutableLiveData<Boolean> errorView = new MutableLiveData<>();
    public final MutableLiveData<ProductAndFilterListBean> presellBean = new MutableLiveData<>();

    private int pageSize = 10;
    private int mCurrentPage = 1;
    public int test = 0;//ViewModel 类让数据可在发生屏幕旋转等"配置"更改后继续存在


    public void getFirst(LoadType loadType){
        mCurrentPage = 1;
        getProducts(loadType);
    }

    public void getMore(){
        mCurrentPage++;
        getProducts(LoadType.NONE);
    }

    private void getProducts(LoadType loadType){
        Map<String, Object> params = new HashMap<>();
        params.put("pageSize",pageSize);
        params.put("startPage",mCurrentPage);
        params.put("queryString","纸");
        Disposable disposable = RxNet.request(RetorfitUtil.getMallRetorfitApi(DemoApi.class).getSearchProduct(params),loading,loadType, new RxNetCallBack<ProductAndFilterListBean>() {
                    @Override
                    public void onSuccess(ProductAndFilterListBean data, int code, String msg) {
                        presellBean.setValue(data);
                    }

                    @Override
                    public void onFailure(int code, String msg) {
                        errorView.setValue(true);
                    }
                });
        compositeDisposable.add(disposable);
    }

    public int getPageSize() {
        return pageSize;
    }

    public int getCurrentPage() {
        return mCurrentPage;
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        if(compositeDisposable!=null){
            compositeDisposable.dispose();
        }
    }
}
