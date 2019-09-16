package com.example.demo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.android.vlayout.DelegateAdapter;
import com.alibaba.android.vlayout.LayoutHelper;
import com.alibaba.android.vlayout.layout.GridLayoutHelper;
import com.alibaba.android.vlayout.layout.SingleLayoutHelper;
import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.holder.ViewAdapter;
import com.bigkoo.convenientbanner.listener.OnItemClickListener;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.loadmore.LoadMoreView;
import com.example.demo.R;
import com.facebook.drawee.view.SimpleDraweeView;
import com.framework.common.loading_view.SimpleLoadMoreView;
import com.framework.common.utils.ToastUtil;
import com.framework.model.demo.ActivityBean;

/**
 * @author zhangzhiqiang
 * @date 2019/6/25.
 * description：
 */
public class LoadMoreAdapter extends DelegateAdapter.Adapter <BaseViewHolder>{
    private SimpleLoadMoreView mLoadMoreView;
    private boolean mLoading,mLoadMoreEnable;
    private RequestLoadMoreListener mRequestLoadMoreListener;
    public LoadMoreAdapter(){
        mLoadMoreView = new SimpleLoadMoreView();
    }

    public void setRequestLoadMoreListener(RequestLoadMoreListener requestLoadMoreListener){
        mRequestLoadMoreListener = requestLoadMoreListener;
    }

    @Override
    public LayoutHelper onCreateLayoutHelper() {
        SingleLayoutHelper helper = new SingleLayoutHelper();
        helper.setAspectRatio(75/5.f);
        return helper;
    }

    @NonNull
    @Override
    public BaseViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(mLoadMoreView.getLayoutId(),viewGroup,false);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mLoadMoreView.getLoadMoreStatus() == LoadMoreView.STATUS_FAIL) {
                    notifyLoadMoreToLoading();
                }
            }
        });
        return new MoreViewHolder(view);
    }

    private void notifyLoadMoreToLoading() {
        if (mLoadMoreView.getLoadMoreStatus() == LoadMoreView.STATUS_LOADING) {
            return;
        }
        mLoadMoreView.setLoadMoreStatus(LoadMoreView.STATUS_DEFAULT);
        notifyItemChanged(0);
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder viewHolder, final int i) {
        if(mLoadMoreView.isLoadEndMoreGone()){
            viewHolder.itemView.setVisibility(View.INVISIBLE);
        }else {
            viewHolder.itemView.setVisibility(View.VISIBLE);
        }
        autoLoadMore(viewHolder.itemView);
        mLoadMoreView.convert(viewHolder);
    }

    @Override
    public int getItemViewType(int position) {
        return Constants.TYPE_LOAD_MORE;
    }

    @Override
    public int getItemCount() {
        return 1;
    }

    private void autoLoadMore(View view) {
//        if (getLoadMoreViewCount() == 0) {
//            return;
//        }
//        if (position < getItemCount() - mPreLoadNumber) {
//            return;
//        }
        if(!mLoadMoreEnable){
            mLoadMoreView.setLoadMoreEndGone(true);
            view.setVisibility(View.INVISIBLE);
            return;
        }else {
            mLoadMoreView.setLoadMoreEndGone(false);
            view.setVisibility(View.VISIBLE);
        }

        if (mLoadMoreView.getLoadMoreStatus() != LoadMoreView.STATUS_DEFAULT) {
            return;
        }
        mLoadMoreView.setLoadMoreStatus(LoadMoreView.STATUS_LOADING);
        if (!mLoading) {
            mLoading = true;
            if (view != null) {
                view.post(new Runnable() {
                    @Override
                    public void run() {
                        mRequestLoadMoreListener.onLoadMoreRequested();
                    }
                });
            } else {
                mRequestLoadMoreListener.onLoadMoreRequested();
            }
        }
    }

    public boolean isLoadMoreEnable() {
        return mLoadMoreEnable;
    }

    public void setLoadMoreEnable(boolean mLoadMoreEnable) {
        this.mLoadMoreEnable = mLoadMoreEnable;
    }

    public interface RequestLoadMoreListener {
        void onLoadMoreRequested();
    }

    public void loadMoreEnd(boolean gone) {
//        if (getLoadMoreViewCount() == 0) {
//            return;
//        }
        mLoading = false;
        //mNextLoadEnable = false;
        mLoadMoreView.setLoadMoreEndGone(gone);
        if (gone) {
            notifyItemRemoved(0);
        } else {
            mLoadMoreView.setLoadMoreStatus(LoadMoreView.STATUS_END);
            notifyItemChanged(0);
        }
    }

    /**
     * Refresh complete
     */
    public void loadMoreComplete() {
//        if (getLoadMoreViewCount() == 0) {
//            return;
//        }
        mLoading = false;
        //mNextLoadEnable = true;
        mLoadMoreView.setLoadMoreStatus(LoadMoreView.STATUS_DEFAULT);
        notifyItemChanged(0);
    }

    public void loadMoreFail() {
//        if (getLoadMoreViewCount() == 0) {
//            return;
//        }
        mLoading = false;
        mLoadMoreView.setLoadMoreStatus(LoadMoreView.STATUS_FAIL);
        notifyItemChanged(0);
    }

    private static class MoreViewHolder extends BaseViewHolder {
        private MoreViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
