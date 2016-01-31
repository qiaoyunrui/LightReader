package com.qiao.androidlab.lightreader.Parts;

import java.io.Serializable;

/**
 * SerializableLightPic
 *
 * @author: 乔云瑞
 * @time: 2016/1/31 20:33
 */
public class SerializableLightPic implements Serializable {

    private String title = "title";
    private String author = "author";
    private String detail = "I am good,thank you very much!";
    private String time = "2015.11.27";
    private int id;     //在数据库中的id
    private String path;    //图片路径

    public SerializableLightPic(LightPic lightPic) {
        if (lightPic != null) {
            title = lightPic.getTitle();
            author = lightPic.getAuthor();
            detail = lightPic.getDetail();
            time = lightPic.getTime();
            id = lightPic.getId();
            path = lightPic.getPath();
        }
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public String getDetail() {
        return detail;
    }

    public String getTime() {
        return time;
    }

    public int getId() {
        return id;
    }

    public String getPath() {
        return path;
    }

}
