package com.example.demo;

/**
 * @author zhangzhiqiang
 * @date 2019/9/5.
 * description：
 */
public class Result<T> {
   public String result="";
   public int code;
   private String isGiveaway;
   public T data;

   public T getData() {
      return data;
   }

   public void setData(T data) {
      this.data = data;
   }

   public String isGiveaway() {
      return isGiveaway;
   }

   public void setGiveaway(String giveaway) {
      isGiveaway = giveaway;
   }

   public String getResult() {
      return result;
   }

   public void setResult(String result) {
      this.result = result;
   }
}
