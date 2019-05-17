package com.framework.common.adapter;

import android.support.annotation.NonNull;
import android.support.v4.util.SparseArrayCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import com.framework.common.R;
import com.framework.common.loading_view.MySimpleLoadMoreView;
import com.framework.common.utils.LogUtil;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import java.util.List;
/**
 * BaseQuickAdapter的添加头脚部感觉实现不太好，支持添加头脚，下拉刷新，上拉加载更多
 * 与adapter剥离以达到自由实现
 * Created by zhangzhiqiang
 */
public class HeaderAndFooterWrapper extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int ITEM_TYPE_LOAD_MORE = Integer.MAX_VALUE - 2;
    private static final int ITEM_TYPE_EMPTY = Integer.MAX_VALUE - 1;
    private static final int BASE_ITEM_TYPE_HEADER = 100000;
    private static final int BASE_ITEM_TYPE_FOOTER = 200000;

    private String strLoadEndText;
    private int mPreLoadNumber = 1;
    private SparseArrayCompat<View> mHeaderViews = new SparseArrayCompat<>();
    private SparseArrayCompat<View> mFootViews = new SparseArrayCompat<>();
    private View mEmptyView = null;
    private int mEmptyLayoutId;
    private boolean mLoading = false;
    private RecyclerView.Adapter mInnerAdapter;
    private boolean mNeedHead = true;//当数据为空时是否需要展示头部视图
    private LoadMoreView mLoadMoreView;
    private boolean mEnableLoadMoreEndClick = false;//当加载没有更多数据后，点击是否要加载更多
    private boolean mLoadMoreEnable=false;//当刷新后会自动置为true（为了智能的加载更多）
    private boolean mRealControlMoreEnable=true;
    private SmartRefreshLayout smartRefreshLayout;

    public HeaderAndFooterWrapper(RecyclerView.Adapter adapter) {
        mInnerAdapter = adapter;
        mLoadMoreView = new MySimpleLoadMoreView(){
            @Override
            public void convert(ViewHolder holder) {
                super.convert(holder);
                if(getLoadMoreStatus()==STATUS_END && !TextUtils.isEmpty(strLoadEndText)){
                    holder.setText(R.id.tv_end_tip,strLoadEndText);
                }
            }
        };
        setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                if(mRefreshListener!=null){
                    mRefreshListener.onLoadMore();
                }
            }
        });
        setDataSetChangeObserver();
    }

    public RecyclerView.Adapter getInnerAdapter() {
        return mInnerAdapter;
    }

    public void setInnerAdapter(RecyclerView.Adapter mInnerAdapter) {
        this.mInnerAdapter = mInnerAdapter;
    }

    public void setEmptyView(View view) {
        mEmptyView = view;
    }

    public void setEmptyLayoutId(int emptyLayoutId) {
        this.mEmptyLayoutId = emptyLayoutId;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mHeaderViews.get(viewType) != null) {
            ViewHolder holder = ViewHolder.createViewHolder(parent.getContext(), mHeaderViews.get(viewType));
            return holder;
        } else if (mFootViews.get(viewType) != null) {
            ViewHolder holder = ViewHolder.createViewHolder(parent.getContext(), mFootViews.get(viewType));
            if (holder.itemView.getParent() != null) {
                LogUtil.e("重复添加了");
            }
            return holder;
        } else if (viewType == ITEM_TYPE_EMPTY) {
            ViewHolder holder;
            if (mEmptyView != null) {
                holder = ViewHolder.createViewHolder(parent.getContext(), mEmptyView);
            } else {
                holder = ViewHolder.createViewHolder(parent.getContext(), parent, mEmptyLayoutId);
            }
            return holder;
        }else if(viewType == ITEM_TYPE_LOAD_MORE){
            return createLoadingView(parent);
        }
        return mInnerAdapter.onCreateViewHolder(parent, viewType);
    }

    @Override
    public int getItemViewType(int position) {
        if (position < 0) {
            position = 0;
        }
        if (isEmpty()) {
            if (isHeaderViewPos(position)) {
                return mHeaderViews.keyAt(position);
            } else {
                return ITEM_TYPE_EMPTY;
            }
        }
        if (isShowLoadMore(position)) {
            return ITEM_TYPE_LOAD_MORE;
        }
        if (isHeaderViewPos(position)) {
            return mHeaderViews.keyAt(position);
        } else if (isFooterViewPos(position)) {
            int i = position - getHeadersCount() - getRealItemCount();
            return mFootViews.keyAt(mFootViews.size() - i - 1);//为了二级页面加载在前面
        }
        return mInnerAdapter.getItemViewType(position - getHeadersCount());
    }

    private int getRealItemCount() {
        return mInnerAdapter.getItemCount();
    }

    private ViewHolder createLoadingView(ViewGroup parent) {
        ViewHolder holder = ViewHolder.createViewHolder(parent.getContext(),parent,mLoadMoreView.getLayoutId());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mLoadMoreView.getLoadMoreStatus() == LoadMoreView.STATUS_FAIL) {
                    notifyLoadMoreToLoading();
                }
                if (mEnableLoadMoreEndClick && mLoadMoreView.getLoadMoreStatus() == LoadMoreView.STATUS_END) {
                    notifyLoadMoreToLoading();
                }
            }
        });
        return holder;
    }

    private void notifyLoadMoreToLoading() {
        if (mLoadMoreView.getLoadMoreStatus() == LoadMoreView.STATUS_LOADING) {
            return;
        }
        mLoadMoreView.setLoadMoreStatus(LoadMoreView.STATUS_DEFAULT);
        notifyItemChanged(getLoadMoreViewPosition());
    }

    public int getLoadMoreViewPosition() {
        return getHeadersCount() + getFootersCount() + getRealItemCount();
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (isHeaderViewPos(position)) {
            return;
        }
        if(getItemViewType(position) == ITEM_TYPE_LOAD_MORE){
            autoLoadMore(position);
            mLoadMoreView.convert((ViewHolder) holder);
            return;
        }
        if (isFooterViewPos(position)) {
            return;
        }
        if (getItemViewType(position) == ITEM_TYPE_EMPTY) {
            return;
        }
        mInnerAdapter.onBindViewHolder(holder, position - getHeadersCount());
    }

    @Override
    public int getItemCount() {
        if (isEmpty()) {
            return mNeedHead ? (getHeadersCount() + 1) : 1;//根据mNeedHead来决定是否保留头部
        }
        return getHeadersCount() + getFootersCount() + getRealItemCount()+(getLoadMoreViewCount()>0 ? 1 : 0);
    }

    private boolean isEmpty() {
        return (mEmptyView != null || mEmptyLayoutId != 0) && mInnerAdapter.getItemCount() == 0;
    }

    GridLayoutManager.SpanSizeLookup oldLoopUp;

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        oldLoopUp = WrapperUtils.onAttachedToRecyclerView(mInnerAdapter, recyclerView, oldLoopUp, new WrapperUtils.SpanSizeCallback() {
            @Override
            public int getSpanSize(GridLayoutManager layoutManager, GridLayoutManager.SpanSizeLookup oldLookup, int position) {
                int viewType = getItemViewType(position);
                if (mHeaderViews.get(viewType) != null) {
                    return layoutManager.getSpanCount();
                } else if (mFootViews.get(viewType) != null) {
                    return layoutManager.getSpanCount();
                }else if(viewType==ITEM_TYPE_EMPTY || viewType==ITEM_TYPE_LOAD_MORE){
                    return layoutManager.getSpanCount();
                }
                if (oldLookup != null) {
                    return oldLookup.getSpanSize(position - mHeaderViews.size());
                }
                return 1;
            }
        });
    }

    @Override
    public void onViewAttachedToWindow(RecyclerView.ViewHolder holder) {
        mInnerAdapter.onViewAttachedToWindow(holder);
        int position = holder.getLayoutPosition();
        if (isHeaderViewPos(position) || isFooterViewPos(position) || isEmpty()) {
            WrapperUtils.setFullSpan(holder);
        }
    }

    private boolean isHeaderViewPos(int position) {
        return position < getHeadersCount();
    }

    private boolean isFooterViewPos(int position) {
        return position >= getHeadersCount() + getRealItemCount();
    }


    public void addHeaderView(View view) {
        if (!isContanerHeaderView(view)) {//防止重复添加
            mHeaderViews.put(mHeaderViews.size() + BASE_ITEM_TYPE_HEADER, view);
        }
    }

    private boolean isContanerHeaderView(View view) {
        return mHeaderViews.indexOfValue(view) >= 0;
    }

    public void addFootView(View view) {
        mFootViews.put(mFootViews.size() + BASE_ITEM_TYPE_FOOTER, view);
    }

    public int getHeadersCount() {
        return mHeaderViews.size();
    }

    public int getFootersCount() {
        return mFootViews.size();
    }

    public int getHeadViewPosition(View view) {
        return mHeaderViews.indexOfValue(view);
    }

    public View findHeadViewByType(Class className) {
        int size = mHeaderViews.size();
        for (int i = 0; i < size; i++) {
            View view = mHeaderViews.valueAt(i);
            if (view.getClass().getSimpleName().equals(className.getSimpleName())) {
                return view;
            }
        }
        return null;
    }

    public void removeHeadView(View view) {
        int position = getHeadViewPosition(view);
        if (position != -1) {
            mHeaderViews.removeAt(position);
            notifyItemRemoved(position);
        }
    }

    public View getFooterView(int position) {
        return mFootViews.valueAt(position);
    }

    public void clearAllHead() {
        mHeaderViews.clear();
        notifyDataSetChanged();
    }

    public void clearAllFooter() {
        mFootViews.clear();
        notifyDataSetChanged();
    }

    public int getFooterViewPosition(View view) {
        return mFootViews.indexOfValue(view);
    }

    public void removeFooterView(View view) {
        int position = getFooterViewPosition(view);
        if (position != -1) {
            mFootViews.removeAt(position);
            notifyItemRemoved(mHeaderViews.size() + mInnerAdapter.getItemCount() + position);
        }
    }

    private boolean isObserver = false;

    public void setDataSetChangeObserver() {
        isObserver = true;
    }

    @Override
    public void registerAdapterDataObserver(RecyclerView.AdapterDataObserver observer) {
        super.registerAdapterDataObserver(observer);
        if (isObserver) {
            mInnerAdapter.registerAdapterDataObserver(mObserver);
        }
    }

    @Override
    public void unregisterAdapterDataObserver(RecyclerView.AdapterDataObserver observer) {
        super.unregisterAdapterDataObserver(observer);
        if (isObserver) {
            mInnerAdapter.unregisterAdapterDataObserver(mObserver);
        }
    }

    private RecyclerView.AdapterDataObserver mObserver = new RecyclerView.AdapterDataObserver() {
        @Override
        public void onChanged() {
            notifyDataSetChanged();
        }

        @Override
        public void onItemRangeChanged(int positionStart, int itemCount) {
            notifyItemRangeChanged(positionStart + getHeadersCount(), itemCount);
        }

        @Override
        public void onItemRangeChanged(int positionStart, int itemCount, Object payload) {
            notifyItemRangeChanged(positionStart + getHeadersCount(), itemCount, payload);
        }

        @Override
        public void onItemRangeInserted(int positionStart, int itemCount) {
            notifyItemRangeInserted(positionStart + getHeadersCount(), itemCount);
        }

        @Override
        public void onItemRangeRemoved(int positionStart, int itemCount) {
            notifyItemRangeRemoved(positionStart + getHeadersCount(), itemCount);
        }

        @Override
        public void onItemRangeMoved(int fromPosition, int toPosition, int itemCount) {
            notifyItemMoved(fromPosition + getHeadersCount(), toPosition + getHeadersCount());
        }
    };

    public void setHeadWhenEmpty(boolean needHead) {
        mNeedHead = needHead;
    }

    public void setLoadEndText(String strLoadEndText) {
        this.strLoadEndText = strLoadEndText;
    }

    public <T> void loadComplete(List<T> data, int pageSize) {
        mLoadMoreEnable = true;
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

    public interface OnLoadMoreListener {
        void onLoadMoreRequested();
    }

    private OnLoadMoreListener mOnLoadMoreListener;

    public void setOnLoadMoreListener(OnLoadMoreListener loadMoreListener) {
        if (loadMoreListener != null) {
            mOnLoadMoreListener = loadMoreListener;
        }
    }

    public void setLoadMoreView(LoadMoreView loadMoreView) {
        mLoadMoreView = loadMoreView;
    }

    private boolean isShowLoadMore(int position) {
        return getLoadMoreViewCount()>0 && (position >= (mHeaderViews.size()+mInnerAdapter.getItemCount()+mFootViews.size()));
    }

    private void autoLoadMore(int position) {
        if (getLoadMoreViewCount() == 0) {
            return;
        }
        if (position < getItemCount() - mPreLoadNumber) {
            return;
        }
        if (mLoadMoreView.getLoadMoreStatus() != LoadMoreView.STATUS_DEFAULT) {
            return;
        }
        mLoadMoreView.setLoadMoreStatus(LoadMoreView.STATUS_LOADING);
        if (!mLoading) {
            mLoading = true;
            mOnLoadMoreListener.onLoadMoreRequested();
        }
    }

    /**
     * @return Whether the Adapter is actively showing load
     * progress.
     */
    public boolean isLoading() {
        return mLoading;
    }


    /**
     * Refresh end, no more data
     */
    public void loadMoreEnd() {
        loadMoreEnd(false);
    }

    /**
     * Load more view count
     *
     * @return 0 or 1
     */
    public int getLoadMoreViewCount() {
        if (mOnLoadMoreListener == null || !mLoadMoreEnable || !mRealControlMoreEnable) {
            return 0;
        }
        if (mInnerAdapter.getItemCount() == 0) {
            return 0;
        }
        return 1;
    }

    /**
     * Refresh end, no more data
     *
     * @param gone if true gone the load more view
     */
    public void loadMoreEnd(boolean gone) {
        if (getLoadMoreViewCount() == 0) {
            return;
        }
        mLoading = false;
        mLoadMoreView.setLoadMoreEndGone(gone);
        if (gone) {
            notifyItemRemoved(getLoadMoreViewPosition());
        } else {
            mLoadMoreView.setLoadMoreStatus(LoadMoreView.STATUS_END);
            notifyItemChanged(getLoadMoreViewPosition());
        }
    }

    /**
     * Refresh complete
     */
    public void loadMoreComplete() {
        if (getLoadMoreViewCount() == 0) {
            return;
        }
        mLoading = false;
        mLoadMoreEnable = true;
        mLoadMoreView.setLoadMoreStatus(LoadMoreView.STATUS_DEFAULT);
        notifyItemChanged(getLoadMoreViewPosition());
    }

    /**
     * Refresh failed
     */
    public void loadMoreFail() {
        if (getLoadMoreViewCount() == 0) {
            return;
        }
        mLoading = false;
        mLoadMoreView.setLoadMoreStatus(LoadMoreView.STATUS_FAIL);
        notifyItemChanged(getLoadMoreViewPosition());
    }

    /**
     * Set the enabled state of load more.
     *
     * @param enable True if load more is enabled, false otherwise.
     */
    public void setEnableLoadMore(boolean enable) {
        int oldLoadMoreCount = getLoadMoreViewCount();
        mLoadMoreEnable = enable;
        int newLoadMoreCount = getLoadMoreViewCount();

        if (oldLoadMoreCount == 1) {
            if (newLoadMoreCount == 0) {
                notifyItemRemoved(getLoadMoreViewPosition());
            }
        } else {
            if (newLoadMoreCount == 1) {
                mLoadMoreView.setLoadMoreStatus(LoadMoreView.STATUS_DEFAULT);
                notifyItemInserted(getLoadMoreViewPosition());
            }
        }
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

    private BaseAdapter.RefreshListener mRefreshListener;

    public void setRefreshListener(BaseAdapter.RefreshListener refreshListener){
        mRefreshListener = refreshListener;
    }

    public void setLoadMoreEnable(boolean mLoadMoreEnable) {
        int oldLoadMoreCount = getLoadMoreViewCount();
        this.mLoadMoreEnable = mLoadMoreEnable;
        int newLoadMoreCount = getLoadMoreViewCount();

        if (oldLoadMoreCount == 1) {
            if (newLoadMoreCount == 0) {
                notifyItemRemoved(getLoadMoreViewPosition());
            }
        } else {
            if (newLoadMoreCount == 1) {
                mLoadMoreView.setLoadMoreStatus(com.chad.library.adapter.base.loadmore.LoadMoreView.STATUS_DEFAULT);
                notifyItemInserted(getLoadMoreViewPosition());
            }
        }
    }

    public interface RefreshListener{
        void onRefresh();
        void onLoadMore();
    }

    public void setRealControlMoreEnable(boolean mRealControlMoreEnable) {
        this.mRealControlMoreEnable = mRealControlMoreEnable;
    }
}
