package com.example.demo.excption_handler;

import android.app.Application;

import com.framework.common.utils.LogUtil;

public class CrashHandler implements Thread.UncaughtExceptionHandler {
    private Thread.UncaughtExceptionHandler systemDefaultHandler;
    private static CrashHandler instance = new CrashHandler();

    private CrashHandler() {//构造方法私有
    }

    public static CrashHandler getInstance() {
        return instance;
    }

    public void init() {
        /**
         * JVM的处理
         * 当线程抛出一个未捕获到的异常时，JVM将为异常寻找以下三种可能的处理器。
         * 1、线程对象的未捕获异常处理器
         * 2、线程对象所在的线程组（ThreadGroup）的未捕获异常处理器
         * 3、默认的未捕获异常处理器
         * 最后，如果一个处理器都没有，JVM将堆栈异常记录打印到控制台，并退出程序。(这也是你经常看到的logcat崩溃打印，AndroidRuntime抛出的打印)
         */
        //Runtime$KillApplicationHandler退出并打印(systemDefaultHandler为系统初始化时设置的类型为Runtime$KillApplicationHandler)
        systemDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(instance);//设置该CrashHandler为系统默认的
    }

    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        LogUtil.e("zhang",ex.getMessage());
        LogUtil.e("zhang",systemDefaultHandler.getClass().getSimpleName());
        systemDefaultHandler.uncaughtException(thread,ex);
    }
}