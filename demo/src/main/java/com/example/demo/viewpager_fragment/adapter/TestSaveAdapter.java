package com.example.demo.viewpager_fragment.adapter;

import androidx.recyclerview.widget.RecyclerView;

import com.example.demo.R;
import com.framework.common.adapter.BaseAdapter;
import com.framework.common.adapter.MyViewHolder;
import com.framework.model.demo.PresellBean;
/**
 * @author zhangzhiqiang
 * @date 2019/5/23.
 * description：
 */
public class TestSaveAdapter extends BaseAdapter<PresellBean.PresellProduct,MyViewHolder>{
    public TestSaveAdapter(RecyclerView recyclerView) {
        super(recyclerView, R.layout.item_test_position);
    }

    @Override
    protected void convert(MyViewHolder helper, PresellBean.PresellProduct item) {
        helper.setImage(R.id.itemIcon,item.getProductAdsPic());
    }
}
