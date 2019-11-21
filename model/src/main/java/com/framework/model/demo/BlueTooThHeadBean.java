package com.framework.model.demo;

import com.chad.library.adapter.base.entity.IExpandable;
import com.chad.library.adapter.base.entity.MultiItemEntity;

import java.util.List;

/**
 * @author zhangzhiqiang
 * @date 2019/9/18.
 * description：
 */
public class BlueTooThHeadBean implements MultiItemEntity , IExpandable<BlueTooThBean> {
    public String name;
    public List<BlueTooThBean> subItems;
    @Override
    public int getItemType() {
        return 0;
    }

    private boolean isExpand;
    @Override
    public boolean isExpanded() {
        return isExpand;
    }

    @Override
    public void setExpanded(boolean expanded) {
        isExpand = expanded;
    }

    @Override
    public List<BlueTooThBean> getSubItems() {
        return subItems;
    }

    @Override
    public int getLevel() {
        return 0;
    }
}
