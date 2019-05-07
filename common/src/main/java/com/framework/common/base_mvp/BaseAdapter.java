package com.framework.common.base_mvp;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.framework.common.R;
import com.framework.common.loading_view.SimpleLoadMoreView;
import com.framework.common.utils.ListUtils;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import java.util.List;
/**
 * @author zhangzhiqiang
 * @date 2019/5/6.
 * description：
 */
public abstract class BaseAdapter<T,K extends BaseViewHolder> extends BaseQuickAdapter<T,K> {
    private String strLoadEndText;
    private SmartRefreshLayout smartRefreshLayout;
    public BaseAdapter(RecyclerView recyclerView,int layoutResId) {
        super(layoutResId);
        //做一些统一的处理
        bindToRecyclerView(recyclerView);
        disableLoadMoreIfNotFullPage();//默认第一次加载不进入回调setOnLoadMoreListener
        setLoadMoreView(new SimpleLoadMoreView(){
            @Override
            public void convert(BaseViewHolder holder) {
                super.convert(holder);
                if(getLoadMoreStatus()==STATUS_END && !TextUtils.isEmpty(strLoadEndText)){
                    holder.setText(R.id.tv_end_tip,strLoadEndText);
                }
            }
        });
        setOnLoadMoreListener(new RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                if(mRefreshListener!=null){
                    mRefreshListener.onLoadMore();
                }
            }
        },recyclerView);
        setEnableLoadMore(false);
    }

    public void setOrAddData(@NonNull List<T> data, int pageIndex,int pageSize) {
        if(pageIndex<=1){
            super.setNewData(data);
        }else if(!ListUtils.isEmpty(data)){
            super.addData(data);
        }
        int size = data!=null? data.size():0;
        if(size<pageSize){
            loadMoreEnd();
        }else {
            loadMoreComplete();
        }
        if(smartRefreshLayout!=null){
            smartRefreshLayout.finishRefresh(true);
        }
    }

    public void setLoadEndText(String loadEndText){
        strLoadEndText = loadEndText;
    }

    public void setSmartRefreshLayout(SmartRefreshLayout smartRefreshLayout){
        this.smartRefreshLayout = smartRefreshLayout;
        smartRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                setEnableLoadMore(false);
                if(mRefreshListener!=null){
                    mRefreshListener.onRefresh();
                }
            }
        });
    }

    @Override
    public void loadMoreFail() {
        if(!ListUtils.isEmpty(mData)){
            super.loadMoreFail();
        }
        if(smartRefreshLayout!=null){
            smartRefreshLayout.finishRefresh(false);
        }
    }

    private RefreshListener mRefreshListener;

    public void setRefreshListener(RefreshListener refreshListener){
        mRefreshListener = refreshListener;
    }

    public interface RefreshListener{
        void onRefresh();
        void onLoadMore();
    }
}
