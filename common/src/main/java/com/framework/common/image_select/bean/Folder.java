package com.framework.common.image_select.bean;

import java.util.List;

/**
 * 文件夹
 * Created by Nereo on 2015/4/7.
 */
public class Folder {
    public String name;
    public String path;
    public Image cover;
    public List<Image> images;

    @Override
    public boolean equals(Object o) {
        try {
            Folder other = null;
            if(null != o) {
                other = (Folder) o;
            }
            if(null != other){
                return this.path.equalsIgnoreCase(other.path);
            }else {
                return false;
            }

        }catch (ClassCastException e){
            e.printStackTrace();
        }
        return super.equals(o);
    }
}
