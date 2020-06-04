package com.example.dream_house2.ui.booked;

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
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class BookedViewModel extends ViewModel {
    private MutableLiveData<List<Post>> postMutableLiveData;
    private List<Post> postList = new ArrayList<>();

    public LiveData<List<Post>> getMyPostMutableLiveData() {
        if (postMutableLiveData == null) postMutableLiveData = new MutableLiveData<>();
        return postMutableLiveData;
    }

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
                                        postList.add(post);
                                    }
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
        FireBaseClient.GetInstance().getFirebaseFirestore()
                .collection(common.Post_DataBase_Table)
                .get()
                .addOnCompleteListener(task -> {
                    try {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult())) {
                                Post post = document.toObject(Post.class);
                                if (post.getPost_owner().equals(common.Current_Client)) {
                                    postList.add(post);
                                }
                            }
                            postMutableLiveData.setValue(postList);
                        }
                    } catch (Throwable t) {
                        Log.e("GetPostException", Objects.requireNonNull(t.getMessage()));
                    }
                });
    }

}
