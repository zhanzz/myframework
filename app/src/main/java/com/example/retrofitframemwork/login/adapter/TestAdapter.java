package com.example.retrofitframemwork.login.adapter;

import androidx.recyclerview.widget.RecyclerView;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.retrofitframemwork.R;
import com.framework.common.adapter.BaseAdapter;
/**
 * @author zhangzhiqiang
 * @date 2019/5/6.
 * description：
 */
public class TestAdapter extends BaseAdapter<String,BaseViewHolder> {
    public TestAdapter(RecyclerView recyclerView) {
        super(recyclerView,R.layout.item_test_basequick_adapter);
    }


    @Override
    protected void convert(BaseViewHolder helper, String item) {
        helper.setText(R.id.tv_title,item);
    }
}
