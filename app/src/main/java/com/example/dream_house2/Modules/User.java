package com.example.dream_house2.Modules;

import android.net.Uri;

public class User {
    private String gmail;
    private String password;
    private String name;
    private Uri image;

    public User() {
    }

    public User(String gmail, String password, String name, Uri image) {
        this.gmail = gmail;
        this.password = password;
        this.name = name;
        this.image = image;
    }

    public String getGmail() {
        return gmail;
    }

    public void setGmail(String gmail) {
        this.gmail = gmail;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Uri getImage() {
        return image;
    }

    public void setImage(Uri image) {
        this.image = image;
    }
}
