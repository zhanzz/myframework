package com.framework.common.utils;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;

import androidx.annotation.DrawableRes;

/**
 * @author zhangzhiqiang
 * @date 2019/11/20.
 * description：目的在于减少xml的数量
 */
public class DrawableUtils {
    public static Drawable generateShape(int solidColor, int conner){
        GradientDrawable drawable = new GradientDrawable();
        drawable.setColor(solidColor);
        drawable.setCornerRadius(conner);
        return drawable;
    }

    public static Drawable generateShapeV2(int solidColor, int width,int height){
        GradientDrawable drawable = new GradientDrawable();
        drawable.setColor(solidColor);
        drawable.setSize(width,height);
        drawable.setBounds(0,0,width,height);
        return drawable;
    }

    public static Drawable genrateStateListDrawable(Context context,@DrawableRes int normal, @DrawableRes int select){
        StateListDrawable stalistDrawable = new StateListDrawable();
        //获取对应的属性值 Android框架自带的属性 attr
        int pressed = android.R.attr.state_pressed;
        int focused = android.R.attr.state_focused;
        int checked = android.R.attr.state_checked;
        int selected = android.R.attr.state_selected;
        //负号表示对应的属性值为false
        stalistDrawable.addState(new int []{checked}, context.getResources().getDrawable(select));
        stalistDrawable.addState(new int []{selected}, context.getResources().getDrawable(select));
        stalistDrawable.addState(new int []{-selected}, context.getResources().getDrawable(normal));
        //没有任何状态时显示的图片，我们给它设置我空集合
        stalistDrawable.addState(new int []{}, context.getResources().getDrawable(normal));
        return stalistDrawable;
    }

    public static Drawable generateShape(int solidColor,float[] radii){
        GradientDrawable drawable = new GradientDrawable();
        drawable.setColor(solidColor);
        drawable.setCornerRadii(radii);
        return drawable;
    }

    public static Drawable makeGradientShape(int[] solidColors, int conner){
        GradientDrawable drawable = new GradientDrawable();
        drawable.setColors(solidColors);
        drawable.setCornerRadius(conner);
        drawable.setGradientType(GradientDrawable.LINEAR_GRADIENT);
        return drawable;
    }
}
