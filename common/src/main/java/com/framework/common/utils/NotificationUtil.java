package com.framework.common.utils;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;

/**
 * @author zhangzhiqiang
 * @date 2019/12/31.
 * description：
 */
public class NotificationUtil {
    public static String createChannelId(Context context,ChannelConfig config) {
        if(config==null){
            return "";
        }
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        String channelId = context.getPackageName() + config.channelId;//通知渠道id
        //String channelName = "消息通知";//"PUSH_NOTIFY_NAME";//通知渠道名
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            //int importance = config.importance;//通知级别
            NotificationChannel channel = new NotificationChannel(channelId, config.channelName, config.importance);
            channel.enableLights(config.enableLight);//设置指示灯
            channel.setLightColor(Color.GREEN);
            channel.enableVibration(config.enableVibration);//设置通知出现震动
            channel.setShowBadge(config.showBadge);
            channel.setDescription(config.description);
            if (notificationManager != null) {
                notificationManager.createNotificationChannel(channel);
            }
        }
        return channelId;
    }

    public static class ChannelConfig {
        String channelId;
        String channelName;
        int importance;
        String description;
        boolean enableLight;
        boolean enableVibration;
        boolean showBadge;

        public static class Builder {
            String channelId;
            String channelName="";
            int importance;
            String description="";
            boolean enableLight = false;
            boolean enableVibration = false;
            boolean showBadge = false;

            public Builder(){
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    importance = NotificationManager.IMPORTANCE_DEFAULT;
                }
            }

            public ChannelConfig build() {
                ChannelConfig config = new ChannelConfig();
                config.channelId = channelId;
                config.channelName = channelName;
                config.importance = importance;
                config.description = description;
                config.enableLight = enableLight;
                config.enableVibration = enableVibration;
                config.showBadge = showBadge;
                return config;
            }

            public String getChannelId() {
                return channelId;
            }

            public Builder setChannelId(String channelId) {
                this.channelId = channelId;
                return this;
            }

            public String getChannelName() {
                return channelName;
            }

            public Builder setChannelName(String channelName) {
                this.channelName = channelName;
                return this;
            }

            public int getImportance() {
                return importance;
            }

            public Builder setImportance(int importance) {
                this.importance = importance;
                return this;
            }

            public String getDescription() {
                return description;
            }

            public Builder setDescription(String description) {
                this.description = description;
                return this;
            }

            public boolean isEnableLight() {
                return enableLight;
            }

            public Builder setEnableLight(boolean enableLight) {
                this.enableLight = enableLight;
                return this;
            }

            public boolean isEnableVibration() {
                return enableVibration;
            }

            public Builder setEnableVibration(boolean enableVibration) {
                this.enableVibration = enableVibration;
                return this;
            }

            public boolean isShowBadge() {
                return showBadge;
            }

            public Builder setShowBadge(boolean showBadge) {
                this.showBadge = showBadge;
                return this;
            }
        }
    }
}
