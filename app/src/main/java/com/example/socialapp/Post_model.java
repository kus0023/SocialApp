package com.example.socialapp;

public class Post_model {
    private String userid;
    private String image;
    private int likes;
    private String caption;
    private String Date;
    private String Time;

    public Post_model() {
    }

    public Post_model(String userid, String image, int likes, String caption, String date, String time) {
        this.userid = userid;
        this.image = image;
        this.likes = likes;
        this.caption = caption;
        Date = date;
        Time = time;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getTime() {
        return Time;
    }

    public void setTime(String time) {
        Time = time;
    }
}
