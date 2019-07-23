package com.example.demo.adapter;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.alibaba.android.vlayout.DelegateAdapter;
import com.alibaba.android.vlayout.LayoutHelper;
import com.alibaba.android.vlayout.VirtualLayoutManager;
import com.alibaba.android.vlayout.layout.FixLayoutHelper;
import com.alibaba.android.vlayout.layout.ScrollFixLayoutHelper;
import com.example.demo.R;
import com.framework.common.utils.UIHelper;
/**
 * @author zhangzhiqiang
 * @date 2019/7/1.
 * description：
 */
public class ScrollTopAdapter extends DelegateAdapter.Adapter<ScrollTopAdapter.FixImageViewHolder> {
    @Override
    public LayoutHelper onCreateLayoutHelper() {
        ScrollFixLayoutHelper helper = new ScrollFixLayoutHelper(FixLayoutHelper.BOTTOM_RIGHT, UIHelper.dip2px(12), UIHelper.dip2px(16));
        helper.setShowType(ScrollFixLayoutHelper.SHOW_ON_ENTER);
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
            params = new VirtualLayoutManager.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        }
        params.width = ViewGroup.LayoutParams.WRAP_CONTENT;
        params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        viewHolder.itemView.setLayoutParams(params);
        ((ImageView)viewHolder.itemView).setImageResource(R.drawable.ic_scroll_to_top);
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
