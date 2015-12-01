package com.qiao.androidlab.lightreader.Parts;

import android.graphics.Bitmap;

/**
 * Created by Administrator on 2015/11/30.
 */
public class LightPic {

    private String title = "title";
    private String author = "author";
    private String detail = "I am good,thank you very much!";
    private String time = "2015.11.27";
    private Bitmap bm = null;

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

    public void setTitle(String name) {
        this.title = name;
    }

    public String getTitle() {
        return title;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getAuthor() {
        return author;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getDetail() {
        return detail;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTime() {
        return time;
    }

    public void setBm(Bitmap bm) {
        this.bm = bm;
    }

    public Bitmap getBm() {
        return bm;
    }

}
