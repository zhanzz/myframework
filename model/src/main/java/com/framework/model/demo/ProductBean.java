package com.framework.model.demo;

import android.text.TextUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by zhangzhiqiang on 2016/8/31.
 */
public class ProductBean {
    int monthlySales;
    String productName;
    String retailPrice;
    String productPrice;
    public String priceId;
    public long onShelveTime;//上架时间  店新品用到
    /**
     * 活动类型  1,普通 2->团购 ，3->预售  4-新人专区,5-限量抢购,6-高额返利
     * 24-新版拼团，25-秒杀活动，26-返利活动
     */
    int saleType;
    double activityPrice;
    double marketPrice;//市场价（划价）
    //高返活动用
    String originalSaleEarning;//原本获利／返利
    String currentSaleEarning;//现自销获利／返利

    String shopId;
    String productIcon;
    String thumPicture;
    String productId;
    String belongType;
    String activityType;//如果等于1 就是促销的
    double price;

    String saleEarning; //自销获利
    String distributionEarning; //分销获利
    List<String> tagType;// 商品类型
    String shareEarning;
    double orderEarning;

    List<String> titleTag;//模块类型 1蜘点超市

    String agentShopId; // 代理商提成计算
    int minSale;//批发起订量

    int itemType;//类型用于不同的item视图


    //为购物车新增加字段
    int num;
    int localStock = Integer.MIN_VALUE; //本地记录的库存，如果为-1说明没有同步后台库存
    String skuId;//用于添加购物车
    String carId;//用于修改购物车
    private String activityId;
    public String searchKey;//跳转所需参数
    public boolean isShowCart;//是否展示加入购物车
    public boolean isO2o;//是否到家商品
    public int displaySales; //销售量
    private String productAdsPic;
    private String activitySales;



    public double getMarketPrice() {
        return marketPrice;
    }

    public void setMarketPrice(double marketPrice) {
        this.marketPrice = marketPrice;
    }

    public int getDisplaySales() {
        return displaySales;
    }

    public void setDisplaySales(int displaySales) {
        this.displaySales = displaySales;
    }

    public String getActivityId() {
        return activityId;
    }

    public void setActivityId(String activityId) {
        this.activityId = activityId;
    }

    public String getAgentShopId() {
        return agentShopId;
    }

    public void setAgentShopId(String agentShopId) {
        this.agentShopId = agentShopId;
    }

    public String getCarId() {
        return carId;
    }

    public void setCarId(String carId) {
        this.carId = carId;
    }


    public String getSkuId() {
        return skuId;
    }

    public void setSkuId(String skuId) {
        this.skuId = skuId;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public int getLocalStock() {
        return localStock;
    }

    public void setLocalStock(int localStock) {
        this.localStock = localStock;
    }

    public List<String> getTitleTag() {
        return titleTag;
    }

    public void setTitleTag(List<String> titleTag) {
        this.titleTag = titleTag;
    }

    public int getMinSale() {
        return minSale;
    }

    public void setMinSale(int minSale) {
        this.minSale = minSale;
    }

    public double getOrderEarning() {
        return orderEarning;
    }

    public void setOrderEarning(double orderEarning) {
        this.orderEarning = orderEarning;
    }

    public String getShareEarning() {
        return shareEarning;
    }

    public void setShareEarning(String shareEarning) {
        this.shareEarning = shareEarning;
    }

    public String getOriginalSaleEarning() {
        return originalSaleEarning;
    }

    public void setOriginalSaleEarning(String originalSaleEarning) {
        this.originalSaleEarning = originalSaleEarning;
    }

    public String getCurrentSaleEarning() {
        return currentSaleEarning;
    }

    public void setCurrentSaleEarning(String currentSaleEarning) {
        this.currentSaleEarning = currentSaleEarning;
    }

    public String getOnShelveTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日");
        return sdf.format(new Date(onShelveTime));
    }

    public void setOnShelveTime(long onShelveTime) {
        this.onShelveTime = onShelveTime;
    }

    public List<String> getTagType() {
        return tagType;
    }

    public void setTagType(List<String> tagType) {
        this.tagType = tagType;
    }

    public String getActivityType() {
        return activityType;
    }

    public void setActivityType(String activityType) {
        this.activityType = activityType;
    }

    public int getItemType() {
        return itemType;
    }

    public void setItemType(int itemType) {
        this.itemType = itemType;
    }

    public String getDistributionEarning() {
        return distributionEarning;
    }

    public void setDistributionEarning(String distributionEarning) {
        this.distributionEarning = distributionEarning;
    }

    public String getSaleEarning() {
        return saleEarning;
    }

    public void setSaleEarning(String saleEarning) {
        this.saleEarning = saleEarning;
    }

    public double getActivityPrice() {
        return activityPrice;
    }

    public void setActivityPrice(double activityPrice) {
        this.activityPrice = activityPrice;
    }

    //是否显示划价
    public boolean isShowMiddleLinePrice(){
        if(marketPrice>0.00001){
            return true;
        }
        return  false;
    }

    public int getSaleType() {
        return saleType;
    }

    public void setSaleType(int saleType) {
        this.saleType = saleType;
    }

    public double getShowPrice(){
        if(activityPrice>0.00001){
            return activityPrice;
        }
        return price;
    }

    public String getThumPicture() {
        return TextUtils.isEmpty(thumPicture) ? productIcon:thumPicture;
    }

    public void setThumPicture(String thumPicture) {
        this.thumPicture = thumPicture;
    }

    public String getProductIcon() {
        if(TextUtils.isEmpty(productIcon)){
            return thumPicture;
        }
        return productIcon;
    }

    public void setProductIcon(String productIcon) {
        this.productIcon = productIcon;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(String productPrice) {
        this.productPrice = productPrice;
    }

    public String getBelongType() {
        return belongType;
    }

    public void setBelongType(String belongType) {
        this.belongType = belongType;
    }

    public int getMonthlySales() {
        return monthlySales;
    }

    public void setMonthlySales(int monthlySales) {
        this.monthlySales = monthlySales;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getRetailPrice() {
        return retailPrice;
    }

    public void setRetailPrice(String retailPrice) {
        this.retailPrice = retailPrice;
    }

    public String getShopId() {
        return shopId;
    }

    public void setShopId(String shopId) {
        this.shopId = shopId;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getProductAdsPic() {
        return productAdsPic;
    }

    public void setProductAdsPic(String productAdsPic) {
        this.productAdsPic = productAdsPic;
    }

    public String getActivitySales() {
        return activitySales;
    }

    public void setActivitySales(String activitySales) {
        this.activitySales = activitySales;
    }
}
