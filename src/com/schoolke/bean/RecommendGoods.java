package com.schoolke.bean;

/**
 * Created by Bragg Troy on 2017/4/22.
 */
public class RecommendGoods {
    private int id;
    private Goods goods;
    private int index;
    private String recommendTitle;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Goods getGoods() {
        return goods;
    }

    public void setGoods(Goods goods) {
        this.goods = goods;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String getRecommendTitle() {
        return recommendTitle;
    }

    public void setRecommendTitle(String recommendTitle) {
        this.recommendTitle = recommendTitle;
    }

    public RecommendGoods(int id, Goods goods, int index, String recommendTitle) {
        this.id = id;
        this.goods = goods;
        this.index = index;
        this.recommendTitle = recommendTitle;
    }

    public RecommendGoods() {
    }
}
