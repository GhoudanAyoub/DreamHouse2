package com.example.dream_house2.di;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.example.dream_house2.Modules.Post;

@Database(entities = {Post.class},version = 1)
public abstract class PostDb extends RoomDatabase {
    public abstract PostDoa getDoa();
}
