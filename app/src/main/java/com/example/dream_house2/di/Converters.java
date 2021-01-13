package com.example.dream_house2.di;

import androidx.room.TypeConverter;

import com.google.gson.Gson;

import java.util.List;

public class Converters {
    @TypeConverter
    public String FromListToString(List<String> images) {
        return new Gson().toJson(images);
    }

    @TypeConverter
    public List FromStringToList(String json) {
        return new Gson().fromJson(json, List.class);
    }

}
