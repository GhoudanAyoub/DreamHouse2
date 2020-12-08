package com.example.dream_house2.di;

import androidx.lifecycle.LiveData;

import com.example.dream_house2.Modules.Post;

import java.util.List;

import javax.inject.Inject;

public class Repository {
    private PostDoa postDoa;

    @Inject
    public Repository(PostDoa postDoa) {
        this.postDoa = postDoa;
    }

    public void InsertData(Post post){postDoa.insertData(post);}
    public void DeleteData(int id){postDoa.DeleteData(id);}
    public LiveData<List<Post>> getData(){return postDoa.getPostList();}
}
