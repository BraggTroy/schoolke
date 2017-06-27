package com.schoolke.bean;

/**
 * Created by Bragg Troy on 2017/3/21.
 */
public class Rules {
    private int id;
    private String type;
    private String text;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Rules(int id, String type, String text) {
        this.id = id;
        this.type = type;
        this.text = text;
    }

    public Rules() {
    }
}
