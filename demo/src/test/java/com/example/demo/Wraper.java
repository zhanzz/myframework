package com.example.demo;

/**
 * @author zhangzhiqiang
 * @date 2019/11/20.
 * description：
 */
public class Wraper {
    EnumBean isGiveaway;

    public boolean isGiveaway() {
        return isGiveaway.getValue()==1;
    }

    public void setIsGiveaway(EnumBean isGiveaway) {
        this.isGiveaway = isGiveaway;
    }
}
