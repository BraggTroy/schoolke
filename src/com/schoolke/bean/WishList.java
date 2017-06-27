package com.schoolke.bean;

/**
 * Created by Bragg Troy on 2017/3/20.
 */
public class WishList {
    private int id;
    private PreGoods preGoods;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public PreGoods getPreGoods() {
        return preGoods;
    }

    public void setPreGoods(PreGoods preGoods) {
        this.preGoods = preGoods;
    }

    public WishList(int id, PreGoods preGoods) {
        this.id = id;
        this.preGoods = preGoods;
    }

    public WishList() {
    }
}
