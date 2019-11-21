package com.example.demo;

import java.io.Serializable;

/**
 * Created by zhangzhiqiang on 2018/8/10.
 */

public class EnumBean implements Serializable{
    private static final long serialVersionUID = -5649886787726370727L;
    String name;
    int value;
    String description;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
