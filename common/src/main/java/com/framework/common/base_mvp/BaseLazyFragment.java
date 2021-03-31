package com.framework.common.base_mvp;

import android.os.Bundle;
import androidx.annotation.Nullable;
/**
 * 懒加载Fragment
 * 不会在Fragment创建时就就加载数据，1而是只有用户可见时去加载onLoad()，从而不影响App性能
 */
public abstract class BaseLazyFragment extends BaseFragment {
    //是否用户可见
    protected boolean isVisible = false;

    //false为对用户可见时调用加载数据，true为不会再加载
    protected boolean isLoadData = false;
    //fragment是否已创建准备好
    private boolean isPrepared;


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if(getUserVisibleHint()){
            isVisible = true;
            //只有用户可见时进行加载
            lazyLoad();
        }else{
            isVisible = false;
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        isVisible = false;
    }

    @Override
    public void onResume() {
        super.onResume();
        isVisible = true;
        //只有用户可见时进行加载
        lazyLoad();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        isPrepared = true;
        lazyLoad();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        isPrepared = false;
    }

    protected abstract void loadLazyData();

    private void lazyLoad(){
        if (!isPrepared || !isVisible || isLoadData) {
            return;
        }
        loadLazyData();
        isLoadData = true;
    }
}
