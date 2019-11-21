package com.example.demo;

/**
 * @author zhangzhiqiang
 * @date 2019/11/20.
 * description：
 */
public class Son extends Fater {
    private int age = 10;

//    @Override
//    public int getAge() {
//        return age;
//    }

    @Override
    public void setAge(int age) {
        this.age = age;
    }

    @Override
    public int getFAge(){
        return age;
    }
}
