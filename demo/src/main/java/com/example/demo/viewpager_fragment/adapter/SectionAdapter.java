package com.example.demo.viewpager_fragment.adapter;

import com.chad.library.adapter.base.BaseSectionQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
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
public class SectionAdapter extends BaseSectionQuickAdapter<SectionEntity, BaseViewHolder> {

    public SectionAdapter(List<SectionEntity> data) {
        super(R.layout.item_section_content, R.layout.item_section_content, data);
    }

    @Override
    protected void convertHead(BaseViewHolder helper, SectionEntity item) {
        helper.setText(R.id.textView,((BigCategory)item).categoryName);
    }

    @Override
    protected void convert(BaseViewHolder helper, SectionEntity item) {
        helper.setText(R.id.textView,((SmallCategory)item).categoryName);
    }
}
