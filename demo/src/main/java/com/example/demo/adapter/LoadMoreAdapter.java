package com.example.demo.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.android.vlayout.DelegateAdapter;
import com.alibaba.android.vlayout.LayoutHelper;
import com.alibaba.android.vlayout.layout.SingleLayoutHelper;
import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.holder.ViewAdapter;
import com.bigkoo.convenientbanner.listener.OnItemClickListener;
import com.example.demo.R;
import com.facebook.drawee.view.SimpleDraweeView;
import com.framework.common.utils.ToastUtil;
import com.framework.model.demo.ActivityBean;

/**
 * @author zhangzhiqiang
 * @date 2019/6/25.
 * description：
 */
public class LoadMoreAdapter extends DelegateAdapter.Adapter {

    public LoadMoreAdapter(){

    }

    @Override
    public LayoutHelper onCreateLayoutHelper() {
        SingleLayoutHelper helper = new SingleLayoutHelper();
        helper.setAspectRatio(50/750.f);
        return helper;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        ConvenientBanner banner = new ConvenientBanner(viewGroup.getContext());
        banner.setCanLoop(true);
        banner.setPageIndicator(R.drawable.item_select_indicator,R.drawable.item_un_select_indicator);
        return new BannerViewHolder(banner);
    }

    @Override
    public void onViewRecycled(@NonNull RecyclerView.ViewHolder holder) {
        if(holder.itemView instanceof ConvenientBanner){
            ((ConvenientBanner) holder.itemView).clear();
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, final int i) {

    }

    @Override
    public int getItemViewType(int position) {
        return Constants.TYPE_BANNER;
    }

    @Override
    public void onViewAttachedToWindow(@NonNull RecyclerView.ViewHolder holder) {

    }

    @Override
    public void onViewDetachedFromWindow(@NonNull RecyclerView.ViewHolder holder) {

    }

    @Override
    public int getItemCount() {
        return 1;
    }

    public static class BannerViewHolder extends RecyclerView.ViewHolder{
        public BannerViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
