package com.example.demo;

/**
 * @author zhangzhiqiang
 * @date 2019/9/5.
 * description：
 */
public class Result<T> {
   public String result="";
   public int code;
   //is开头的要正确生成get set   boolean类型为setGiveaway  String 类型为setIsGiveaway
   private boolean isGiveaway;
   public T data;
   private String isX;

   public String getIsX() {
      return isX;
   }

   public void setIsX(String isX) {
      this.isX = isX;
   }

   public boolean isGiveaway() {
      return isGiveaway;
   }

   public void setGiveaway(boolean giveaway) {
      isGiveaway = giveaway;
   }

   public T getData() {
      return data;
   }

   public void setData(T data) {
      this.data = data;
   }

   public String getResult() {
      return result;
   }

   public void setResult(String result) {
      this.result = result;
   }
}
