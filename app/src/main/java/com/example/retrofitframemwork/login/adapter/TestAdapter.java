package com.example.retrofitframemwork.login.adapter;

import android.support.v7.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.retrofitframemwork.R;
import com.framework.common.adapter.CommonAdapter;
import com.framework.common.adapter.ViewHolder;
import com.framework.common.base_mvp.BaseAdapter;

/**
 * @author zhangzhiqiang
 * @date 2019/5/6.
 * description：
 */
public class TestAdapter extends CommonAdapter<String> {
    public TestAdapter(RecyclerView recyclerView) {
        super(R.layout.item_test_basequick_adapter);
    }

    @Override
    public void convert(ViewHolder holder, String s, int position) {
        holder.setText(R.id.tv_title,s);
    }
}
