package com.framework.common.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import com.framework.common.manager.LocalBroadcastActionManager;
/**
 * @author zhangzhiqiang
 * @date 2019/4/19.
 * description：
 */
public class NetChangeReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            boolean hasNet;
            if (networkInfo != null && networkInfo.isAvailable()) {
                hasNet = true;
            } else {
                hasNet = false;
            }
            Intent nIntent = new Intent(LocalBroadcastActionManager.ACTION_NETWORK_CHANGE);
            nIntent.putExtra("hasNet", hasNet);
            LocalBroadcastManager.getInstance(context).sendBroadcastSync(nIntent);
        }
    }
}
