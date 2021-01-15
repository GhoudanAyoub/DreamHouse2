package com.example.dream_house2.di;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.example.dream_house2.Modules.Post;

@Database(entities = {Post.class},exportSchema = true,version = 1)
@TypeConverters(Converters.class)
public abstract class PostDb extends RoomDatabase {
    public abstract PostDoa getDoa();
}
