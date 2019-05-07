package com.framework.common.utils;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.DisplayMetrics;
import android.view.WindowManager;
import com.framework.common.BaseApplication;
import java.lang.reflect.Field;

public class UIHelper {

    /**
     * 获取系统状态栏的高度
     */
    public static int sStatusHeight = 0;
    public static int getStatusHeight(){
        if(BaseApplication.getApp()==null){
            return 0;
        }
        if(sStatusHeight>0){
            return sStatusHeight;
        }
        int statusHeight = 0;
        if (0 == statusHeight){
            Class<?> localClass;
            try {
                localClass = Class.forName("com.android.internal.R$dimen");
                Object localObject = localClass.newInstance();
                Field field = localClass.getField("status_bar_height");
                int i5 = Integer.parseInt(field.get(localObject).toString());
                statusHeight = BaseApplication.getApp().getResources().getDimensionPixelSize(i5);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if(statusHeight==0){
            DisplayMetrics dis = BaseApplication.getApp().getResources().getDisplayMetrics();
            statusHeight = (int) (25*dis.density);
        }
        sStatusHeight = statusHeight;
        return statusHeight;
    }

    /**
     * @param imgRes
     * @return 从资源里面获取drawable的引用
     */
    public static Drawable getDrawable(Context context,int imgRes){
        if(context==null){
            return null;
        }
        Drawable drawable = context.getResources().getDrawable(imgRes);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        return drawable;
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(float dpValue) {
        if(BaseApplication.getApp()==null){
            return 0;
        }
        float scale = BaseApplication.getApp().getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dip(float pxValue) {
        if (BaseApplication.getApp() == null)
            return 0;
        final float scale = BaseApplication.getApp().getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * 将px值转换为sp值，保证文字大小不变
     */
    public static int px2sp(float pxValue) {
        if (BaseApplication.getApp() == null)
            return 0;
        final float fontScale = BaseApplication.getApp().getResources().getDisplayMetrics().scaledDensity;
        return (int) (pxValue / fontScale + 0.5f);
    }

    /**
     * 将sp值转换为px值，保证文字大小不变
     */
    public static int sp2px(float spValue) {
        if (BaseApplication.getApp() == null)
            return 0;
        final float fontScale = BaseApplication.getApp().getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    /**
     * 获取屏幕宽度
     */
    public static int getDisplayWidth() {
        if (BaseApplication.getApp() == null)
            return 0;
        WindowManager manage = (WindowManager) BaseApplication.getApp().getSystemService(Context.WINDOW_SERVICE);
        return manage.getDefaultDisplay().getWidth();
    }

    /**
     * 获取屏幕高度
     */
    public static int getDisplayHeight() {
        if (BaseApplication.getApp() == null)
            return 0;
        WindowManager manage = (WindowManager) BaseApplication.getApp().getSystemService(Context.WINDOW_SERVICE);
        return manage.getDefaultDisplay().getHeight();
    }

    public static int getDeviceDpi(){
        if (BaseApplication.getApp() == null)
            return 0;
        DisplayMetrics dis= BaseApplication.getApp().getResources().getDisplayMetrics();
        return dis.densityDpi;
    }

    public static String getResolution() {
        StringBuilder builder = new StringBuilder(String.valueOf(getDisplayWidth()));
        builder.append("x").append(String.valueOf(getDisplayHeight()));
        return builder.toString();
    }
}
