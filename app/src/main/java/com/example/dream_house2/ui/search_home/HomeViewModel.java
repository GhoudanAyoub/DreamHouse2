package com.example.dream_house2.ui.search_home;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.dream_house2.API.FireBaseClient;
import com.example.dream_house2.Modules.Post;
import com.example.dream_house2.common.common;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class HomeViewModel extends ViewModel {
    private MutableLiveData<List<Post>> postMutableLiveData;
    private List<Post> postList = new ArrayList<>();

    public LiveData<List<Post>> getPostMutableLiveData() {
        if (postMutableLiveData == null) postMutableLiveData = new MutableLiveData<>();
        return postMutableLiveData;
    }

    public void GetPosts() {
        FireBaseClient.GetInstance().getFirebaseDatabase()
                .getReference(common.Post_DataBase_Table)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        try {
                            if (dataSnapshot.exists()) {
                                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                    Post post = dataSnapshot1.getValue(Post.class);
                                    postList.add(post);
                                }
                                postMutableLiveData.setValue(postList);
                            }
                        } catch (Throwable t) {
                            Log.e("GetPostException", Objects.requireNonNull(t.getMessage()));
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                    }
                });
    }
}