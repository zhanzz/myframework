package com.example.demo.coordinator_layout.adapter;

import android.view.Gravity;
import android.view.TextureView;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseViewHolder;
import com.framework.common.adapter.BaseAdapter;
import com.framework.common.utils.UIHelper;

/**
 * @author zhangzhiqiang
 * @date 2020/1/16.
 * description：
 */
public class TestBehaviorAdapter extends BaseAdapter<String, BaseViewHolder> {

    public TestBehaviorAdapter(RecyclerView recyclerView) {
        super(recyclerView);
    }

    @Override
    protected BaseViewHolder onCreateDefViewHolder(ViewGroup parent, int viewType) {
        TextView tv = new TextView(mContext);
        tv.setTextSize(16);
        tv.setTextColor(0xff333333);
        tv.setGravity(Gravity.CENTER);
        RecyclerView.LayoutParams params = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, UIHelper.dip2px(60));
        tv.setLayoutParams(params);
        return new BaseViewHolder(tv);
    }

    @Override
    protected void convert(BaseViewHolder helper, String item) {
        ((TextView)helper.itemView).setText(item);
    }
}
