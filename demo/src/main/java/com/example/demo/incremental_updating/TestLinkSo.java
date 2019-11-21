package com.example.demo.incremental_updating;

/**
 * @author zhangzhiqiang
 * @date 2019/10/26.
 * description：
 */
public class TestLinkSo {
    public static native String getThirdString();

    static {
        System.loadLibrary("testlink");
    }
}
