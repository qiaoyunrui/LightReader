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
    private int id = 1;     //在数据库中的id
    private int serverId = 1;   //在服务器中的id
    private String path;    //图片路径
    private int lon = 0;    //经度
    private int lat = 0;    //纬度

    public SerializableLightPic() {

    }

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

    public void setTitle(String title) {
        this.title = title;
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

    public int getLon() {
        return lon;
    }

    public void setLon(int lon) {
        this.lon = lon;
    }

    public int getLat() {
        return lat;
    }

    public void setLat(int lat) {
        this.lat = lat;
    }

    public int getServerId() {
        return serverId;
    }

    public void setServerId(int serverId) {
        this.serverId = serverId;
    }
}
