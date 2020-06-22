package com.example.dream_house2.ui.favors;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.dream_house2.API.FireBaseClient;
import com.example.dream_house2.Modules.Post;
import com.example.dream_house2.Modules.favorites;
import com.example.dream_house2.common.common;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class FavorViewModel extends ViewModel {
    favorites favorites;
    private MutableLiveData<List<favorites>> favoritesMutableLiveData;
    private List<favorites> favoritesList = new ArrayList<>();

    public LiveData<List<favorites>> GetFavoritesMutableLiveData() {
        if (favoritesMutableLiveData == null) favoritesMutableLiveData = new MutableLiveData<>();
        return favoritesMutableLiveData;
    }

    public void GetFavors() {
        FireBaseClient.GetInstance().getFirebaseFirestore()
                .collection(common.Favor_DataBase_Table)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        try {
                            for (QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult())) {
                                favorites = document.toObject(favorites.class);
                                Post post = favorites.getPost();
                                if (post.getPost_owner().equals(common.Current_Client)) {
                                    favoritesList.add(favorites);
                                }
                            }
                            favoritesMutableLiveData.setValue(favoritesList);
                        } catch (Throwable t) {
                            Log.e("GetUserException", Objects.requireNonNull(t.getMessage()));
                        }
                    }
                });
    }


}