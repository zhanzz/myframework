package com.example.demo.pagelist;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.paging.ItemKeyedDataSource;
import androidx.paging.PageKeyedDataSource;

import com.example.demo.DemoApi;
import com.framework.common.base_mvp.IBaseView;
import com.framework.common.callBack.RxNetCallBack;
import com.framework.common.data.LoadType;
import com.framework.common.data.Result;
import com.framework.common.net.RxNet;
import com.framework.common.retrofit.RetorfitUtil;
import com.framework.model.demo.ProductBean;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.disposables.Disposable;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * @author zhangzhiqiang
 * @date 2019/11/15.
 * description：
 */
public class DataSourceByItem extends PageKeyedDataSource<Integer, ProductBean> implements IDataSource {
    private Call mProductDispose;
    private int mCurrentPage = 1;
    private IBaseView mvp;
    private MutableLiveData<ResultBean> resultState = new MutableLiveData<>();
    private LoadInitialParams<Integer> initParams;
    private LoadInitialCallback<Integer, ProductBean> initCallback;

    @Override
    public void loadInitial(@NonNull LoadInitialParams<Integer> params, @NonNull LoadInitialCallback<Integer, ProductBean> callback) {
        initParams = params;
        initCallback = callback;
        initLoad(params, callback);
    }

    private void initLoad(@NonNull LoadInitialParams<Integer> params, @NonNull LoadInitialCallback<Integer, ProductBean> callback) {
        ResultBean resultBean = new ResultBean();
        resultBean.setInit(true);
        resultBean.setLoadingState(LoadingState.Loading);
        resultState.postValue(resultBean);

        Map<String, String> sparams = new HashMap<>();
        sparams.put("pageSize", String.valueOf(params.requestedLoadSize));
        sparams.put("startPage", String.valueOf(1));
        sparams.put("urlType", "2");
        try {
            mProductDispose = RetorfitUtil.getRetorfitApi(DemoApi.class).getHomeProductsV2(sparams);
            Response<Result<List<ProductBean>>> response = mProductDispose.execute();
            List<ProductBean> list = response.body().getData();
            callback.onResult(list, 0, 2);
            resultBean.setLoadingState(LoadingState.Normal);
            resultState.postValue(resultBean);
        } catch (Exception e) {
            resultBean.setLoadingState(LoadingState.Failed);
            resultState.postValue(resultBean);
        }
    }

    @Override
    public void loadBefore(@NonNull LoadParams<Integer> params, @NonNull LoadCallback<Integer, ProductBean> callback) {
    }

    @Override
    public void loadAfter(@NonNull LoadParams<Integer> params, @NonNull LoadCallback<Integer, ProductBean> callback) {
        ResultBean resultBean = new ResultBean();
        resultBean.setInit(false);
        resultBean.setLoadingState(LoadingState.Loading);
        resultState.postValue(resultBean);

        Map<String, String> sparams = new HashMap<>();
        sparams.put("pageSize", String.valueOf(params.requestedLoadSize));
        sparams.put("startPage", String.valueOf(params.key));
        sparams.put("urlType", "2");
        mProductDispose = RetorfitUtil.getRetorfitApi(DemoApi.class).getHomeProductsV2(sparams);
        mProductDispose.enqueue(new Callback<Result<List<ProductBean>>>() {
            @Override
            public void onResponse(Call<Result<List<ProductBean>>> call, Response<Result<List<ProductBean>>> response) {
                if(response.isSuccessful()){
                    List<ProductBean> list = response.body().getData();
                    callback.onResult(list, params.key+1);
                    resultBean.setLoadingState(LoadingState.Normal);
                    resultState.postValue(resultBean);
                }else {
                    resultBean.setLoadingState(LoadingState.Failed);
                    resultState.postValue(resultBean);
                }
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                resultBean.setLoadingState(LoadingState.Failed);
                resultState.postValue(resultBean);
            }
        });
    }

    @NotNull
    @Override
    public MutableLiveData<ResultBean> getResultBean() {
        return resultState;
    }

    @Override
    public void refresh() {
        invalidate();
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

    @Override
    public void invalidate() {
        if (mProductDispose != null) {
            mProductDispose.cancel();
        }
        super.invalidate();
    }
}
