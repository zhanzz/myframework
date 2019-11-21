package com.example.demo;

/**
 * @author zhangzhiqiang
 * @date 2019/9/5.
 * description：
 */
public class Result<T> {
   public String result;
   public int code;
   private boolean isGiveaway;
   public T data;

   public boolean isGiveaway() {
      return isGiveaway;
   }

   public void setGiveaway(boolean giveaway) {
      isGiveaway = giveaway;
   }
}
