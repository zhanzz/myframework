package com.example.retrofitframemwork.login.presenter;

import com.example.retrofitframemwork.AppApi;
import com.example.retrofitframemwork.login.view.ITestView;
import com.example.retrofitframemwork.utils.RetorfitUtil;
import com.framework.common.base_mvp.BasePresenter;
import com.framework.common.callBack.RxNetCallBack;
import com.framework.common.utils.RxNet;
import com.framework.model.TestBean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.disposables.Disposable;

/**
 * @author zhangzhiqiang
 * @date 2019/5/6.
 * description：
 */
public class TestPresenter extends BasePresenter<ITestView> {
    public static final int PAGE_SIZE = 5;
    int mCurentPage;
    Disposable mLasDisposable;
    public void refreshData(boolean isShowLoading){
        mCurentPage=1;
        if(mLasDisposable!=null){//刷新时取消上一次的请求
            mLasDisposable.dispose();
        }
        loadData(isShowLoading);
    }

    public void loadMoreData(){
        loadData(false);
    }

    private void loadData(boolean isShowLoading){
        final int localPage = mCurentPage;
        Map<String,Object> params = new HashMap<>();
        params.put("date","2019-05-06");
        mLasDisposable = RxNet.request(RetorfitUtil.getRetorfitApi().loadPageData(params), getMvpView(), isShowLoading, new RxNetCallBack<TestBean>() {
            @Override
            public void onSuccess(TestBean data, int code, String msg) {
                mCurentPage++;
                List<String> list = new ArrayList<>();
                for(int i=0;i<(mCurentPage<3?5:4);i++){
                   list.add("string"+i);
                }
                getMvpView().onPageData(list,localPage);
            }

            @Override
            public void onFailure(int code, String msg) {
                if(localPage<=1){
                    getMvpView().showErrorView();
                }
                getMvpView().showToast(msg);
                getMvpView().onLoadFail();
            }
        });
    }
}
