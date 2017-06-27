package com.schoolke.bean;

/**
 * Created by Bragg Troy on 2017/3/20.
 */
public class Notice {
    private int id;
    private String name;
    private String time;
    private String text;

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

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Notice(int id, String name, String time, String text) {
        this.id = id;
        this.name = name;
        this.time = time;
        this.text = text;
    }
    public Notice(){}
}
