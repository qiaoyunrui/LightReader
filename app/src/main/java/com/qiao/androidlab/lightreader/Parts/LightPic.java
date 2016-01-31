package com.qiao.androidlab.lightreader.Parts;

import android.graphics.Bitmap;

import java.io.Serializable;

/**
 * Created by Administrator on 2015/11/30.
 */
public class LightPic implements Serializable {

    private String title = "title";
    private String author = "author";
    private String detail = "I am good,thank you very much!";
    private String time = "2015.11.27";
    private Bitmap bm = null;
    private int id;     //在数据库中的id

    private String path;    //图片路径

    public LightPic() {
    }

    public LightPic(Bitmap bm) {
        this.bm = bm;
    }

    public LightPic(String title, String author, String detial, String time, Bitmap bm) {
        this.title = title;
        this.author = author;
        this.detail = detial;
        this.time = time;
        this.bm = bm;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String name) {
        this.title = name;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public Bitmap getBm() {
        return bm;
    }

    public void setBm(Bitmap bm) {
        this.bm = bm;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

}
