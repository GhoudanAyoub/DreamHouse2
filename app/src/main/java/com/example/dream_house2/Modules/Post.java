package com.example.dream_house2.Modules;

public class Post {
    private String post_owner;
    private String city;
    private String price;
    private String room;
    private int rate;
    private String images;
    private String description;
    private String home_type;

    public Post() { }

    public Post(String post_owner, String city, String price, String room, int rate, String images, String description, String home_type) {
        this.post_owner = post_owner;
        this.city = city;
        this.price = price;
        this.room = room;
        this.rate = rate;
        this.images = images;
        this.description = description;
        this.home_type = home_type;
    }

    public String getHome_type() {
        return home_type;
    }

    public void setHome_type(String home_type) {
        this.home_type = home_type;
    }

    public String getPost_owner() {
        return post_owner;
    }

    public void setPost_owner(String post_owner) {
        this.post_owner = post_owner;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public int getRate() {
        return rate;
    }

    public void setRate(int rate) {
        this.rate = rate;
    }

    public String getImages() {
        return images;
    }

    public void setImages(String images) {
        this.images = images;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
