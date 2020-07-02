package com.example.demo.window.WindowService;

import android.app.Dialog;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.annotation.Nullable;

public class DialogService extends Service {

    @Override
    public void onCreate() {
        super.onCreate();
        Dialog dialog = new Dialog(getApplicationContext());
        TextView tv = new TextView(getApplicationContext());
        tv.setText("haha");
        tv.setBackgroundColor(0xffff0000);
        dialog.setContentView(tv);
        dialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
        dialog.show();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public static void start(Context context) {
        Intent starter = new Intent(context, DialogService.class);
        context.startService(starter);
    }
}
