package com.example.demo;

/**
 * @author zhangzhiqiang
 * @date 2019/11/20.
 * description：
 */
public class Fater extends FatherFirst implements Cloneable{
    public int age = 20;


    public int getAge() {
        System.out.println("get_father");
        return getFAge();
    }

    public void setAge(int age) {
        System.out.println("set_father");
        this.age = age;
    }

    public int getFAge(){
        return age;
    }

    @Override
    public Fater clone(){
        try {
            return (Fater) super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return this;
    }
}
