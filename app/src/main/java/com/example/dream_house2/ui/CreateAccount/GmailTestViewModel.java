package com.example.dream_house2.ui.CreateAccount;

import android.annotation.SuppressLint;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.dream_house2.API.FireBaseClient;
import com.example.dream_house2.Modules.User;
import com.example.dream_house2.common.common;
import com.google.android.material.textfield.TextInputLayout;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

class GmailTestViewModel extends ViewModel {
    private MutableLiveData<Boolean> booleanMutableLiveData;

    LiveData<Boolean> getBooleanMutableLiveData() {
        if (booleanMutableLiveData == null) booleanMutableLiveData = new MutableLiveData<>();
        return booleanMutableLiveData;
    }

    void createAccount(TextInputLayout Email, TextInputLayout Password) {
        FireBaseClient.GetInstance().getFirebaseAuth()
                .createUserWithEmailAndPassword(Objects.requireNonNull(Email.getEditText()).getText().toString(),
                        Objects.requireNonNull(Password.getEditText()).getText().toString())
                .addOnCompleteListener(t -> {
                    if (t.isSuccessful()) {
                        booleanMutableLiveData.setValue(true);
                    } else {
                        booleanMutableLiveData.setValue(false);
                    }
                });
    }

    @SuppressLint("SimpleDateFormat")
    void AddUser(String gmail, String password, String name, String phone) {
        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
        String formattedDate = df.format(c);
        User user = new User(gmail, password, name, null, phone, formattedDate);
        FireBaseClient.GetInstance().getFirebaseFirestore()
                .collection(common.Users_DataBase_Table)
                .document(name)
                .set(user)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Log.e("", "Add successes");
                    }
                });
    }
}
