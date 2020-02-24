package com.example.demo;

import java.io.Serializable;

/**
 * @author zhangzhiqiang
 * @date 2019/11/20.
 * description：
 */
public class Son extends Fater implements Cloneable, Serializable {
    public int age = 10;

    @Override
    public int getAge() {
        System.out.println("get_son");
        return age;
    }

    @Override
    public void setAge(int age) {
        System.out.println("set_son");
        this.age = age;
    }

    @Override
    public Son clone() {
        return (Son) super.clone();
    }
}
