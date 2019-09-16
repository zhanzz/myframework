package com.framework.common.base_mvp;

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
public abstract class BasePagePresenter<R,T extends IPageBaseView<R>> extends BasePresenter<T>{
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
        mDispose = RxNet.request(getObservable(), getMvpView(), loadType, new RxNetCallBack<R>() {
            @Override
            public void onSuccess(R data, int code, String msg) {
                getMvpView().onPageData(data,mCurrentPage,mPageSize);
                mCurrentPage++;
            }

            @Override
            public void onFailure(int code, String msg) {
                getMvpView().onPageFail(code,msg,mCurrentPage);
            }
        });
    }

    protected abstract Observable<Result<R>> getObservable();

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
