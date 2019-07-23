package com.framework.common.adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.view.ViewGroup;
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
 * description：专用于分页加载
 */
public abstract class BaseAdapter<T,K extends BaseViewHolder> extends BaseQuickAdapter<T,K> {
    private String strLoadEndText;
    private SmartRefreshLayout smartRefreshLayout;
    private boolean mRealControlMoreEnable=true;
    private boolean mCanShowEmptyView=true;//是否允许展示空视图
    private boolean isRecyclerViewHolder=true;//是否回收header footer

    public BaseAdapter(RecyclerView recyclerView, int layoutResId) {
        super(layoutResId);
        //做一些统一的处理
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
        disableLoadMoreIfNotFullPage();
        setEnableLoadMore(false);
    }

    @Override
    public K onCreateViewHolder(ViewGroup parent, int viewType) {
        K viewHolder = super.onCreateViewHolder(parent, viewType);
        if(viewType==HEADER_VIEW||viewType==FOOTER_VIEW||viewType==EMPTY_VIEW){
            viewHolder.setIsRecyclable(isRecyclerViewHolder);
            if(viewHolder.itemView.getParent()!=null){
                ((ViewGroup)viewHolder.itemView.getParent()).removeView(viewHolder.itemView);
            }
        }
        return viewHolder;
    }

    public void setOrAddData(@NonNull List<T> data, int pageIndex, int pageSize) {
        if(pageIndex<=1){
            super.setNewData(data);
            if(!mRealControlMoreEnable){
                setEnableLoadMore(false);
            }
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

    @Override
    public void setNewData(@Nullable List<T> data) {
        super.setNewData(data);
        if(!mRealControlMoreEnable){
            setEnableLoadMore(false);
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

    public void loadMoreFail(int pageIndex) {
        if(pageIndex<=1){
            if(smartRefreshLayout!=null){
                smartRefreshLayout.finishRefresh(false);
            }
        }else{
            if(!ListUtils.isEmpty(mData)){
                super.loadMoreFail();
            }
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

    public void setRealControlMoreEnable(boolean mRealControlMoreEnable) {
        this.mRealControlMoreEnable = mRealControlMoreEnable;
    }

    public void setCanShowEmptyView(boolean canShowEmptyView) {
        this.mCanShowEmptyView = canShowEmptyView;
    }

    @Override
    public int getEmptyViewCount() {
        if (mCanShowEmptyView){
            return super.getEmptyViewCount();
        }else {
            return 0;
        }
    }

    /**
     * 此方法解决在使用共享缓存池时的异常
     * @param isRecyclerViewHolder
     */
    public void setIsRecyclerHeadAndFooter(boolean isRecyclerViewHolder){
        this.isRecyclerViewHolder = isRecyclerViewHolder;
    }
}
