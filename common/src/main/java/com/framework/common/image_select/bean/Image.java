package com.framework.common.image_select.bean;

import android.net.Uri;

import com.chad.library.adapter.base.entity.MultiItemEntity;

/**
 * 图片实体
 * Created by Nereo on 2015/4/7.
 */
public class Image implements MultiItemEntity {
    public static final int TYPE_NORMAL = 0,TYPE_CAMERA=1;
    public String path;
    public Uri uri;
    public String name;
    public long time;
    public int itemType = TYPE_NORMAL;//视图类型

    public Image(String path, String name, long time){
        this.path = path;
        this.name = name;
        this.time = time;
    }

    public Image() {
        path="";
    }

    @Override
    public boolean equals(Object o) {
        try {
            Image other = null;
            if(null != o) {
                other = (Image) o;
            }
            if(null != other) {
                return this.path.equalsIgnoreCase(other.path);
            }else{
                return false;
            }
        }catch (ClassCastException e){
            e.printStackTrace();
        }
        return super.equals(o);
    }

    @Override
    public int getItemType() {
        return itemType;
    }
}
