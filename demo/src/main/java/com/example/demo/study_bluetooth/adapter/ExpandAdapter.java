package com.example.demo.study_bluetooth.adapter;

import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseSectionQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.entity.IExpandable;
import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.chad.library.adapter.base.entity.SectionEntity;
import com.example.demo.R;
import com.framework.common.adapter.BaseMultiItemQuickAdapter;
import com.framework.model.demo.BigCategory;
import com.framework.model.demo.BlueTooThBean;
import com.framework.model.demo.BlueTooThHeadBean;
import com.framework.model.demo.SmallCategory;

import java.util.List;

/**
 * @author zhangzhiqiang
 * @date 2019/6/5.
 * description：
 */
public class ExpandAdapter extends BaseMultiItemQuickAdapter<MultiItemEntity, BaseViewHolder> {
    public ExpandAdapter(RecyclerView recyclerView) {
        super(recyclerView);
        addItemType(0, R.layout.item_section_head);
        addItemType(1, R.layout.item_section_content);
    }

    @Override
    protected boolean isFixedViewType(int type) {
        return super.isFixedViewType(type) || type==0;
    }

    @Override
    protected void convert(final BaseViewHolder helper, MultiItemEntity item) {
        switch (item.getItemType()){
            case 0:
                helper.setText(R.id.textView,((BlueTooThHeadBean)item).name);
//                helper.itemView.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        int pos = helper.getAdapterPosition();
//                        IExpandable expandable = (IExpandable) getItem(pos);
//                        if (expandable.isExpanded()) {
//                            collapse(pos);
//                        } else {
//                            expand(pos);
//                        }
//                    }});
                break;
            case 1:
                BlueTooThBean bean = (BlueTooThBean)item;
                helper.setText(R.id.textView, TextUtils.isEmpty(bean.name)
                        ? bean.address:bean.name);
                break;
        }
    }

    @Override
    public void setHasStableIds(boolean hasStableIds) {
        super.setHasStableIds(hasStableIds);
    }
}
