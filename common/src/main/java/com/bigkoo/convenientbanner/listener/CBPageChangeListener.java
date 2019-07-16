package com.bigkoo.convenientbanner.listener;

import android.graphics.drawable.Drawable;
import android.support.v4.view.ViewPager;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.ArrayList;

/**
 * Created by Sai on 15/7/29.
 * 翻页指示器适配器
 */
public class CBPageChangeListener implements ViewPager.OnPageChangeListener {
    private ViewGroup mIndicatorContainer;
    private Drawable[] mPageDrawable;//0是未选中的drawable
    public CBPageChangeListener(ViewGroup indicatorContainer, Drawable pageDrawable[]){
        this.mIndicatorContainer=indicatorContainer;
        this.mPageDrawable = pageDrawable;
    }
    @Override
    public void onPageScrollStateChanged(int state) {
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    @Override
    public void onPageSelected(int index) {
        for (int i = 0; i < mIndicatorContainer.getChildCount(); i++) {
            if(i==index){
                mIndicatorContainer.getChildAt(i).setBackground(mPageDrawable[1]);
            }else {
                mIndicatorContainer.getChildAt(i).setBackground(mPageDrawable[0]);
            }
        }
    }

    public void setIndicatorContainer(ViewGroup mIndicatorContainer) {
        this.mIndicatorContainer = mIndicatorContainer;
    }

    public void setPageDrawable(Drawable[] mPageDrawable) {
        this.mPageDrawable = mPageDrawable;
    }
}
