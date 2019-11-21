package com.example.retrofitframemwork.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.framework.common.utils.LogUtil;

/**
 * @author zhangzhiqiang
 * @date 2019/9/17.
 * description：
 */
public class StaticBroadCastReceiver extends BroadcastReceiver{

    @Override
    public void onReceive(Context context, Intent intent) {
        LogUtil.e("zhang","收到了广播"+intent.getData());
    }
}
