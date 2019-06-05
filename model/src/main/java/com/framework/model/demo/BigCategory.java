package com.framework.model.demo;

import com.chad.library.adapter.base.entity.IExpandable;
import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.chad.library.adapter.base.entity.SectionEntity;

import java.util.List;

/**
 * @author zhangzhiqiang
 * @date 2019/6/4.
 * description：
 */
public class BigCategory extends SectionEntity<BigCategory> implements IExpandable, MultiItemEntity {
    protected boolean mExpandable = false;
    public String categoryName;
    public String categoryId;
    public List<SmallCategory> list;
    public BigCategory() {
        super(true, "");
    }

    @Override
    public boolean isExpanded() {
        return mExpandable;
    }

    @Override
    public void setExpanded(boolean expanded) {
        mExpandable = expanded;
    }

    @Override
    public List getSubItems() {
        return list;
    }

    @Override
    public int getLevel() {
        return 0;
    }

    @Override
    public int getItemType() {
        return 0;
    }
}
