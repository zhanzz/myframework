package com.framework.common.utils;

import com.framework.common.base_mvp.BasePresenter;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class MyCrashHandler implements Thread.UncaughtExceptionHandler {

    private Thread.UncaughtExceptionHandler mDefaultUncaughtExceptionHandler;

    public static MyCrashHandler getInstance() {
        return CrashHandlerHolder.INSTANCE;
    }

    public void init() {
       /*
        * 弹出解决方案之后把崩溃继续交给系统处理，
        * 所以保存当前UncaughtExceptionHandler用于崩溃发生时使用。
        */
        mDefaultUncaughtExceptionHandler = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(this);
    }

    // 当有未截获的异常时，回调此方法
    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        if(ex instanceof BasePresenter.MvpViewNotAttachedException){
            return;
        }
        // 传递给保存的UncaughtExceptionHandler
        mDefaultUncaughtExceptionHandler.uncaughtException(thread, ex);
    }

    private static class CrashHandlerHolder {
        static final MyCrashHandler INSTANCE = new MyCrashHandler();
    }

    private MyCrashHandler() { }

    public void restart() {
        try {
            Class clazz = Class.forName("java.lang.Daemons$FinalizerWatchdogDaemon");
            Method method = clazz.getSuperclass().getDeclaredMethod("stop");
            method.setAccessible(true);
            Field field = clazz.getDeclaredField("INSTANCE");
            field.setAccessible(true);
            method.invoke(field.get(null));

            Method method2 = clazz.getSuperclass().getDeclaredMethod("start");
            method2.setAccessible(true);
            method2.invoke(field.get(null));
        }
        catch (Throwable e) {
            e.printStackTrace();
        }
    }
}