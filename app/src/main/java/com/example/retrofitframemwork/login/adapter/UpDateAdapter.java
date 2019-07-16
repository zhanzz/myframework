package com.example.retrofitframemwork.login.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.retrofitframemwork.R;

import java.util.List;

/**
 * @author zhangzhiqiang
 * @date 2019/7/11.
 * description：
 */
public class UpDateAdapter extends BaseQuickAdapter<String, BaseViewHolder> {
    public UpDateAdapter(List<String> data) {
        super(R.layout.item_version_updata_list,data);
    }

    @Override
    protected void convert(BaseViewHolder helper, String item) {
        helper.setText(R.id.tv_order_number, helper.getAdapterPosition() + 1 + ".");
        helper.setText(R.id.tv_new_zone_content, item);
    }
}
