package com.framework.common.data;

/**
 * @author zhangzhiqiang
 * @date 2019/5/24.
 * description：
 */
public enum LoadType {
    NONE,//不需要展示loading
    LOAD,//展示loading
    LOAD_DIALOG,//展示不可点击后面视图的loading,类似一个不可点击取消的dialog
    LOGIN_NONE,//需要判断登录，不需要展示loading
    LOGIN_LOAD,//需要判断登录，展示loading
    LOGIN_LOAD_DIALOG //需要判断登录，展示不可点击后面视图的loading,类似一个不可点击取消的dialog
}
