package com.example.study_gradle2;

import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.SerializerFeature;

/**
 * @author zhangzhiqiang
 * @date 2019/8/30.
 * description：
 */
public class Result {
    int code;
    @JSONField(deserialize=false)
    private final String msg="haha";
    User user;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }
}
