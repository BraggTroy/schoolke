package com.schoolke.bean;

/**
 * Created by Bragg Troy on 2017/3/20.
 */
public class Message {
    private int id;
    private String msg;
    private String msgTime;
    private User user;
    private Goods goods;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getMsgTime() {
        return msgTime;
    }

    public void setMsgTime(String msgTime) {
        this.msgTime = msgTime;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Goods getGoods() {
        return goods;
    }

    public void setGoods(Goods goods) {
        this.goods = goods;
    }

    public Message(int id, String msg, String msgTime, User user, Goods goods) {
        this.id = id;
        this.msg = msg;
        this.msgTime = msgTime;
        this.user = user;
        this.goods = goods;
    }

    public Message() {
    }
}
