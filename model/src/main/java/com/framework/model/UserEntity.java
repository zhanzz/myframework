package com.framework.model;

import android.text.TextUtils;
import java.util.List;
/**
 * Created by zhenggl on 2016/3/27.
 * 用户相关的实体类
 * @desc
 */
public class UserEntity{
    //前端定义
    private String account ;//账号
    //
    private String id;
    private String headLoGo;//头像地址
    private String realName;//用户姓名
    private String userName;//用户名
    private String token;//登录token信息

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getHeadLoGo() {
        return headLoGo;
    }

    public void setHeadLoGo(String headLoGo) {
        this.headLoGo = headLoGo;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }


    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
