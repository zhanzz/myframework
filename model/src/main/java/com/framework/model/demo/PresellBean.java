package com.framework.model.demo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class PresellBean implements Serializable {
    private ArrayList<PresellProduct> productList;
    private List<CategoryItem> categories;

    public List<CategoryItem> getCategories() {
        return categories;
    }

    public void setCategories(List<CategoryItem> categories) {
        this.categories = categories;
    }

    public ArrayList<PresellProduct> getProductList() {
        return productList;
    }

    public void setProductList(ArrayList<PresellProduct> productList) {
        this.productList = productList;
    }

    public static class PresellProduct implements Serializable {
        private String productId;
        private String productName;
        private double activityPrice;
        private double productPrice;
        private double marketPrice;
        private String productIcon;
        private String productAdsPic;
        private String activityId;
        private int activitySales;
        private double saleEarning;
        private double shareEarning;
        private double orderEarning;
        private double distributionEarning;

        public double getMarketPrice() {
            return marketPrice;
        }

        public void setMarketPrice(double marketPrice) {
            this.marketPrice = marketPrice;
        }

        public double getDistributionEarning() {
            return distributionEarning;
        }

        public void setDistributionEarning(double distributionEarning) {
            this.distributionEarning = distributionEarning;
        }

        public double getOrderEarning() {
            return orderEarning;
        }

        public void setOrderEarning(double orderEarning) {
            this.orderEarning = orderEarning;
        }

        public String getProductAdsPic() {
            return productAdsPic;
        }

        public void setProductAdsPic(String productAdsPic) {
            this.productAdsPic = productAdsPic;
        }


        //综合仓用到的字段
        private long currentTime;
        private long endTime;
        private String shopId;
        private String source;

        public String getShopId() {
            return shopId;
        }

        public void setShopId(String shopId) {
            this.shopId = shopId;
        }

        public String getSource() {
            return source;
        }

        public void setSource(String source) {
            this.source = source;
        }

        public long getEndTime() {
            return endTime;
        }

        public void setEndTime(long endTime) {
            this.endTime = endTime;
        }

        public long getCurrentTime() {

            return currentTime;
        }

        public void setCurrentTime(long currentTime) {
            this.currentTime = currentTime;
        }

        public String getProductId() {
            return productId;
        }

        public void setProductId(String productId) {
            this.productId = productId;
        }

        public String getProductName() {
            return productName;
        }

        public void setProductName(String productName) {
            this.productName = productName;
        }


        public double getProductPrice() {
            return productPrice;
        }

        public void setProductPrice(double productPrice) {
            this.productPrice = productPrice;
        }

        public String getProductIcon() {
            return productIcon;
        }

        public void setProductIcon(String productIcon) {
            this.productIcon = productIcon;
        }

        public String getActivityId() {
            return activityId;
        }

        public void setActivityId(String activityId) {
            this.activityId = activityId;
        }

        public int getActivitySales() {
            return activitySales;
        }

        public void setActivitySales(int activitySales) {
            this.activitySales = activitySales;
        }

        public double getSaleEarning() {
            return saleEarning;
        }

        public void setSaleEarning(double saleEarning) {
            this.saleEarning = saleEarning;
        }

        public double getShareEarning() {
            return shareEarning;
        }

        public void setShareEarning(double shareEarning) {
            this.shareEarning = shareEarning;
        }

        public double getActivityPrice() {

            return activityPrice;
        }

        public void setActivityPrice(double activityPrice) {
            this.activityPrice = activityPrice;
        }
    }

    public static class CategoryItem {
        public String icon;
        public String id;
        public String name;

        public String getIcon() {
            return icon;
        }

        public void setIcon(String icon) {
            this.icon = icon;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}
