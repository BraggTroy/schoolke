package com.schoolke.bean;

/**
 * Created by Bragg Troy on 2017/3/20.
 */
public class GoodsImages {
    private int id;
    private String name;
    private int goodsId;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(int goodsId) {
        this.goodsId = goodsId;
    }

    public GoodsImages(int id, String name, int goodsId) {
        this.id = id;
        this.name = name;
        this.goodsId = goodsId;
    }

    public GoodsImages() {
    }
}
