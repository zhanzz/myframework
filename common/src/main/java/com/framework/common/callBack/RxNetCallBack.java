package com.framework.common.callBack;

public interface RxNetCallBack<T> {
    /**
     * 数据请求成功
     *
     * @param data 请求到的数据
     */
    void onSuccess(T data,int code,String msg);

    /**
     * 数据请求失败
     */
    void onFailure(int code,String msg);
}