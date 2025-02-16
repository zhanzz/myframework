package com.framework.common.utils;

import android.content.Context;
import android.text.TextUtils;
import android.widget.Toast;

import androidx.annotation.IdRes;
import androidx.annotation.StringRes;

/**
 * @author zhangzhiqiang
 * @date 2019/4/16.
 * description：
 */
public class ToastUtil {
    private static Toast sToast;

    private static Toast getToast(Context applicationContext) {
        if(sToast!=null){
            sToast.cancel();
        }
        sToast = Toast.makeText(applicationContext,"",Toast.LENGTH_SHORT);
        return sToast;
    }

    public static void show(Context context, String msg){
        if(context==null||TextUtils.isEmpty(msg)){
            return;
        }
        Toast toast = getToast(context.getApplicationContext());
        if(toast!=null){
            toast.setText(msg);
            toast.show();
        }
    }

    public static void show(Context context, @StringRes int resId){
        if(context==null||resId==0){
            return;
        }
        String msg = context.getResources().getString(resId);
        Toast toast = getToast(context.getApplicationContext());
        if(toast!=null){
            toast.setText(msg);
            toast.show();
        }
    }

    public static void show(Context context, String msg, int duration){
        if(context==null||TextUtils.isEmpty(msg)){
            return;
        }
        Toast toast = getToast(context.getApplicationContext());
        if(toast!=null){
            toast.setDuration(duration);
            toast.setText(msg);
            toast.show();
        }
    }

    public static void show(Context context, String msg, int gravity, int duration){
        if(context==null||TextUtils.isEmpty(msg)){
            return;
        }
        Toast toast = getToast(context.getApplicationContext());
        if(toast!=null){
            toast.setGravity(gravity,0,0);
            toast.setDuration(duration);
            toast.setText(msg);
            toast.show();
        }
    }
}
