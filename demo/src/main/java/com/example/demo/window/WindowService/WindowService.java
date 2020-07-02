package com.example.demo.window.WindowService;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.IBinder;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.annotation.Nullable;

/**
 * @author zhangzhiqiang
 * @date 2019/10/12.
 * description：
 */
public class WindowService extends Service {
    WindowManager.LayoutParams params;
    WindowManager windowManager;
    @Override
    public void onCreate() {
        super.onCreate();
        TextView tv = new TextView(this);
        tv.setText("我是自定义window");
        tv.setBackgroundColor(0xffff0000);
        tv.setOnTouchListener(new FloatingOnTouchListener());

        params = new WindowManager.LayoutParams();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            params.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;//需要权限
        } else {
            params.type = WindowManager.LayoutParams.TYPE_PHONE;
        }
        params.width = ViewGroup.LayoutParams.WRAP_CONTENT;
        params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        params.format = PixelFormat.RGBA_8888;
        params.gravity = Gravity.LEFT | Gravity.TOP;
        params.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        params.width = 500;
        params.height = 100;
        params.x = 300;
        params.y = 300;
        windowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        windowManager.addView(tv,params);

        //window type 1到99在activity中使用 activity dialog属于此类
        //type 1000到1999要依赖于父窗口 popwindow属于此类

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private class FloatingOnTouchListener implements View.OnTouchListener {
        private int x;
        private int y;

        @Override
        public boolean onTouch(View view, MotionEvent event) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    x = (int) event.getRawX();
                    y = (int) event.getRawY();
                    break;
                case MotionEvent.ACTION_MOVE:
                    int nowX = (int) event.getRawX();
                    int nowY = (int) event.getRawY();
                    int movedX = nowX - x;
                    int movedY = nowY - y;
                    x = nowX;
                    y = nowY;
                    params.x = params.x + movedX;
                    params.y = params.y + movedY;
                    windowManager.updateViewLayout(view, params);
                    break;
                default:
                    break;
            }
            return false;
        }
    }

    public static void start(Context context) {
        Intent starter = new Intent(context, WindowService.class);
        context.startService(starter);
    }
}
