package com.framework.common.base_mvp;

import androidx.annotation.NonNull;

import com.framework.common.callBack.RxNetCallBack;
import com.framework.common.data.LoadType;
import com.framework.common.data.Result;
import com.framework.common.net.RxNet;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;

/**
 * @author zhangzhiqiang
 * @date 2019/9/12.
 * description：
 */
public abstract class BasePagePresenter<R,T extends IPageBaseView> extends BasePresenter<T>{
    private int mCurrentPage;
    private final int mPageSize;
    private Disposable mDispose;
    public BasePagePresenter(int pageSize){
        mPageSize = pageSize;
    }

    public void getFirst(LoadType loadType){
        mCurrentPage = 1;
        getPageData(loadType);
    }
    
    public void getMore(){
        getPageData(LoadType.NONE);
    }

    private void getPageData(LoadType loadType){
        if(mDispose!=null){
            mDispose.dispose();
        }
        Map<String,Object> params = new HashMap<>();
        params.put("pageSize",mPageSize);
        params.put("startPage",mCurrentPage);
        mDispose = RxNet.request(getObservable(params), getMvpView(), loadType, new RxNetCallBack<R>() {
            @Override
            public void onSuccess(R data, int code, String msg) {
                onPageData(data,mCurrentPage,mPageSize);
                mCurrentPage++;
            }

            @Override
            public void onFailure(int code, String msg) {
                getMvpView().onPageFail(code,msg,mCurrentPage);
            }
        });
    }

    protected abstract Observable<Result<R>> getObservable(@NonNull Map<String,Object> params);

    protected abstract void onPageData(R data,int mCurrentPage,int mPageSize);

    public int getCurrentPage() {
        return mCurrentPage;
    }

    public void setCurrentPage(int currentPage){
        mCurrentPage = currentPage;
    }

    public int getPageSize() {
        return mPageSize;
    }
}
