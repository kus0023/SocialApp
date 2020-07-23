package com.example.socialapp;

public class Post_model {
    private String image;
    private int likes;
    private String caption;
    private String Date;
    private String Time;

    public String getImage() {
        return image;
    }

    public Post_model(String image, int likes, String caption, String date, String time) {
        this.image = image;
        this.likes = likes;
        this.caption = caption;
        Date = date;
        Time = time;
    }

    public Post_model(String image,String caption){
        this.image = image;
        this.caption = caption;

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

    public Post_model() {
    }
}
