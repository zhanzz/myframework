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
import com.alibaba.android.vlayout.layout.FloatLayoutHelper;
import com.example.demo.R;
import com.framework.common.utils.UIHelper;

/**
 * @author zhangzhiqiang
 * @date 2019/7/1.
 * description：
 */
public class FloatAdapter extends DelegateAdapter.Adapter<FloatAdapter.FixImageViewHolder> {
    @Override
    public LayoutHelper onCreateLayoutHelper() {
        FloatLayoutHelper helper = new FloatLayoutHelper();
        helper.setAlignType(FixLayoutHelper.BOTTOM_RIGHT);
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
        ((ImageView)viewHolder.itemView).setImageResource(R.drawable.ic_accumulate_red_packet);
        VirtualLayoutManager.LayoutParams params = (VirtualLayoutManager.LayoutParams) viewHolder.itemView.getLayoutParams();
        if(params==null){
            params = new VirtualLayoutManager.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        }
        params.leftMargin = params.rightMargin = UIHelper.dip2px(12);
        params.bottomMargin = UIHelper.dip2px(80);
        viewHolder.itemView.setLayoutParams(params);
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
