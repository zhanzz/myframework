package com.example.demo.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.alibaba.android.vlayout.DelegateAdapter;
import com.alibaba.android.vlayout.LayoutHelper;
import com.alibaba.android.vlayout.layout.SingleLayoutHelper;
import com.example.demo.widget.MoveRecyclerView;
import com.framework.common.utils.UIHelper;
import com.framework.model.demo.ActivityBean;

import java.util.List;

/**
 * @author zhangzhiqiang
 * @date 2019/6/26.
 * description：
 */
public class ScrollActAdapter extends DelegateAdapter.Adapter<ScrollActAdapter.RecyclerViewViewHolder>{
    private ActivityBean.ActivityItemBean mItemBean;
    private int parentViewHeight;
    private RecyclerView.RecycledViewPool mPool;
    private int lastOffset;
    private int lastPosition;

    public ScrollActAdapter(RecyclerView.RecycledViewPool pool,ActivityBean.ActivityItemBean itemBean){
        mItemBean = itemBean;
        float rate = mItemBean.getHeight() / (float) mItemBean.getWidth();
        parentViewHeight = (int) (UIHelper.getDisplayWidth() * rate);
        mPool = pool;
        List list = mItemBean.getAdsList();
        //添加更多
        if("1".equals(mItemBean.getIsShowMore())&&mItemBean.getAdsList().size()>= mItemBean.getMaxShowNum()){
            ActivityBean.ActBean bean = new ActivityBean.ActBean();
            list.add(bean);
        }
    }

    @Override
    public LayoutHelper onCreateLayoutHelper() {
        SingleLayoutHelper helper = new SingleLayoutHelper();
        helper.setAspectRatio((float) mItemBean.getWidth()/mItemBean.getHeight());
        return helper;
    }

    @NonNull
    @Override
    public RecyclerViewViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        MoveRecyclerView recyclerView = new MoveRecyclerView(viewGroup.getContext());
        LinearLayoutManager layoutManager = new LinearLayoutManager(recyclerView.getContext());
        layoutManager.setRecycleChildrenOnDetach(true);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setRecycledViewPool(mPool);
        layoutManager.setInitialPrefetchItemCount((int)Math.ceil(mItemBean.getMaxShowNum()));
        WareActAdapter adapter = new WareActAdapter(recyclerView.getContext(),mItemBean, parentViewHeight);
        recyclerView.setAdapter(adapter);
        return new RecyclerViewViewHolder(recyclerView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewViewHolder viewHolder, int i) {
        MoveRecyclerView recyclerView = (MoveRecyclerView) viewHolder.itemView;

        WareActAdapter adapter = (WareActAdapter) recyclerView.getAdapter();
        if("1".equals(mItemBean.getIsShowMore())&&mItemBean.getAdsList().size()>= mItemBean.getMaxShowNum()){
            recyclerView.setmIsNeedMore(true);
        }else{
            recyclerView.setmIsNeedMore(false);
        }
        adapter.setNewData(mItemBean,parentViewHeight);
    }

    @Override
    public void onViewDetachedFromWindow(@NonNull RecyclerViewViewHolder holder) {
        if(holder.itemView instanceof RecyclerView){
            LinearLayoutManager layoutManager = ((LinearLayoutManager)((RecyclerView) holder.itemView).getLayoutManager());
            //获取可视的第一个view
            View topView = layoutManager.getChildAt(0);
            if(topView != null) {
                //获取与该view的顶部的偏移量
                lastOffset = topView.getLeft();
                //得到该View的数组位置
                lastPosition = layoutManager.getPosition(topView);
            }
        }
    }

    @Override
    public void onViewAttachedToWindow(@NonNull RecyclerViewViewHolder holder) {
        if (holder.itemView instanceof RecyclerView) {
            RecyclerView recyclerView = ((RecyclerView) holder.itemView);
            LinearLayoutManager manager = (LinearLayoutManager) recyclerView.getLayoutManager();
            manager.scrollToPositionWithOffset(lastPosition, lastOffset);
        }
    }

    @Override
    public int getItemCount() {
        return 1;
    }

    @Override
    public int getItemViewType(int position) {
        return Constants.TYPE_SCROLL_ACT;
    }

    static final class RecyclerViewViewHolder extends RecyclerView.ViewHolder{

        public RecyclerViewViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
