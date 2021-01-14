package com.example.dream_house2.Modules;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.List;

@Entity(tableName = "post")
public class Post implements Parcelable {

    @PrimaryKey(autoGenerate = true)
    private int id;
    private String post_owner;
    private String city;
    private String price;
    private String room;
    private int rate;
    private String images;
    private String description;
    private String home_type;
    private String Num;

    public Post() { }

    public Post(String post_owner, String city, String price, String room, int rate, String images, String description, String home_type, String num) {
        this.post_owner = post_owner;
        this.city = city;
        this.price = price;
        this.room = room;
        this.rate = rate;
        this.images = images;
        this.description = description;
        this.home_type = home_type;
        this.Num = num;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    protected Post(Parcel in) {
        post_owner = in.readString();
        city = in.readString();
        price = in.readString();
        room = in.readString();
        rate = in.readInt();
        images = in.readString();
        description = in.readString();
        home_type = in.readString();
        Num = in.readString();
    }

    public String getNum() {
        return Num;
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


    public static final Creator<Post> CREATOR = new Creator<Post>() {
        @Override
        public Post createFromParcel(Parcel in) {
            return new Post(in);
        }

        @Override
        public Post[] newArray(int size) {
            return new Post[size];
        }
    };

    public void setNum(String num) {
        Num = num;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        try {
            dest.writeString(post_owner);
            dest.writeString(city);
            dest.writeString(price);
            dest.writeString(room);
            dest.writeInt(rate);
            dest.writeString(images);
            dest.writeString(description);
            dest.writeString(home_type);
            dest.writeString(Num);
        } catch (Exception e) {
        }
    }

}
