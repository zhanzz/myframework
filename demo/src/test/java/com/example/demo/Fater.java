package com.example.demo;

/**
 * @author zhangzhiqiang
 * @date 2019/11/20.
 * description：
 */
public class Fater {
    public int age = 20;

    public int getAge() {
        return getFAge();
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int getFAge(){
        return age;
    }
}
