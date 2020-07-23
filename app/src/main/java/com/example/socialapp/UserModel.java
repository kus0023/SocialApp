package com.example.socialapp;

public class UserModel {
    private String fname;
    private String lname;
    private String email;
    private String phone;
    private String gender;
    private String dob;

    private String profile;
    private String background;

    public UserModel() {
    }

    public UserModel(String fname, String lname, String email, String phone, String gender, String dob, String profile, String background) {
        this.fname = fname;
        this.lname = lname;
        this.email = email;
        this.phone = phone;
        this.gender = gender;
        this.dob = dob;
        this.profile = profile;
        this.background = background;
    }

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getLname() {
        return lname;
    }

    public void setLname(String lname) {
        this.lname = lname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }

    public String getBackground() {
        return background;
    }

    public void setBackground(String background) {
        this.background = background;
    }
}
