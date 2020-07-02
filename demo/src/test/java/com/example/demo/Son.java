package com.example.demo;

import java.io.Serializable;

/**
 * @author zhangzhiqiang
 * @date 2019/11/20.
 * description：
 */
public class Son extends Fater implements Cloneable, Serializable {
    public int xx = 0;
    public int age = 10;
    public Result<String> result = new Result<>();
    public Result<String> getResult(){
        return result;
    }
    public int sex = getAge();

    public Son(){
        System.out.println("SOn");
    }

    public Son(int x){
        //getAge();
        this();
    }

    @Override
    public int getAge() {
        System.out.println("get_son");
        xx = 100;
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
