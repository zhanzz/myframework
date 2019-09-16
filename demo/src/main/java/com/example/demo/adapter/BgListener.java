package com.example.demo.adapter;

import android.graphics.drawable.Animatable;
import android.view.View;
import com.alibaba.android.vlayout.layout.BaseLayoutHelper;
import com.example.demo.R;
import com.facebook.drawee.controller.BaseControllerListener;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.image.ImageInfo;
import com.framework.common.utils.FrescoUtils;
/**
 * @author zhangzhiqiang
 * @date 2019/9/6.
 * description：
 */
public class BgListener extends BaseLayoutHelper.DefaultLayoutViewHelper {
    private String mUrl;
    private int mWidth,mHeight;
    public BgListener(String url,int width,int height) {
        super(null,null);
        mUrl = url;
        mWidth = width;
        mHeight = height;
    }

    @Override
    public void onBind(final View layoutView, BaseLayoutHelper baseLayoutHelper) {
        if (layoutView.getTag(R.id.tag_layout_helper_bg) == null) {
            if (layoutView instanceof SimpleDraweeView) {
                SimpleDraweeView simpleDraweeView = (SimpleDraweeView) layoutView;
                FrescoUtils.showImage(mUrl,simpleDraweeView,mWidth,mHeight,new BaseControllerListener<ImageInfo>(){
                    @Override
                    public void onFinalImageSet(String id,ImageInfo imageInfo, Animatable animatable) {
                        onBindViewSuccess(layoutView,mUrl);
                    }
                });
            }
        }
    }

    @Override
    public void onUnbind(final View layoutView, BaseLayoutHelper baseLayoutHelper) {
        layoutView.setTag(R.id.tag_layout_helper_bg, null);
    }

    @Override
    public void onBindViewSuccess(View layoutView, String id) {
        layoutView.setTag(R.id.tag_layout_helper_bg, id);
    }
}
