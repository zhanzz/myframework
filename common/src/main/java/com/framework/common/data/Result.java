package com.framework.common.data;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * @author zhangzhiqiang
 * @date 2019/4/14.
 * description：
 */
public class Result<T>{
    @JSONField(name = "result")
    int code;
    @JSONField(name = "desc")
    String message;
    T data;

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
