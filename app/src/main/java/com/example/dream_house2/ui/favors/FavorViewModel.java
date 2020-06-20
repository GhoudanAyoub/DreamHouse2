package com.example.dream_house2.ui.favors;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.dream_house2.API.FireBaseClient;
import com.example.dream_house2.Modules.favorites;
import com.example.dream_house2.common.common;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class FavorViewModel extends ViewModel {
    private MutableLiveData<List<favorites>> favoritesMutableLiveData;
    private List<favorites> favoritesList = new ArrayList<>();

    public LiveData<List<favorites>> GetFavoritesMutableLiveData() {
        if (favoritesMutableLiveData == null) favoritesMutableLiveData = new MutableLiveData<>();
        return favoritesMutableLiveData;
    }

    public void GetFavors() {
        FireBaseClient.GetInstance().getFirebaseDatabase()
                .getReference(common.Favor_DataBase_Table)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        try {
                            if (dataSnapshot.exists()) {
                                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                    favorites favorite = dataSnapshot1.getValue(favorites.class);
                                    if (Objects.requireNonNull(favorite).getPost().getPost_owner().equals(common.Current_Client)) {
                                        favoritesList.add(favorite);
                                    }
                                }
                                favoritesMutableLiveData.setValue(favoritesList);
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