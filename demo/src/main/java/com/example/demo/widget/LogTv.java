package com.example.demo.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.framework.common.utils.LogUtil;

/**
 * @author zhangzhiqiang
 * @date 2019/9/7.
 * description：
 */
public class LogTv extends TextView {
    public LogTv(Context context) {
        super(context);
    }

    public LogTv(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        int count = attrs.getAttributeCount();
        for(int i=0;i<count;i++){
            LogUtil.e("zhang","name="+attrs.getAttributeName(i)+";value="+attrs.getAttributeValue(i));
        }
    }

    public LogTv(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public LogTv(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    int position;
    public void setPosition(int position){
        this.position = position;
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        LogUtil.e("life","onDetachedFromWindow"+toString());
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        LogUtil.e("life","onAttachedToWindow"+toString());
    }

    @Override
    public void onStartTemporaryDetach() {
        super.onStartTemporaryDetach();
        LogUtil.e("life","onStartTemporaryDetach"+toString());
    }

    @Override
    public void onFinishTemporaryDetach() {
        super.onFinishTemporaryDetach();
        LogUtil.e("life","onFinishTemporaryDetach"+toString());
    }
}
