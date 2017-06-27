package com.schoolke.bean;

/**
 * Created by Bragg Troy on 2017/4/6.
 */
public class PreGoods {
    private int id;
    private String goodsName;
    private String schoolPrice;
    private String originPrice;
    private String publishTime;
    private int state;
    private String imageName;
    private User user;
    private int classifyId;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public String getSchoolPrice() {
        return schoolPrice;
    }

    public void setSchoolPrice(String schoolPrice) {
        this.schoolPrice = schoolPrice;
    }

    public String getOriginPrice() {
        return originPrice;
    }

    public void setOriginPrice(String originPrice) {
        this.originPrice = originPrice;
    }

    public String getPublishTime() {
        return publishTime;
    }

    public void setPublishTime(String publishTime) {
        this.publishTime = publishTime;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public int getClassifyId() {
        return classifyId;
    }

    public void setClassifyId(int classifyId) {
        this.classifyId = classifyId;
    }

    public PreGoods(int id, String goodsName, String schoolPrice, String originPrice, String publishTime, int state, String imageName, User user, int classifyId) {
        this.id = id;
        this.goodsName = goodsName;
        this.schoolPrice = schoolPrice;
        this.originPrice = originPrice;
        this.publishTime = publishTime;
        this.state = state;
        this.imageName = imageName;
        this.user = user;
        this.classifyId = classifyId;
    }

    public PreGoods() {
    }
}
