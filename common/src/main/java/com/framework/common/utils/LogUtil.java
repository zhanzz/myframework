package com.framework.common.utils;

import android.util.Log;

import com.framework.common.BuildConfig;

/**
 * @author zhangzhiqiang
 * @date 2019/5/6.
 * description：
 */
public class LogUtil {
    private static String TAG = "Log";
    private static boolean isShow = BuildConfig.DEBUG_ENVIRONMENT;

    public static void v(String msg){
        if(isShow){
            Log.v(TAG,msg);
        }
    }

    public static void d(String msg){
        if(isShow){
            Log.d(TAG,msg);
        }
    }

    public static void e(String msg){
        if(isShow){
            Log.e(TAG,msg);
        }
    }

    public static void i(String msg){
        if(isShow){
            Log.i(TAG,msg);
        }
    }
}
