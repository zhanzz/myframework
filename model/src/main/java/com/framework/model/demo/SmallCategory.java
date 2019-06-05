package com.framework.model.demo;

import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.chad.library.adapter.base.entity.SectionEntity;

/**
 * @author zhangzhiqiang
 * @date 2019/6/4.
 * description：
 */
public class SmallCategory extends SectionEntity<SmallCategory> implements MultiItemEntity {
    public String categoryName;
    public String categoryId;
    public SmallCategory() {
        super(false, "");
    }

    @Override
    public int getItemType() {
        return 1;
    }
}
