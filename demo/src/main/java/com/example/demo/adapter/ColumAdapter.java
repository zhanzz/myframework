package com.example.demo.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.alibaba.android.vlayout.DelegateAdapter;
import com.alibaba.android.vlayout.LayoutHelper;
import com.alibaba.android.vlayout.layout.ColumnLayoutHelper;
import com.facebook.drawee.view.SimpleDraweeView;
import com.framework.model.demo.ActivityBean;

/**
 * @author zhangzhiqiang
 * @date 2019/6/25.
 * description：
 */
public class ColumAdapter extends DelegateAdapter.Adapter<OnePlusMoreAdapter.ImageViewHolder>{
    private ActivityBean.ActivityItemBean mItemBean;

    public ColumAdapter(ActivityBean.ActivityItemBean itemBean){
        mItemBean = itemBean;
    }

    @Override
    public LayoutHelper onCreateLayoutHelper() {
        ColumnLayoutHelper helper = new ColumnLayoutHelper();
        helper.setItemCount(mItemBean.getAdsList().size());
        helper.setAspectRatio((float) mItemBean.getWidth()/mItemBean.getHeight());
        return helper;
    }

    @NonNull
    @Override
    public OnePlusMoreAdapter.ImageViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        SimpleDraweeView simpleDraweeView = new SimpleDraweeView(viewGroup.getContext());
        return new OnePlusMoreAdapter.ImageViewHolder(simpleDraweeView);
    }

    @Override
    public void onBindViewHolder(@NonNull OnePlusMoreAdapter.ImageViewHolder viewHolder, int i) {
        viewHolder.setImageUrl(mItemBean.getAdsList().get(i).getActPicUrl());
    }

    @Override
    public int getItemCount() {
        return mItemBean.getAdsList().size();
    }

    @Override
    public int getItemViewType(int position) {
        return Constants.TYPE_COLUM;
    }
}
