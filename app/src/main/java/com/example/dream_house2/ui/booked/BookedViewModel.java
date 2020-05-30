package com.example.dream_house2.ui.booked;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.dream_house2.API.FireBaseClient;
import com.example.dream_house2.Modules.Post;
import com.example.dream_house2.common.common;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

class BookedViewModel extends ViewModel {
    private MutableLiveData<List<Post>> postMutableLiveData ;
    private List<Post> postList = new ArrayList<>();
    LiveData<List<Post>> getMyPostMutableLiveData() {
        if (postMutableLiveData==null )postMutableLiveData=new MutableLiveData<>();
        return postMutableLiveData;
    }

    void GetMyPosts(){
        FireBaseClient.GetInstance().getFirebaseFirestore()
                .collection(common.Post_DataBase_Table)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()){
                        for (QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult())) {
                            try {
                                Post post = document.toObject(Post.class);
                                if (post.getPost_owner().equals(common.Current_Client)){ postList.add(post);}
                            } catch (Throwable t) {
                                Log.e("GetPostException", Objects.requireNonNull(t.getMessage()));
                            }
                        }
                        postMutableLiveData.setValue(postList);
                    }
                });
    }

}
