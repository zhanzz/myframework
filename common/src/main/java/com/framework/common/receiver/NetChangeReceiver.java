package com.framework.common.receiver;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import com.framework.common.manager.ActivityManager;
/**
 * @author zhangzhiqiang
 * @date 2019/4/19.
 * description：
 */
public class NetChangeReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        Activity activity = ActivityManager.getInstance().getLastActivity();
        if(activity instanceof INetChange){
            INetChange iNetChange = (INetChange) activity;
            if (networkInfo != null && networkInfo.isAvailable()) {
                iNetChange.onNetChange(true);
            } else {
                iNetChange.onNetChange(false);
            }
        }
    }
}
