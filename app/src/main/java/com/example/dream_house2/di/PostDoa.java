package com.example.dream_house2.di;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.dream_house2.Modules.Post;

import java.util.List;

@Dao
public interface PostDoa {

    @Insert
    void insertData(Post post);

    @Query("delete from post where id = :id")
    void DeleteData(int id);

    @Query("select * from post")
    LiveData<List<Post>> getPostList();
}
