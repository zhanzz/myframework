package com.example.jetpack.nvigation.fragment;

import androidx.recyclerview.widget.RecyclerView;

import com.example.jetpack.R;
import com.framework.common.adapter.BaseAdapter;
import com.framework.common.adapter.MyViewHolder;
import com.framework.model.demo.ProductBean;
/**
 * @author zhangzhiqiang
 * @date 2019/5/23.
 * description：
 */
public class TestSaveAdapter extends BaseAdapter<ProductBean,MyViewHolder>{
    public TestSaveAdapter(RecyclerView recyclerView) {
        super(recyclerView, R.layout.layout_search_product);
    }

    @Override
    protected void convert(MyViewHolder helper,ProductBean item) {
        helper.setImage(R.id.img_good,item.getProductIcon());
        helper.setText(R.id.txt_good_name,item.getProductName());
    }
}
