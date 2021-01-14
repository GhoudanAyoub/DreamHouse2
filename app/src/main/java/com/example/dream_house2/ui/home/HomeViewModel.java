package com.example.dream_house2.ui.home;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.hilt.lifecycle.ViewModelInject;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.dream_house2.API.FireBaseClient;
import com.example.dream_house2.Modules.Post;
import com.example.dream_house2.common.common;
import com.example.dream_house2.di.Repository;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class HomeViewModel extends ViewModel {
    private final Repository repository;

    private LiveData<List<Post>> FavList = null;
    private List<Post> postList = new ArrayList<>();
    private List<Post> bookedpostList = new ArrayList<>();

    private MutableLiveData<List<Post>> postMutableLiveData;
    private MutableLiveData<List<Post>> bookedMutableLiveData;

    @ViewModelInject
    public HomeViewModel(Repository repository) { this.repository = repository; }

    public LiveData<List<Post>> getFavList() { return FavList; }

    public LiveData<List<Post>> getPostMutableLiveData() { if (postMutableLiveData == null) postMutableLiveData = new MutableLiveData<>();return postMutableLiveData; }

    public LiveData<List<Post>> getbookedMutableLiveData() { if (bookedMutableLiveData == null) bookedMutableLiveData = new MutableLiveData<>();return bookedMutableLiveData; }


    //Post Data
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

    //Booked Data
    public void GetMyPosts() {
        FireBaseClient.GetInstance().getFirebaseDatabase()
                .getReference(common.Post_DataBase_Table)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        try {
                            if (dataSnapshot.exists()) {
                                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                    Post post = dataSnapshot1.getValue(Post.class);
                                    if (Objects.requireNonNull(post).getPost_owner().equals(common.Current_Client)) {
                                        bookedpostList.add(post);
                                    }
                                }
                                bookedMutableLiveData.setValue(bookedpostList);
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

    // Room Data
    public void GetData(){ FavList = repository.getData(); }
    public void DeleteData(int id){repository.DeleteData(id);}
    public void InsertData(Post post){repository.InsertData(post);}

}