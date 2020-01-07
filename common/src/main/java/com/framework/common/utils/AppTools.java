package com.framework.common.utils;


import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Environment;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;

import java.io.File;
import java.util.List;

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

    public static boolean isMarshmallowOrHigher() {
        return android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M;
    }

    public static boolean isQOrHigher() {
        return android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q;
    }

    public static void hideSoftKey(Activity activity) {
        if (activity == null || activity.getWindow() == null) {
            return;
        }
        /**隐藏软键盘**/
        View view = activity.getWindow().peekDecorView();
        if (view != null) {
            InputMethodManager inputmanger = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            inputmanger.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    //执行时间170ms
    public static int getApkCode(Context context, String apkPath) {
        if (!new File(apkPath).exists()) {
            return 0;
        }
        PackageManager pm = context.getPackageManager();
        PackageInfo info = pm.getPackageArchiveInfo(apkPath, 0);
        if (info != null && info.packageName.equals(context.getPackageName())) {
            return info.versionCode;
        }
        return 0;
    }

    private static boolean isAppRunningForeground(Context context) {
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> runningAppProcessList = activityManager.getRunningAppProcesses();
        if (ListUtils.isEmpty(runningAppProcessList)) {
            return false;
        } else {
            for (ActivityManager.RunningAppProcessInfo info : runningAppProcessList) {
                if (info.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND
                        && info.processName .equals(context.getApplicationInfo().processName) ) {
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }
}
