package com.framework.common.adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.entity.IExpandable;
import com.chad.library.adapter.base.loadmore.LoadMoreView;
import com.framework.common.R;
import com.framework.common.loading_view.SimpleLoadMoreView;
import com.framework.common.utils.ListUtils;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
/**
 * @author zhangzhiqiang
 * @date 2019/5/6.
 * description：专用于分页加载
 */
public abstract class BaseAdapter<T,K extends BaseViewHolder> extends BaseQuickAdapter<T,K> {
    private String strLoadEndText;
    private SmartRefreshLayout smartRefreshLayout;
    private boolean mRealControlMoreEnable;
    private boolean isRecyclerViewHolder=true;//是否回收header footer
    private RecyclerView mRecyclerView;
    private LoadMoreView mLoadMoreView;

    public BaseAdapter(RecyclerView recyclerView){
        this(recyclerView,0);
    }

    public BaseAdapter(RecyclerView recyclerView, int layoutResId) {
        super(layoutResId);
        bindToRecyclerView(recyclerView);
        //做一些统一的处理
        mLoadMoreView = new SimpleLoadMoreView(){
            @Override
            public void convert(BaseViewHolder holder) {
                super.convert(holder);
                if(getLoadMoreStatus()==STATUS_END && !TextUtils.isEmpty(strLoadEndText)){
                    holder.setText(R.id.tv_end_tip,strLoadEndText);
                }
            }
        };
        setLoadMoreView(mLoadMoreView);
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

    @Override
    protected RecyclerView getRecyclerView() {
        return mRecyclerView;
    }

    @Override
    public void bindToRecyclerView(RecyclerView recyclerView) {
        if (getRecyclerView() != recyclerView && recyclerView!=null) {
            mRecyclerView = recyclerView;

            recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                    if(newState==RecyclerView.SCROLL_STATE_IDLE){//静止状态时
                        if(isLastItemVisible()){
                            notifyLoadMoreToLoadingWhenFail();
                        }
                    }
                }
            });
        }
    }

    @Override
    public K onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = null;
        switch (viewType){
            case EMPTY_VIEW:
                view = getEmptyView();
                break;
            case HEADER_VIEW:
                view = getHeaderLayout();
                break;
            case FOOTER_VIEW:
                view = getFooterLayout();
                break;
        }
        if(view!=null && view.getParent()!=null) {
            ((ViewGroup)view.getParent()).removeView(view);
        }
        K viewHolder = super.onCreateViewHolder(parent, viewType);
        if(viewType==HEADER_VIEW||viewType==FOOTER_VIEW||viewType==EMPTY_VIEW){
            viewHolder.setIsRecyclable(isRecyclerViewHolder);
        }
        return viewHolder;
    }

    public void setOrAddData(@Nullable List<T> data, int pageIndex, int pageSize) {
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
        mRealControlMoreEnable = true;
        mRefreshListener = refreshListener;
    }

    public interface RefreshListener{
        void onRefresh();
        void onLoadMore();
    }

    public void setRealControlMoreEnable(boolean mRealControlMoreEnable) {
        this.mRealControlMoreEnable = mRealControlMoreEnable;
    }

    /**
     * 此方法解决在使用共享缓存池时的异常
     * @param isRecyclerViewHolder
     */
    public void setIsRecyclerHeadAndFooter(boolean isRecyclerViewHolder){
        this.isRecyclerViewHolder = isRecyclerViewHolder;
    }

    /**
     * 判断最后一个child是否完全显示出来
     *
     * @return true完全显示出来，否则false
     */
    private boolean isLastItemVisible() {
        RecyclerView.Adapter adapter = mRecyclerView.getAdapter();
        int position = 0;
        RecyclerView.LayoutManager layoutManager= mRecyclerView.getLayoutManager();
        if(layoutManager instanceof LinearLayoutManager){
            position=((LinearLayoutManager)layoutManager).findLastVisibleItemPosition();
        }else if(layoutManager instanceof StaggeredGridLayoutManager){
            int[] p = ((StaggeredGridLayoutManager)layoutManager).findLastVisibleItemPositions(null);
            Arrays.sort(p);
            position = p[p.length-1];
        }
        if(null == adapter || adapter.getItemCount() == 1){
            return false;
        }
        return position>=getLoadMoreViewPosition();
    }

    /**
     * The notification starts the callback and loads more
     */
    public void notifyLoadMoreToLoadingWhenFail() {
        if (mLoadMoreView.getLoadMoreStatus() == LoadMoreView.STATUS_FAIL) {
            mLoadMoreView.setLoadMoreStatus(LoadMoreView.STATUS_DEFAULT);
            notifyItemChanged(getLoadMoreViewPosition());
        }
    }

    public void notifyExpandDataChange(){
        List<T> result = new ArrayList<>();
        List<T> data = getData();
        if(!ListUtils.isEmpty(data)){
            for(T bean:data){
                if(bean instanceof IExpandable){
                    result.add(bean);
                    IExpandable iExpandable = ((IExpandable) bean);
                    if(iExpandable.isExpanded()&&!ListUtils.isEmpty(iExpandable.getSubItems())){
                        result.addAll(((IExpandable) bean).getSubItems());
                    }
                }
            }
        }
        setNewData(result);
    }

    /**
     * Set whether to use empty view
     *
     * @param isUseEmpty
     */
    @Override
    public void isUseEmpty(boolean isUseEmpty) {
        super.isUseEmpty(isUseEmpty);
    }
}
