package com.example.retrofitframemwork.login.service;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.net.Uri;
import android.os.Binder;
import android.os.Build;
import android.os.Environment;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;
import android.view.View;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.example.retrofitframemwork.R;
import com.framework.common.BaseApplication;
import com.framework.common.callBack.FileCallBack;
import com.framework.common.manager.PermissionManager;
import com.framework.common.net.RxNet;
import com.framework.common.utils.AppTools;
import com.framework.common.utils.LogUtil;

import java.io.File;

import io.reactivex.disposables.Disposable;

public class UpdateService extends Service {
    public final static String EXTRA_URL="extra_url",EXTRA_DIRECTORY="extra_directory",EXTRA_FILE_NAME="extra_file_name";
    private NotificationCompat.Builder mBuilder;
    /**
     * Notification管理
     */
    private NotificationManager mNotificationManager;
    /**
     * Notification的ID
     */
    int mNotifyId = 102,mInstallNotifyId=103;
    /**
     * Notification的进度条数值
     */
    int mProgress = 0,mOldProgress=0;
    private File mFile;
    RemoteViews mRemoteViews;
    private ProgressListener mListener;
    /**
     * 通知栏按钮点击事件对应的ACTION
     */
    public final static String ACTION_BUTTON = "com.notifications.intent.action.ButtonClick.com.framework";
    String url,directory,fileName;
    /**
     * 通知栏按钮广播
     */
    public ButtonBroadcastReceiver bReceiver;
    private boolean isStart;
    private Disposable mDisposable;
    private LocalBinder binder = new LocalBinder();
    @Override
    public void onCreate() {
        super.onCreate();
        mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        mRemoteViews = new RemoteViews(getPackageName(), R.layout.view_custom_progress);
        mRemoteViews.setImageViewResource(R.id.custom_progress_icon, R.mipmap.ic_launcher);
        mRemoteViews.setTextViewText(R.id.tv_custom_progress_title, getString(R.string.app_name));
        initButtonReceiver();
        //点击的事件处理
        Intent buttonIntent = new Intent(ACTION_BUTTON);
        //这里加了广播，所及INTENT的必须用getBroadcast方法
        PendingIntent intent_prev = PendingIntent.getBroadcast(this, 1, buttonIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        mRemoteViews.setOnClickPendingIntent(R.id.btn_reload, intent_prev);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    /**
     * 带按钮的通知栏点击广播接收
     */
    public void initButtonReceiver() {
        bReceiver = new ButtonBroadcastReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ACTION_BUTTON);
        registerReceiver(bReceiver, intentFilter);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (null != intent && intent.hasExtra(EXTRA_URL)
                && intent.hasExtra(EXTRA_DIRECTORY)
                && intent.hasExtra(EXTRA_FILE_NAME) && !isStart) {
            LogUtil.e("onStartCommand="+this);
            isStart = true;
            url = intent.getStringExtra(EXTRA_URL);
            directory = intent.getStringExtra(EXTRA_DIRECTORY);
            fileName = intent.getStringExtra(EXTRA_FILE_NAME);
            if (TextUtils.isEmpty(url) || TextUtils.isEmpty(directory) || TextUtils.isEmpty(fileName)) {
                Toast.makeText(this, "更新失败！", Toast.LENGTH_SHORT).show();
                return super.onStartCommand(intent, flags, startId);
            }
            File fdirectory = null;
            fdirectory = new File(directory);
            if (!fdirectory.exists()) {
                fdirectory.mkdirs();
            }
            mFile = new File(fdirectory, fileName);
            if (mFile.exists()) {
                mFile.delete();
            }
            initNotification();
            mBuilder.setTicker(getString(R.string.app_name) +"版本更新"); //通知首次出现在通知栏，带上升动画效果的
            startForeground(mNotifyId,mBuilder.build());//置为前台服务，保活
            mBuilder.setTicker(null);
            downLoadAPK();
        }
        return START_REDELIVER_INTENT;
    }

    private void initNotification() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            String channelId = getPackageName() + "update";//通知渠道id
            String channelName = "版本更新通知";//"PUSH_NOTIFY_NAME";//通知渠道名
            int importance = NotificationManager.IMPORTANCE_HIGH;//通知级别
            NotificationChannel channel = new NotificationChannel(channelId, channelName, importance);//
            channel.enableLights(true);//设置指示灯
            channel.setLightColor(Color.GREEN);
            channel.enableVibration(false);//设置通知不震动
            channel.setVibrationPattern(new long[]{0});
            channel.setShowBadge(true);
            channel.setDescription("应用有新版本时通知");
            if (notificationManager != null) {
                notificationManager.createNotificationChannel(channel);
            }
            mBuilder = new NotificationCompat.Builder(this,channelId);
        }else {
            mBuilder = new NotificationCompat.Builder(this,null);
        }
        mBuilder.setWhen(System.currentTimeMillis())// 通知产生的时间，会在通知信息里显示
                .setVibrate(new long[]{0})//设置通知不震动
                .setContentIntent(getDefalutIntent(PendingIntent.FLAG_UPDATE_CURRENT))
                .setPriority(Notification.PRIORITY_DEFAULT)// 设置该通知优先级
                .setOngoing(true)// ture，设置他为一个正在进行的通知。他们通常是用来表示一个后台任务,用户积极参与(如播放音乐)或以某种方式正在等待,因此占用设备(如一个文件下载,同步操作,主动网络连接)
                .setSmallIcon(R.mipmap.ic_launcher);
    }

    /**
     * 下载文件
     */
    private void downLoadAPK() {
        mDisposable = RxNet.downLoadFile(url, new FileCallBack(directory,fileName) {
            @Override
            public void onFile(File file) {
               /*********下载完成，点击安装***********/
                mProgress = 100;
                if(mListener!=null){
                    mListener.onProgress(mProgress);
                }
                if (mFile == null || mFile.length() == 0) {
                    return;
                }
                installApk();
                stopForeground(true);
                showCustomProgressNotify("下载完成,点击安装", true, false);
                stopSelf();
            }

            @Override
            public void onFail(Throwable e) {
                showCustomProgressNotify("下载失败,请重试", false, true);
            }

            @Override
            public void inProgress(float progress, long total) {
                mProgress = (int) (progress * 100);
                if(mProgress-mOldProgress>=1){//控制一下更新频率
                    mOldProgress = mProgress;
                    if(mListener!=null){
                        mListener.onProgress(mProgress);
                    }
                    showCustomProgressNotify("下载中...", false, false);
                }
            }
        });
        showCustomProgressNotify("下载中...", false, false);
    }

    /**
     * @获取默认的pendingIntent,为了防止2.3及以下版本报错
     * @flags属性: 在顶部常驻:Notification.FLAG_ONGOING_EVENT
     * 点击去除： Notification.FLAG_AUTO_CANCEL
     */
    public PendingIntent getDefalutIntent(int flags) {
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 1, new Intent(), flags);
        return pendingIntent;
    }

    /**
     * 显示自定义的带进度条通知栏
     */
    private void showCustomProgressNotify(String status, boolean isInstall, boolean isFail) {
        mRemoteViews.setTextViewText(R.id.tv_custom_progress_status, status);
        if (mProgress >= 100) {
            mRemoteViews.setProgressBar(R.id.custom_progressbar, 0, 0, false);
            mRemoteViews.setViewVisibility(R.id.custom_progressbar, View.GONE);
        } else {
            if(!isFail){
                mRemoteViews.setTextViewText(R.id.tv_custom_progress_status,"下载"+mProgress+"%");
                mRemoteViews.setViewVisibility(R.id.custom_progressbar, View.VISIBLE);
            }else{
                mRemoteViews.setViewVisibility(R.id.custom_progressbar, View.GONE);
            }
            mRemoteViews.setProgressBar(R.id.custom_progressbar, 100, mProgress, false);

        }
        if (isFail) {
            mRemoteViews.setViewVisibility(R.id.btn_reload, View.VISIBLE);
        } else {
            mRemoteViews.setViewVisibility(R.id.btn_reload, View.INVISIBLE);
        }
        mBuilder.setContent(mRemoteViews);
        if (isInstall) {
            Uri uri;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                uri = FileProvider.getUriForFile(getApplicationContext(), getPackageName() + ".FileProvider", mFile);
            } else {
                uri = Uri.fromFile(mFile);
            }
            Intent intent = new Intent(Intent.ACTION_INSTALL_PACKAGE);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.setDataAndType(uri, "application/vnd.android.package-archive");
            PendingIntent pendingIntent = PendingIntent.getActivity(UpdateService.this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            mBuilder.setAutoCancel(true)
                    .setContentIntent(pendingIntent);
        } else {
            mBuilder.setContentIntent(getDefalutIntent(PendingIntent.FLAG_UPDATE_CURRENT));
        }
        Notification nitify = mBuilder.build();
        nitify.contentView = mRemoteViews;
        nitify.flags |= Notification.FLAG_NO_CLEAR;
        mNotificationManager.notify(isInstall ? mInstallNotifyId:mNotifyId, nitify);
    }

    /**
     * 广播监听按钮点击
     */
    public class ButtonBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(ACTION_BUTTON)) {
                downLoadAPK();
                showCustomProgressNotify("下载中...", false, false);
            }
        }
    }

    private void installApk() {
        Uri uri;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            uri = FileProvider.getUriForFile(BaseApplication.getApp(), BaseApplication.getApp().getPackageName() + ".FileProvider", mFile);
        } else {
            uri = Uri.fromFile(mFile);
        }
        Intent intent = new Intent(Intent.ACTION_INSTALL_PACKAGE);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.setDataAndType(uri,"application/vnd.android.package-archive");
        startActivity(intent);
    }

    /**
     * 创建Binder对象，返回给客户端即Activity使用，提供数据交换的接口
     */
    public class LocalBinder extends Binder {
        // 声明一个方法，getService。（提供给客户端调用）
        public UpdateService getService() {
            // 返回当前对象LocalService,这样我们就可在客户端端调用Service的公共方法了
            return UpdateService.this;
        }

        public int getProgerss(){
            return mProgress;
        }

        public void setProgressListener(ProgressListener listener){
            mListener = listener;
            if(mListener!=null){
                mListener.onProgress(mProgress);
            }
        }
    }

    public interface ProgressListener{
        void onProgress(int progress);
    }

    @Override
    public void onDestroy() {
        if (bReceiver != null) {
            unregisterReceiver(bReceiver);
        }
        stopForeground(true);
        isStart = false;
        if(mDisposable!=null){
            mDisposable.dispose();
        }
        super.onDestroy();
    }
}
