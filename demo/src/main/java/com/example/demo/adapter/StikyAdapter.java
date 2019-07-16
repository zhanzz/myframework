package com.example.demo.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.alibaba.android.vlayout.DelegateAdapter;
import com.alibaba.android.vlayout.LayoutHelper;
import com.alibaba.android.vlayout.VirtualLayoutManager;
import com.alibaba.android.vlayout.layout.FixLayoutHelper;
import com.alibaba.android.vlayout.layout.ScrollFixLayoutHelper;
import com.alibaba.android.vlayout.layout.StickyLayoutHelper;
import com.example.demo.R;
import com.framework.common.utils.LogUtil;
import com.framework.common.utils.UIHelper;

/**
 * @author zhangzhiqiang
 * @date 2019/7/1.
 * description：
 */
public class StikyAdapter extends DelegateAdapter.Adapter<StikyAdapter.FixImageViewHolder> {
    @Override
    public LayoutHelper onCreateLayoutHelper() {
        StickyLayoutHelper helper = new StickyLayoutHelper();
        helper.setStickyStart(true);
        helper.setOffset(0);
        return helper;
    }

    @NonNull
    @Override
    public FixImageViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        ImageView iv = new ImageView(viewGroup.getContext());
        return new FixImageViewHolder(iv);
    }

    @Override
    public void onBindViewHolder(@NonNull FixImageViewHolder viewHolder, int i) {
        ViewGroup.LayoutParams params = viewHolder.itemView.getLayoutParams();
        if(params==null){
            params = new VirtualLayoutManager.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        }
        params.width = ViewGroup.LayoutParams.MATCH_PARENT;
        params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        viewHolder.itemView.setLayoutParams(params);
        ((ImageView)viewHolder.itemView).setImageResource(R.drawable.ic_pd_11_activity);
    }

    @Override
    public int getItemViewType(int position) {
        return Constants.TYPE_FIX_IMG;
    }

    @Override
    public int getItemCount() {
        return 1;
    }

    public static final class FixImageViewHolder extends RecyclerView.ViewHolder{

        public FixImageViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
