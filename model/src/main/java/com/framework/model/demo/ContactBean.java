package com.framework.model.demo;

import android.text.TextUtils;

import com.chad.library.adapter.base.entity.MultiItemEntity;

/**
 * @author zhangzhiqiang
 * @date 2019/8/3.
 * description：
 */
public class ContactBean implements Comparable<ContactBean>, MultiItemEntity {
    public static final int TYPE_HEAD = 1,TYPE_NORMAL=0;
    public String phone;
    public String name;
    public char category;//首字母
    public int itemType = TYPE_NORMAL;
    public ContactBean(){

    }

    public ContactBean(String phone, String name,char category) {
        this.phone = phone;
        this.name = name;
        this.category = category;
    }

    @Override
    public int compareTo(ContactBean o) {
        if(category=='#'){
            return 1; //认为#是最小的
        }else if(o.category=='#'){
            return -1;
        }
        return category >= o.category ? 1:-1;
    }

    @Override
    public int getItemType() {
        return itemType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ContactBean that = (ContactBean) o;

        return category == that.category;
    }

    @Override
    public int hashCode() {
        return (int) category;
    }
}
