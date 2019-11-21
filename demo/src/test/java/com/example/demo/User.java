package com.example.demo;

import com.alibaba.fastjson.annotation.JSONField;

import java.util.Date;

/**
 * @author zhangzhiqiang
 * @date 2019/9/5.
 * description：
 */
public class User {
    int age;
    String name;
    @JSONField(format = "MMM dd, yyyy h:mm:ss aa")
    Date date;
    boolean needPrint;

    public boolean isNeedPrint() {
        return needPrint;
    }

    public void setNeedPrint(boolean needPrint) {
        this.needPrint = needPrint;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
