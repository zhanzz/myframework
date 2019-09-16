package com.example.demo.adapter;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;
import android.view.ViewGroup;
import com.alibaba.android.vlayout.DelegateAdapter;
import com.alibaba.android.vlayout.LayoutHelper;
import com.alibaba.android.vlayout.layout.SingleLayoutHelper;
import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.holder.ViewAdapter;
import com.bigkoo.convenientbanner.listener.OnItemClickListener;
import com.example.demo.R;
import com.example.demo.vlayout.activity.StudyVlayoutActivity;
import com.facebook.drawee.view.SimpleDraweeView;
import com.framework.common.utils.ToastUtil;
import com.framework.model.demo.ActivityBean;
/**
 * @author zhangzhiqiang
 * @date 2019/6/25.
 * description：
 */
public class BannerAdapter extends DelegateAdapter.Adapter {
    private ActivityBean.ActivityItemBean mItemBean;
    private int mCurrentPage;
    public BannerAdapter(ActivityBean.ActivityItemBean bean){
        mItemBean = bean;
    }

    @Override
    public LayoutHelper onCreateLayoutHelper() {
        SingleLayoutHelper helper = new SingleLayoutHelper();
        helper.setAspectRatio((float) mItemBean.getWidth()/mItemBean.getHeight());
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
        final ConvenientBanner banner = (ConvenientBanner) viewHolder.itemView;
        banner.setPages(new ViewAdapter<ActivityBean.ActBean,SimpleDraweeView>() {
            @Override
            public SimpleDraweeView createView(Context context,int position) {
                return new SimpleDraweeView(context);
            }

            @Override
            public void UpdateUI(SimpleDraweeView view, int position, ActivityBean.ActBean data) {
                view.setImageURI(data.getActPicUrl());
            }

            @Override
            public int getItemType(int position) {
                return 0;
            }
        },mItemBean.getAdsList());
        banner.setOnItemClickListener(new OnItemClickListener<ActivityBean.ActBean>() {
            @Override
            public void onItemClick(View view, int position, ActivityBean.ActBean data) {
                ToastUtil.show(banner.getContext(),"position="+position);
            }
        });
        banner.startTurning(5000);
    }

    @Override
    public int getItemViewType(int position) {
        return Constants.TYPE_BANNER;
    }

    @Override
    public void onViewAttachedToWindow(@NonNull RecyclerView.ViewHolder holder) {
        if(holder.itemView instanceof ConvenientBanner){
            ((ConvenientBanner) holder.itemView).startTurning(5000);
            ((ConvenientBanner) holder.itemView).setCurrentItem(mCurrentPage);
        }
    }

    @Override
    public void onViewDetachedFromWindow(@NonNull RecyclerView.ViewHolder holder) {
        if(holder.itemView instanceof ConvenientBanner) {
            ((ConvenientBanner) holder.itemView).stopTurning();
            mCurrentPage = ((ConvenientBanner) holder.itemView).getCurrentItem();
        }
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
