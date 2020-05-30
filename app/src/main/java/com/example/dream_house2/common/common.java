package com.example.dream_house2.common;

import com.example.dream_house2.API.FireBaseClient;

import java.util.Objects;

public class common {

    public static final String Favor_DataBase_Table = "favorite";
    public static final String Post_DataBase_Table = "post";
    public static final String Users_DataBase_Table = "user";
    public static final String Current_Client = Objects.requireNonNull(FireBaseClient.GetInstance().getFirebaseAuth().getCurrentUser()).getDisplayName();

}
