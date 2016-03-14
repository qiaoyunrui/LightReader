package com.qiao.androidlab.lightreader.Parts;

import java.io.Serializable;

/**
 * User
 *
 * @author: 乔云瑞
 * @time: 2016/3/14 12:12
 */
public class User implements Serializable {
    private String username;
    private int uid;
    private int countOfImages = 0;  //上传图片数量

    public User(int uid, String username) {
        this.username = username;
        this.uid = uid;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getCountOfImages() {
        return countOfImages;
    }

    public void setCountOfImages(int countOfImages) {
        this.countOfImages = countOfImages;
    }
}
