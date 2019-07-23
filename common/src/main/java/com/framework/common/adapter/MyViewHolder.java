package com.framework.common.adapter;

import androidx.annotation.IdRes;
import android.view.View;

import com.chad.library.adapter.base.BaseViewHolder;
import com.facebook.drawee.view.SimpleDraweeView;
import com.framework.common.utils.FrescoUtils;

/**
 * @author zhangzhiqiang
 * @date 2019/5/24.
 * description：
 */
public class MyViewHolder extends BaseViewHolder {
    public MyViewHolder(View view) {
        super(view);
    }

    public void setImage(@IdRes int viewId,String url){
        SimpleDraweeView view = getView(viewId);
        FrescoUtils.showThumb(url,view);
    }

    public void setImage(@IdRes int viewId,String url,int width,int height){
        SimpleDraweeView view = getView(viewId);
        FrescoUtils.showThumb(url,view,width,height);
    }
}
