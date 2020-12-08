package com.example.dream_house2.di;

import android.app.Application;

import androidx.room.Room;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.android.components.ApplicationComponent;

@Module
@InstallIn(ApplicationComponent.class)
public class DbModule {

    @Singleton
    @Provides
    public static PostDb ProvideDb(Application application){
        return Room.databaseBuilder(application
        ,PostDb.class
        ,"PostDb")
                .fallbackToDestructiveMigration()
                .allowMainThreadQueries()
                .build();
    }


    @Singleton
    @Provides
    public PostDoa ProvideDoa(PostDb postDb){
        return postDb.getDoa();
    }
}
