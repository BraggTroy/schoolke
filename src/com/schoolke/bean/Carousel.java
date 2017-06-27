package com.schoolke.bean;

/**
 * Created by Bragg Troy on 2017/5/9.
 */
public class Carousel {
    private int id;
    private String name;
    private String link;

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

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public Carousel(int id, String name, String link) {
        this.id = id;
        this.name = name;
        this.link = link;
    }

    public Carousel() {
    }
}
