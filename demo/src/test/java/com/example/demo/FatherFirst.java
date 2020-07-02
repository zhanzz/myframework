package com.example.demo;

public class FatherFirst {
    protected FatherFirst(){
        getAge();
        System.out.println("FatherFirst");
    }

    public int getAge() {
        System.out.println("get_FatherFirst");
        return 4;
    }
}
