package com.schoolke.bean;

/**
 * Created by Bragg Troy on 2017/3/20.
 */
public class Goods {
    private int id;
    private String goodsName;
    private String schoolPrice;
    private String originPrice;
    private String sell;
    private String remark;
    private String publishTime;
    private Classify classify;
    private User user;
    private int state;

    public int getId() {
        return id;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public String getSchoolPrice() {
        return schoolPrice;
    }

    public String getOriginPrice() {
        return originPrice;
    }

    public String getSell() {
        return sell;
    }

    public String getRemark() {
        return remark;
    }

    public String getPublishTime() {
        return publishTime;
    }

    public int getState() {
        return state;
    }

    public Classify getClassify() {
        return classify;
    }

    public User getUser() {
        return user;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public void setSchoolPrice(String schoolPrice) {
        this.schoolPrice = schoolPrice;
    }

    public void setOriginPrice(String originPrice) {
        this.originPrice = originPrice;
    }

    public void setSell(String sell) {
        this.sell = sell;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public void setPublishTime(String publishTime) {
        this.publishTime = publishTime;
    }

    public void setState(int state) {
        this.state = state;
    }

    public void setClassify(Classify classify) {
        this.classify = classify;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Goods(int id, String goodsName, String schoolPrice, String originPrice, String sell, String remark, String publishTime, Classify classify, User user, int state) {
        this.id = id;
        this.goodsName = goodsName;
        this.schoolPrice = schoolPrice;
        this.originPrice = originPrice;
        this.sell = sell;
        this.remark = remark;
        this.publishTime = publishTime;
        this.classify = classify;
        this.user = user;
        this.state = state;
    }

    public Goods() {
    }
}
