package com.example.socialapp;

public class Post_model {
    private String userid;
    private String firstname;
    private String lastname;
    private int image;
    private int likes;
    private String caption;
    private String Date;
    private String Time;

    public Post_model(String userid, String firstname, String lastname, int image, int likes, String caption, String date, String time) {
        this.userid = userid;
        this.firstname = firstname;
        this.lastname = lastname;
        this.image = image;
        this.likes = likes;
        this.caption = caption;
        Date = date;
        Time = time;
    }



    public int getImage() {
        return image;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }


    public void setImage(int image) {
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
