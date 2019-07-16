package com.framework.model.demo;

import java.io.Serializable;
import java.util.List;

public class ActivityBean {
    private String backgroundColor;
    private String titleTag; //商超标识
    private String shopId;//v2.1.31新增40
    private List<ActivityItemBean> floorList;
    private TopFloor topFloor;
    private FloatArea floatLayer; //悬浮按钮
    private PopAdInfo popAd;//v2.0.2新增的弹屏广告
    private SecondPageThemeBean channelList;

    public String getShopId() {
        return shopId;
    }

    public void setShopId(String shopId) {
        this.shopId = shopId;
    }

    public String getTitleTag() {
        return titleTag;
    }

    public void setTitleTag(String titleTag) {
        this.titleTag = titleTag;
    }

    public TopFloor getTopFloor() {
        return topFloor;
    }

    public void setTopFloor(TopFloor topFloor) {
        this.topFloor = topFloor;
    }

    public String getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(String backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public SecondPageThemeBean getChannelList() {
        return channelList;
    }

    public void setChannelList(SecondPageThemeBean channelList) {
        this.channelList = channelList;
    }

    public List<ActivityItemBean> getFloorList() {
        return floorList;
    }

    public void setFloorList(List<ActivityItemBean> floorList) {
        this.floorList = floorList;
    }

    public FloatArea getFloatLayer() {
        return floatLayer;
    }

    public void setFloatLayer(FloatArea floatLayer) {
        this.floatLayer = floatLayer;
    }

    public PopAdInfo getPopAd() {
        return popAd;
    }

    public void setPopAd(PopAdInfo popAd) {
        this.popAd = popAd;
    }

    /**
     * v2.0.2新增的弹屏广告
     */
    public static class PopAdInfo {
        private int contentWidth;
        private int contentHeight;
        private String bgAlpha;
        private String actPicUrl;
        private float percent;
        private String anim;
        private String jumpType;
        private List<String> params;
        private String contentType;//（h5、pic）
        private String h5Link;//（h5链接地址）
        //查询券需要
        private boolean hasTicket;
        private String id;

        public boolean isHasTicket() {
            return hasTicket;
        }

        public void setHasTicket(boolean hasTicket) {
            this.hasTicket = hasTicket;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getContentType() {
            return contentType;
        }

        public void setContentType(String contentType) {
            this.contentType = contentType;
        }

        public String getH5Link() {
            return h5Link;
        }

        public void setH5Link(String h5Link) {
            this.h5Link = h5Link;
        }

        public int getContentWidth() {
            return contentWidth;
        }

        public void setContentWidth(int contentWidth) {
            this.contentWidth = contentWidth;
        }

        public int getContentHeight() {
            return contentHeight;
        }

        public void setContentHeight(int contentHeight) {
            this.contentHeight = contentHeight;
        }

        public float getPercent() {
            return percent;
        }

        public void setPercent(float percent) {
            this.percent = percent;
        }

        public String getBgAlpha() {
            return bgAlpha;
        }

        public void setBgAlpha(String bgAlpha) {
            this.bgAlpha = bgAlpha;
        }

        public String getActPicUrl() {
            return actPicUrl;
        }

        public void setActPicUrl(String actPicUrl) {
            this.actPicUrl = actPicUrl;
        }

        public String getAnim() {
            return anim;
        }

        public void setAnim(String anim) {
            this.anim = anim;
        }

        public String getJumpType() {
            return jumpType;
        }

        public void setJumpType(String jumpType) {
            this.jumpType = jumpType;
        }

        public List<String> getParams() {
            return params;
        }

        public void setParams(List<String> params) {
            this.params = params;
        }
    }

    public static class TopFloor {
        String jumpType;//跳转类型，参考之前的跳转
        List<String> params;//跳转携带参数，参考之前的跳转
        String actPicUrl;//图片链接地址
        int width;//宽度，默认填充屏幕宽度，750，不可以编辑
        int height;//高度，默认60
        int appearFloorNum;//滑动到第几个楼层时出现

        public String getJumpType() {
            return jumpType;
        }

        public void setJumpType(String jumpType) {
            this.jumpType = jumpType;
        }

        public List<String> getParams() {
            return params;
        }

        public void setParams(List<String> params) {
            this.params = params;
        }

        public String getActPicUrl() {
            return actPicUrl;
        }

        public void setActPicUrl(String actPicUrl) {
            this.actPicUrl = actPicUrl;
        }

        public int getWidth() {
            return width;
        }

        public void setWidth(int width) {
            this.width = width;
        }

        public int getHeight() {
            return height;
        }

        public void setHeight(int height) {
            this.height = height;
        }

        public int getAppearFloorNum() {
            return appearFloorNum;
        }

        public void setAppearFloorNum(int appearFloorNum) {
            this.appearFloorNum = appearFloorNum;
        }
    }

    public static class FloatArea {
        private String pictureUrl;
        private String isDragEnable;
        private String position;
        String jumpType;
        List<String> params;

        public String getJumpType() {
            return jumpType;
        }

        public void setJumpType(String jumpType) {
            this.jumpType = jumpType;
        }

        public List<String> getParams() {
            return params;
        }

        public void setParams(List<String> params) {
            this.params = params;
        }

        public String getPictureUrl() {
            return pictureUrl;
        }

        public void setPictureUrl(String pictureUrl) {
            this.pictureUrl = pictureUrl;
        }

        public String getIsDragEnable() {
            return isDragEnable;
        }

        public void setIsDragEnable(String isDragEnable) {
            this.isDragEnable = isDragEnable;
        }

        public String getPosition() {
            return position;
        }

        public void setPosition(String position) {
            this.position = position;
        }
    }

    public static class HeadInfo {
        private String backgroundColor;
        private String titleImgUrl;
        private String titleName;
        private List<String> naTypes;
        private String showChangeArea;//是否显示选择地区，1显示 0 不显示

        public String getShowChangeArea() {
            return showChangeArea;
        }

        public void setShowChangeArea(String showChangeArea) {
            this.showChangeArea = showChangeArea;
        }

        public List<String> getNaTypes() {
            return naTypes;
        }

        public void setNaTypes(List<String> naTypes) {
            this.naTypes = naTypes;
        }

        public String getBackgroundColor() {
            return backgroundColor;
        }

        public void setBackgroundColor(String backgroundColor) {
            this.backgroundColor = backgroundColor;
        }

        public String getTitleImgUrl() {
            return titleImgUrl;
        }

        public void setTitleImgUrl(String titleImgUrl) {
            this.titleImgUrl = titleImgUrl;
        }

        public String getTitleName() {
            return titleName;
        }

        public void setTitleName(String titleName) {
            this.titleName = titleName;
        }
    }

    public static class ActivityItemBean {
        public boolean hasArea;
        public String titleTag;
        public String shopId;
        private int floorNum;
        private int innerBorder; //内边距
        private List<Integer> externalBorder; //外边距 //int数组类型：上、右、下、左 或者一个值代表上右下左
        private String isShowMore; //是否显示更多 （0不显示，1显示）
        private String jumpType;
        private List<String> params;
        private String template;
        private String backgroundColor;
        private String backgroundImgUrl;
        private int width;
        private int height;
        private String roundCorner = "0";//[1=设置圆角，0代表不设置，默认是0]
        private int cornerRadius;//圆角半径
        private AnnConfig annConfig;
        private String api;
        private String requestParams;
        private String style;//产品样式
        private float maxShowNum;
        private List<ProductBean> waresList;
        private WareConfig waresListConfig;
        private ApiConfig apiConfig;
        private List<TabBean> tabList;
        private List<ActivityItemBean> tabFloorList;
        private TabListConfig tabListConfig;
        private SecondTabListConfig secondTabListConfig;
        private List<ActBean> adsList;
        private SearchObj searchObj;
        private String indicatorNormalColor;
        private String indicatorPressColor;
        private String indicatorPosition;
        private ShareApperance shareApperance;
        private int row;
        private int col;
        private String themeType; //0或空代表之前默认的标准样式，1—>上述第一种，2上述第二种
        private String changeBgImg;//是否以高斯模糊的效果设置背景图片，1开启，0关闭
        private String indicatorType;//0或者空代表默认圆圈样式指示器，1当前第几/总数样式
        private int space;//banner 页面之间的间距
        private float bannerWidthRate; //banner中间页面占屏幕的比例

        public String getRequestParams() {
            return requestParams;
        }

        public void setRequestParams(String requestParams) {
            this.requestParams = requestParams;
        }

        public String getStyle() {
            return style;
        }

        public void setStyle(String style) {
            this.style = style;
        }

        public String getApi() {
            return api;
        }

        public void setApi(String api) {
            this.api = api;
        }

        public ApiConfig getApiConfig() {
            return apiConfig;
        }

        public void setApiConfig(ApiConfig apiConfig) {
            this.apiConfig = apiConfig;
        }

        public int getSpace() {
            return space;
        }

        public void setSpace(int space) {
            this.space = space;
        }

        public float getBannerWidthRate() {
            return bannerWidthRate;
        }

        public void setBannerWidthRate(float bannerWidthRate) {
            this.bannerWidthRate = bannerWidthRate;
        }

        public String getRoundCorner() {
            return roundCorner;
        }

        public void setRoundCorner(String roundCorner) {
            this.roundCorner = roundCorner;
        }

        public int getCornerRadius() {
            return cornerRadius;
        }

        public void setCornerRadius(int cornerRadius) {
            this.cornerRadius = cornerRadius;
        }

        public String getChangeBgImg() {
            return changeBgImg;
        }

        public void setChangeBgImg(String changeBgImg) {
            this.changeBgImg = changeBgImg;
        }

        public String getIndicatorType() {
            return indicatorType;
        }

        public void setIndicatorType(String indicatorType) {
            this.indicatorType = indicatorType;
        }

        public List<ActivityItemBean> getTabFloorList() {
            return tabFloorList;
        }

        public void setTabFloorList(List<ActivityItemBean> tabFloorList) {
            this.tabFloorList = tabFloorList;
        }

        public String getThemeType() {
            return themeType;
        }

        public void setThemeType(String themeType) {
            this.themeType = themeType;
        }

        public int getRow() {
            return row;
        }

        public void setRow(int row) {
            this.row = row;
        }

        public int getCol() {
            return col;
        }

        public void setCol(int col) {
            this.col = col;
        }

        public TabListConfig getTabListConfig() {
            return tabListConfig;
        }

        public void setTabListConfig(TabListConfig tabListConfig) {
            this.tabListConfig = tabListConfig;
        }

        public SecondTabListConfig getSecondTabListConfig() {
            return secondTabListConfig;
        }

        public void setSecondTabListConfig(SecondTabListConfig secondTabListConfig) {
            this.secondTabListConfig = secondTabListConfig;
        }

        public String getJumpType() {
            return jumpType;
        }

        public void setJumpType(String jumpType) {
            this.jumpType = jumpType;
        }

        public List<String> getParams() {
            return params;
        }

        public void setParams(List<String> params) {
            this.params = params;
        }

        public ShareApperance getShareApperance() {
            return shareApperance;
        }

        public void setShareApperance(ShareApperance shareApperance) {
            this.shareApperance = shareApperance;
        }

        public String getIsShowMore() {
            return isShowMore;
        }

        public void setIsShowMore(String isShowMore) {
            this.isShowMore = isShowMore;
        }

        public String getIndicatorNormalColor() {
            return indicatorNormalColor;
        }

        public void setIndicatorNormalColor(String indicatorNormalColor) {
            this.indicatorNormalColor = indicatorNormalColor;
        }

        public String getIndicatorPressColor() {
            return indicatorPressColor;
        }

        public void setIndicatorPressColor(String indicatorPressColor) {
            this.indicatorPressColor = indicatorPressColor;
        }

        public String getIndicatorPosition() {
            return indicatorPosition;
        }

        public void setIndicatorPosition(String indicatorPosition) {
            this.indicatorPosition = indicatorPosition;
        }

        public float getMaxShowNum() {
            return maxShowNum;
        }

        public void setMaxShowNum(float maxShowNum) {
            this.maxShowNum = maxShowNum;
        }

        public SearchObj getSearchObj() {
            return searchObj;
        }

        public void setSearchObj(SearchObj searchObj) {
            this.searchObj = searchObj;
        }


        public AnnConfig getAnnConfig() {
            return annConfig;
        }

        public void setAnnConfig(AnnConfig annConfig) {
            this.annConfig = annConfig;
        }

        public List<TabBean> getTabList() {
            return tabList;
        }

        public int getInnerBorder() {
            return innerBorder;
        }

        public void setInnerBorder(int innerBorder) {
            this.innerBorder = innerBorder;
        }

        public List<Integer> getExternalBorder() {
            return externalBorder;
        }

        public void setExternalBorder(List<Integer> externalBorder) {
            this.externalBorder = externalBorder;
        }

        public List<ActBean> getAdsList() {
            return adsList;
        }

        public void setAdsList(List<ActBean> adsList) {
            this.adsList = adsList;
        }

        public void setTabList(List<TabBean> tabList) {
            this.tabList = tabList;
        }

        public String getTemplate() {
            return template;
        }

        public void setTemplate(String template) {
            this.template = template;
        }

        public String getBackgroundColor() {
            return backgroundColor;
        }

        public void setBackgroundColor(String backgroundColor) {
            this.backgroundColor = backgroundColor;
        }

        public String getBackgroundImgUrl() {
            return backgroundImgUrl;
        }

        public void setBackgroundImgUrl(String backgroundImgUrl) {
            this.backgroundImgUrl = backgroundImgUrl;
        }

        public int getWidth() {
            return width;
        }

        public void setWidth(int width) {
            this.width = width;
        }

        public int getHeight() {
            return height;
        }

        public void setHeight(int height) {
            this.height = height;
        }

        public int getFloorNum() {
            return floorNum;
        }

        public void setFloorNum(int floorNum) {
            this.floorNum = floorNum;
        }

        public List<ProductBean> getWaresList() {
            return waresList;
        }

        public void setWaresList(List<ProductBean> waresList) {
            this.waresList = waresList;
        }

        public WareConfig getWaresListConfig() {
            return waresListConfig;
        }

        public void setWaresListConfig(WareConfig waresListConfig) {
            this.waresListConfig = waresListConfig;
        }
    }


    public static class SearchObj {
        private String hint;
        String jumpType;
        List<String> params;

        public String getJumpType() {
            return jumpType;
        }

        public void setJumpType(String jumpType) {
            this.jumpType = jumpType;
        }

        public List<String> getParams() {
            return params;
        }

        public void setParams(List<String> params) {
            this.params = params;
        }

        public String getHint() {
            return hint;
        }

        public void setHint(String hint) {
            this.hint = hint;
        }
    }

    public static class TabBean {
        private String groupId;
        private String name;
        private int floorNum;//与此tab相关联的楼层
        private String isCategory;
        private String belongType;
        private String categoryLevel;
        private List<SecondTabBean> secondTabList;
        private List<ActivityItemBean> tabFloorList;

        public List<ActivityItemBean> getTabFloorList() {
            return tabFloorList;
        }

        public void setTabFloorList(List<ActivityItemBean> tabFloorList) {
            this.tabFloorList = tabFloorList;
        }

        public int getFloorNum() {
            return floorNum;
        }

        public void setFloorNum(int floorNum) {
            this.floorNum = floorNum;
        }

        public String getGroupId() {
            return groupId;
        }

        public void setGroupId(String groupId) {
            this.groupId = groupId;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public List<SecondTabBean> getSecondTabList() {
            return secondTabList;
        }

        public void setSecondTabList(List<SecondTabBean> secondTabList) {
            this.secondTabList = secondTabList;
        }

        public String getIsCategory() {
            return isCategory;
        }

        public void setIsCategory(String isCategory) {
            this.isCategory = isCategory;
        }

        public String getBelongType() {
            return belongType;
        }

        public void setBelongType(String belongType) {
            this.belongType = belongType;
        }

        public String getCategoryLevel() {
            return categoryLevel;
        }

        public void setCategoryLevel(String categoryLevel) {
            this.categoryLevel = categoryLevel;
        }

        @Override
        public String toString() {
            return name;
        }
    }

    public static class SecondTabBean {
        private String secondTabId;
        private String name;
        private String key;
        private String categoryLevel;

        public String getSecondTabId() {
            return secondTabId;
        }

        public void setSecondTabId(String secondTabId) {
            this.secondTabId = secondTabId;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }

        public String getCategoryLevel() {
            return categoryLevel;
        }

        public void setCategoryLevel(String categoryLevel) {
            this.categoryLevel = categoryLevel;
        }
    }

    public static class WareProduct {
        private String productId;
        private String promotionTag;
        private String productPrice;
        private String saleEarning;
        private String productName;
        private String productIcon;
        private String skuId;

        public String getProductId() {
            return productId;
        }

        public void setProductId(String productId) {
            this.productId = productId;
        }

        public String getPromotionTag() {
            return promotionTag;
        }

        public void setPromotionTag(String promotionTag) {
            this.promotionTag = promotionTag;
        }

        public String getProductPrice() {
            return productPrice;
        }

        public void setProductPrice(String productPrice) {
            this.productPrice = productPrice;
        }

        public String getSaleEarning() {
            return saleEarning;
        }

        public void setSaleEarning(String saleEarning) {
            this.saleEarning = saleEarning;
        }

        public String getProductName() {
            return productName;
        }

        public void setProductName(String productName) {
            this.productName = productName;
        }

        public String getProductIcon() {
            return productIcon;
        }

        public void setProductIcon(String productIcon) {
            this.productIcon = productIcon;
        }

        public String getSkuId() {
            return skuId;
        }

        public void setSkuId(String skuId) {
            this.skuId = skuId;
        }
    }

    public static class WareConfig {
        private String cardColor;
        private String zdPrice;
        private String mainPriceColor;
        private String colorDetailConfig;
        private String titleTextColor;
        private String assistPriceColor;
        private String showMore;
        private String direction;
        private String tagPosition;
        int innerBorder;
        int externalBorder;

        private String isShowSale;// "是否展示自销获利，如果1代表有自销获利，0则判断是否有划价"

        public String getIsShowSale() {
            return isShowSale;
        }

        public void setIsShowSale(String isShowSale) {
            this.isShowSale = isShowSale;
        }

        public int getInnerBorder() {
            return innerBorder;
        }

        public void setInnerBorder(int innerBorder) {
            this.innerBorder = innerBorder;
        }

        public int getExternalBorder() {
            return externalBorder;
        }

        public void setExternalBorder(int externalBorder) {
            this.externalBorder = externalBorder;
        }

        public String getCardColor() {
            return cardColor;
        }

        public void setCardColor(String cardColor) {
            this.cardColor = cardColor;
        }

        public String getZdPrice() {
            return zdPrice;
        }

        public void setZdPrice(String zdPrice) {
            this.zdPrice = zdPrice;
        }

        public String getMainPriceColor() {
            return mainPriceColor;
        }

        public void setMainPriceColor(String mainPriceColor) {
            this.mainPriceColor = mainPriceColor;
        }

        public String getColorDetailConfig() {
            return colorDetailConfig;
        }

        public void setColorDetailConfig(String colorDetailConfig) {
            this.colorDetailConfig = colorDetailConfig;
        }

        public String getTitleTextColor() {
            return titleTextColor;
        }

        public void setTitleTextColor(String titleTextColor) {
            this.titleTextColor = titleTextColor;
        }

        public String getAssistPriceColor() {
            return assistPriceColor;
        }

        public void setAssistPriceColor(String assistPriceColor) {
            this.assistPriceColor = assistPriceColor;
        }

        public String getShowMore() {
            return showMore;
        }

        public void setShowMore(String showMore) {
            this.showMore = showMore;
        }

        public String getDirection() {
            return direction;
        }

        public void setDirection(String direction) {
            this.direction = direction;
        }

        public String getTagPosition() {
            return tagPosition;
        }

        public void setTagPosition(String tagPosition) {
            this.tagPosition = tagPosition;
        }
    }

    /**
     * 活动类型
     */
    public static class ActBean {
        private String actId;
        private String actName;
        private String actUrl;
        private String actPicUrl;
        private String actType;
        private int weight;
        private String fontColor;
        private String roundCorner = "0";//[1=设置圆角，0代表不设置，默认是0]
        private int cornerRadius;//圆角半径

        /**
         * 0 h5，1产品， 2跳全局搜索（没关键字），3根据关键字搜索产品列表，
         * 4 去到二级分类页面，5去到三级分类界面（跳转类型有很多，具体看h5与js交互以作参考）
         */
        String jumpType;
        /**
         * (关于参数的说明：参数按one two three优先使用，比如跳产品只需要一个参数就用one
         * 二级分类页面要二个参数就one two，多个参数的对应关系需要统一)
         */
        List<String> params;

        public String getRoundCorner() {
            return roundCorner;
        }

        public void setRoundCorner(String roundCorner) {
            this.roundCorner = roundCorner;
        }

        public int getCornerRadius() {
            return cornerRadius;
        }

        public void setCornerRadius(int cornerRadius) {
            this.cornerRadius = cornerRadius;
        }

        public void setFontColor(String fontColor) {
            this.fontColor = fontColor;
        }

        public String getJumpType() {
            return jumpType;
        }

        public void setJumpType(String jumpType) {
            this.jumpType = jumpType;
        }

        public List<String> getParams() {
            return params;
        }

        public void setParams(List<String> params) {
            this.params = params;
        }

        public String getActId() {
            return actId;
        }

        public void setActId(String actId) {
            this.actId = actId;
        }

        public String getActName() {
            return actName;
        }

        public void setActName(String actName) {
            this.actName = actName;
        }

        public String getActUrl() {
            return actUrl;
        }

        public void setActUrl(String actUrl) {
            this.actUrl = actUrl;
        }

        public String getActPicUrl() {
            return actPicUrl;
        }

        public void setActPicUrl(String actPicUrl) {
            this.actPicUrl = actPicUrl;
        }

        public String getActType() {
            return actType;
        }

        public void setActType(String actType) {
            this.actType = actType;
        }

        public int getWeight() {
            return weight;
        }

        public void setWeight(int weight) {
            this.weight = weight;
        }

        public String getFontColor() {
            return fontColor;
        }


    }

    public static class AnnConfig {
        private String backgroundImg;
        private String useImgFlag;//是否使用背景图片的标志，1代表使用
        private String pictureUrl;
        List<Notice> noticeList;

        public String getBackgroundImg() {
            return backgroundImg;
        }

        public void setBackgroundImg(String backgroundImg) {
            this.backgroundImg = backgroundImg;
        }

        public String getUseImgFlag() {
            return useImgFlag;
        }

        public void setUseImgFlag(String useImgFlag) {
            this.useImgFlag = useImgFlag;
        }

        public String getPictureUrl() {
            return pictureUrl;
        }

        public void setPictureUrl(String pictureUrl) {
            this.pictureUrl = pictureUrl;
        }

        public List<Notice> getNoticeList() {
            return noticeList;
        }

        public void setNoticeList(List<Notice> noticeList) {
            this.noticeList = noticeList;
        }

        public static class Notice implements INoticeBean {
            String noticeId;
            String noticeIcon;
            String noticeLeftIcon;
            String noticeContent;
            String noticeTitle;
            String noticeUrl;
            String jumpType;
            List<String> params;

            public String getNoticeId() {
                return noticeId;
            }

            public void setNoticeId(String noticeId) {
                this.noticeId = noticeId;
            }

            public String getNoticeIcon() {
                return noticeIcon;
            }

            public void setNoticeIcon(String noticeIcon) {
                this.noticeIcon = noticeIcon;
            }

            public String getNoticeLeftIcon() {
                return noticeLeftIcon;
            }

            public void setNoticeLeftIcon(String noticeLeftIcon) {
                this.noticeLeftIcon = noticeLeftIcon;
            }

            public String getNoticeContent() {
                return noticeContent;
            }

            public void setNoticeContent(String noticeContent) {
                this.noticeContent = noticeContent;
            }

            public String getNoticeTitle() {
                return noticeTitle;
            }

            public void setNoticeTitle(String noticeTitle) {
                this.noticeTitle = noticeTitle;
            }

            public String getNoticeUrl() {
                return noticeUrl;
            }

            public void setNoticeUrl(String noticeUrl) {
                this.noticeUrl = noticeUrl;
            }

            public String getJumpType() {
                return jumpType;
            }

            public void setJumpType(String jumpType) {
                this.jumpType = jumpType;
            }

            public List<String> getParams() {
                return params;
            }

            public void setParams(List<String> params) {
                this.params = params;
            }
        }
    }

    public static class ShareApperance {
        String titleTextColor;
        String subTitleColor;
        String subTitleBackgroundUrl;
        String shareIconUrl;
        String shareTextColor;
        String iconBgUrl;

        public String getTitleTextColor() {
            return titleTextColor;
        }

        public void setTitleTextColor(String titleTextColor) {
            this.titleTextColor = titleTextColor;
        }

        public String getSubTitleColor() {
            return subTitleColor;
        }

        public void setSubTitleColor(String subTitleColor) {
            this.subTitleColor = subTitleColor;
        }

        public String getSubTitleBackgroundUrl() {
            return subTitleBackgroundUrl;
        }

        public void setSubTitleBackgroundUrl(String subTitleBackgroundUrl) {
            this.subTitleBackgroundUrl = subTitleBackgroundUrl;
        }

        public String getShareIconUrl() {
            return shareIconUrl;
        }

        public void setShareIconUrl(String shareIconUrl) {
            this.shareIconUrl = shareIconUrl;
        }

        public String getShareTextColor() {
            return shareTextColor;
        }

        public void setShareTextColor(String shareTextColor) {
            this.shareTextColor = shareTextColor;
        }

        public String getIconBgUrl() {
            return iconBgUrl;
        }

        public void setIconBgUrl(String iconBgUrl) {
            this.iconBgUrl = iconBgUrl;
        }
    }

    public static class TabListConfig {
        String backgroundColor;
        String titleTextColor;
        int titleTextSize;
        String titleChoosedColor;
        String titleAheadImgRes;
        String showMore;
        int innerBorder;
        int externalBorder;
        String isShowLine;//是否需要指示线（0 不显示，1显示）
        String lineColor;
        int lineHeight;

        public String getIsShowLine() {
            return isShowLine;
        }

        public void setIsShowLine(String isShowLine) {
            this.isShowLine = isShowLine;
        }

        public String getLineColor() {
            return lineColor;
        }

        public void setLineColor(String lineColor) {
            this.lineColor = lineColor;
        }

        public int getLineHeight() {
            return lineHeight;
        }

        public void setLineHeight(int lineHeight) {
            this.lineHeight = lineHeight;
        }

        public String getBackgroundColor() {
            return backgroundColor;
        }

        public void setBackgroundColor(String backgroundColor) {
            this.backgroundColor = backgroundColor;
        }

        public String getTitleTextColor() {
            return titleTextColor;
        }

        public void setTitleTextColor(String titleTextColor) {
            this.titleTextColor = titleTextColor;
        }

        public int getTitleTextSize() {
            return titleTextSize;
        }

        public void setTitleTextSize(int titleTextSize) {
            this.titleTextSize = titleTextSize;
        }

        public String getTitleChoosedColor() {
            return titleChoosedColor;
        }

        public void setTitleChoosedColor(String titleChoosedColor) {
            this.titleChoosedColor = titleChoosedColor;
        }

        public String getTitleAheadImgRes() {
            return titleAheadImgRes;
        }

        public void setTitleAheadImgRes(String titleAheadImgRes) {
            this.titleAheadImgRes = titleAheadImgRes;
        }

        public String getShowMore() {
            return showMore;
        }

        public void setShowMore(String showMore) {
            this.showMore = showMore;
        }

        public int getInnerBorder() {
            return innerBorder;
        }

        public void setInnerBorder(int innerBorder) {
            this.innerBorder = innerBorder;
        }

        public int getExternalBorder() {
            return externalBorder;
        }

        public void setExternalBorder(int externalBorder) {
            this.externalBorder = externalBorder;
        }
    }

    public static class SecondTabListConfig {
        String backgroundColor;
        String titleTextColor;
        int titleTextSize;
        String titleChoosedColor;
        int innerBorder;
        int externalBorder;
        String normalItemBgColor;
        String selectItemBgColor;

        public String getNormalItemBgColor() {
            return normalItemBgColor;
        }

        public void setNormalItemBgColor(String normalItemBgColor) {
            this.normalItemBgColor = normalItemBgColor;
        }

        public String getSelectItemBgColor() {
            return selectItemBgColor;
        }

        public void setSelectItemBgColor(String selectItemBgColor) {
            this.selectItemBgColor = selectItemBgColor;
        }

        public String getBackgroundColor() {
            return backgroundColor;
        }

        public void setBackgroundColor(String backgroundColor) {
            this.backgroundColor = backgroundColor;
        }

        public String getTitleTextColor() {
            return titleTextColor;
        }

        public void setTitleTextColor(String titleTextColor) {
            this.titleTextColor = titleTextColor;
        }

        public int getTitleTextSize() {
            return titleTextSize;
        }

        public void setTitleTextSize(int titleTextSize) {
            this.titleTextSize = titleTextSize;
        }

        public String getTitleChoosedColor() {
            return titleChoosedColor;
        }

        public void setTitleChoosedColor(String titleChoosedColor) {
            this.titleChoosedColor = titleChoosedColor;
        }

        public int getInnerBorder() {
            return innerBorder;
        }

        public void setInnerBorder(int innerBorder) {
            this.innerBorder = innerBorder;
        }

        public int getExternalBorder() {
            return externalBorder;
        }

        public void setExternalBorder(int externalBorder) {
            this.externalBorder = externalBorder;
        }
    }

    public static class ApiConfig {
        String api;//接口地址
        String params;//参数

        public String getApi() {
            return api;
        }

        public void setApi(String api) {
            this.api = api;
        }

        public String getParams() {
            return params;
        }

        public void setParams(String params) {
            this.params = params;
        }
    }

    public static class SplashInfo implements Serializable {
        String jumpType;
        List<String> params;
        String picUrl;//"图片地址"

        public String getJumpType() {
            return jumpType;
        }

        public void setJumpType(String jumpType) {
            this.jumpType = jumpType;
        }

        public List<String> getParams() {
            return params;
        }

        public void setParams(List<String> params) {
            this.params = params;
        }

        public String getPicUrl() {
            return picUrl;
        }

        public void setPicUrl(String picUrl) {
            this.picUrl = picUrl;
        }
    }
}

