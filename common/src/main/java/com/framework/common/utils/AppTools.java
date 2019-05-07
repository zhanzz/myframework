package com.framework.common.utils;


import android.graphics.Color;
import android.os.Build;

/**
 * @author zhangzhiqiang
 * @date 2019/4/18.
 * description：
 */
public class AppTools {
    public static boolean isLightColor(int color) {
        double darkness = 1 - (0.299 * Color.red(color) + 0.587 * Color.green(color) + 0.114 * Color.blue(color)) / 255;
        if (darkness < 0.5) {
            return true; // It's a light color
        } else {
            return false; // It's a dark color
        }
    }

    public static boolean isMarshmallowOrHigher(){
        return android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M;
    }
}
