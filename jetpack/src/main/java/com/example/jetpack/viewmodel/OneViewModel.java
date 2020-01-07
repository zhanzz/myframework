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
    public final MutableLiveData<PresellBean> presellBean = new MutableLiveData<>();

    public void getProducts(){
        Map<String, Object> params = new HashMap<>();
        params.put("pageSize","10");
        params.put("startPage","1");
        params.put("activityId","");
        Disposable disposable = RxNet.request(RetorfitUtil.getMallRetorfitApi(DemoApi.class).loadCategoryData(params),loading,LoadType.LOAD, new RxNetCallBack<PresellBean>() {
                    @Override
                    public void onSuccess(PresellBean data, int code, String msg) {
                        presellBean.setValue(data);
                    }

                    @Override
                    public void onFailure(int code, String msg) {
                        errorView.setValue(true);
                    }
                });
        compositeDisposable.add(disposable);
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        if(compositeDisposable!=null){
            compositeDisposable.dispose();
        }
    }
}
