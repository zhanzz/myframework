package com.example.demo.viewpager_fragment.adapter;

import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseSectionQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.entity.IExpandable;
import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.chad.library.adapter.base.entity.SectionEntity;
import com.example.demo.R;
import com.framework.model.demo.BigCategory;
import com.framework.model.demo.SmallCategory;

import java.util.List;

/**
 * @author zhangzhiqiang
 * @date 2019/6/5.
 * description：
 */
public class ExpandAdapter extends BaseMultiItemQuickAdapter<MultiItemEntity, BaseViewHolder> {
    public ExpandAdapter(List<MultiItemEntity> data) {
        super(data);
        addItemType(0, R.layout.item_section_head);
        addItemType(1, R.layout.item_section_content);
    }

    //占整行
    @Override
    protected boolean isFixedViewType(int type) {
        return super.isFixedViewType(type) || type==0;
    }

    @Override
    protected BaseViewHolder onCreateDefViewHolder(ViewGroup parent, int viewType) {
        BaseViewHolder viewHolder = super.onCreateDefViewHolder(parent, viewType);
        Log.e("zhang","onCreateDefViewHolder");
        return viewHolder;
    }

    @Override
    protected void convert(final BaseViewHolder helper, MultiItemEntity item) {
        switch (item.getItemType()){
            case 0:
                Log.e("zhang","bind"+((BigCategory)item).categoryName);
                helper.setText(R.id.textView,((BigCategory)item).categoryName);
                helper.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int pos = helper.getAdapterPosition();
                        IExpandable expandable = (IExpandable) getItem(pos);
                        if (expandable.isExpanded()) {
                            collapse(pos);
                        } else {
                            expand(pos);
                        }
                    }});
                break;
            case 1:
                helper.setText(R.id.textView,((SmallCategory)item).categoryName);
                break;
        }
    }
}
