package com.example.dream_house2.ui.profile;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.dream_house2.API.FireBaseClient;
import com.example.dream_house2.Modules.User;
import com.example.dream_house2.common.common;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.Objects;

public class ProfileViewModel extends ViewModel {
    private MutableLiveData<User> UserMutableLiveData;

    public LiveData<User> getUsersMutableLiveData() {
        if (UserMutableLiveData == null) UserMutableLiveData = new MutableLiveData<>();
        return UserMutableLiveData;
    }

    public void GetUsers() {
        FireBaseClient.GetInstance().getFirebaseFirestore()
                .collection(common.Users_DataBase_Table)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult())) {
                            try {
                                UserMutableLiveData.setValue(document.toObject(User.class));
                            } catch (Throwable t) {
                                Log.e("GetUserException", Objects.requireNonNull(t.getMessage()));
                            }
                        }
                    }
                });
    }
}
