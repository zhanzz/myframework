package com.framework.model.demo;

import java.util.List;

/**
 * Created by zhangzhiqiang on 2017/5/12.
 */
public class SecondPageThemeBean {
    String tabBackgroundUrl;//tab底部背景
    String tabBackgroundColor;//tab底部背景颜色
    List<TabStyle> tabList;//tab列表

    public String getTabBackgroundColor() {
        return tabBackgroundColor;
    }

    public void setTabBackgroundColor(String tabBackgroundColor) {
        this.tabBackgroundColor = tabBackgroundColor;
    }

    public List<TabStyle> getTabList() {
        return tabList;
    }

    public void setTabList(List<TabStyle> tabList) {
        this.tabList = tabList;
    }

    public String getTabBackgroundUrl() {
        return tabBackgroundUrl;
    }

    public void setTabBackgroundUrl(String tabBackgroundUrl) {
        this.tabBackgroundUrl = tabBackgroundUrl;
    }

    public static class TabStyle{
        String tabId;
        String tabName;
        String tabNormalImg;//tab未选中时的图片
        String tabPresessImg;//tab选中时的图片
        String linkType;//"normal代表普通原生页面，category代表分类，h5代表H5，shopping_car代表购物车，personal代表个人中心",
        String link;//链接地址

        public String getLink() {
            return link;
        }

        public void setLink(String link) {
            this.link = link;
        }

        public String getTabId() {
            return tabId;
        }

        public void setTabId(String tabId) {
            this.tabId = tabId;
        }

        public String getTabName() {
            return tabName;
        }

        public void setTabName(String tabName) {
            this.tabName = tabName;
        }

        public String getLinkType() {
            return linkType;
        }

        public void setLinkType(String linkType) {
            this.linkType = linkType;
        }

        public String getTabNormalImg() {
            return tabNormalImg;
        }

        public void setTabNormalImg(String tabNormalImg) {
            this.tabNormalImg = tabNormalImg;
        }

        public String getTabPresessImg() {
            return tabPresessImg;
        }

        public void setTabPresessImg(String tabPresessImg) {
            this.tabPresessImg = tabPresessImg;
        }
    }
}
