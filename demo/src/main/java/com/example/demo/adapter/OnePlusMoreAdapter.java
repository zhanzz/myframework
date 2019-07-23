package com.example.demo.adapter;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import com.alibaba.android.vlayout.DelegateAdapter;
import com.alibaba.android.vlayout.LayoutHelper;
import com.alibaba.android.vlayout.VirtualLayoutManager;
import com.alibaba.android.vlayout.layout.OnePlusNLayoutHelper;
import com.facebook.drawee.view.SimpleDraweeView;
import com.framework.common.utils.UIHelper;
import com.framework.model.demo.ActivityBean;
/**
 * @author zhangzhiqiang
 * @date 2019/6/25.
 * description：
 */
public class OnePlusMoreAdapter extends DelegateAdapter.Adapter {
    private ActivityBean.ActivityItemBean mItemBean;
    public OnePlusMoreAdapter(ActivityBean.ActivityItemBean itemBean){
        mItemBean = itemBean;
    }

    @Override
    public LayoutHelper onCreateLayoutHelper() {
        OnePlusNLayoutHelper helper = new OnePlusNLayoutHelper(mItemBean.getAdsList().size());
        helper.setAspectRatio((float) mItemBean.getWidth()/mItemBean.getHeight());
        return helper;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        SimpleDraweeView simpleDraweeView = new SimpleDraweeView(viewGroup.getContext());
        return new ImageViewHolder(simpleDraweeView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        VirtualLayoutManager.LayoutParams params = (VirtualLayoutManager.LayoutParams) viewHolder.itemView.getLayoutParams();
        if(params==null){
            params = new VirtualLayoutManager.LayoutParams(UIHelper.getDisplayWidth(),0);
        }
        params.mAspectRatio = (float) mItemBean.getWidth()/mItemBean.getHeight();
        ((ImageViewHolder)viewHolder).setImageUrl(mItemBean.getAdsList().get(i).getActPicUrl());
    }

    @Override
    public int getItemCount() {
        return mItemBean.getAdsList().size();
    }

    @Override
    public int getItemViewType(int position) {
        return Constants.TYPE_ONE_PULS_MORE;
    }

    static final class ImageViewHolder extends RecyclerView.ViewHolder{

        public ImageViewHolder(@NonNull View itemView) {
            super(itemView);
        }

        public void setImageUrl(String url){
            ((SimpleDraweeView)itemView).setImageURI(url);
        }
    }
}
