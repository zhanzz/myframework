package com.example.demo.service;

import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.view.WindowManager;

import com.example.demo.R;
import com.framework.common.utils.LogUtil;

/**
 * @author zhangzhiqiang
 * @date 2019/7/5.
 * description：
 */
public class ForegroundService extends Service {

    /**
     * id不可设置为0,否则不能设置为前台service
     */
    private static final int NOTIFICATION_DOWNLOAD_PROGRESS_ID = 0x0001;
    private static final int NOTIFICATION_DOWNLOAD_PROGRESS_ID2 = 0x0002;
    private boolean isRemove=false;//是否需要移除

    /**
     * Notification
     */
    public void createNotification(){
        //NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this,"");
        //使用兼容版本
        NotificationCompat.Builder builder=new NotificationCompat.Builder(this,"");
        //设置状态栏的通知图标
        builder.setSmallIcon(R.drawable.load_more_quan);
        //设置通知栏横条的图标
        //禁止用户点击删除按钮删除
        builder.setAutoCancel(false);
        //禁止滑动删除
        builder.setOngoing(true);
        //设置通知栏的标题内容
        builder.setContentTitle("I am Foreground Service!!!");
        //创建通知
        Notification notification = builder.build();

        //设置为前台服务
        startForeground(NOTIFICATION_DOWNLOAD_PROGRESS_ID,notification);
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        int i=intent.getExtras().getInt("cmd");
        if(i==0){
            if(!isRemove) {
                createNotification();
            }
            isRemove=true;
        }else {
            //移除前台服务
            if (isRemove) {
                stopForeground(true);
                NotificationCompat.Builder builder=new NotificationCompat.Builder(this,"");
                //设置状态栏的通知图标
                builder.setSmallIcon(R.drawable.load_more_quan);
                //设置通知栏横条的图标
                //禁止用户点击删除按钮删除
                builder.setAutoCancel(false);
                //禁止滑动删除
                builder.setOngoing(true);
                //设置通知栏的标题内容
                builder.setContentTitle("I am Foreground Service!!!");
                //创建通知
                Notification notification = builder.build();
                NotificationManager mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                mNotificationManager.notify(NOTIFICATION_DOWNLOAD_PROGRESS_ID2,builder.build());
                stopSelf();
            }
            isRemove=false;
        }
        //showDialog();

        return super.onStartCommand(intent, flags, startId);
    }

    private void showDialog() {//按home键之后，显示在了桌面上
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("应用更新")
                .setMessage("有版本更新了")
                .create();
        dialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
        dialog.show();
    }

    @Override
    public void onDestroy() {
        LogUtil.e("服务退出了");
        //移除前台服务
        //if (isRemove) {
            //stopForeground(false);
        //}
        isRemove=false;
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
