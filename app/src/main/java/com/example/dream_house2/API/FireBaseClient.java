package com.example.dream_house2.API;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class FireBaseClient {
    private static FireBaseClient fireBaseClient;
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;

    private FireBaseClient() {
    }

    public static FireBaseClient GetInstance() {
        if (fireBaseClient == null) {
            fireBaseClient = new FireBaseClient();
        }
        return fireBaseClient;
    }

    public FirebaseAuth getFirebaseAuth() {
        if (firebaseAuth == null) {
            firebaseAuth = FirebaseAuth.getInstance();
        }
        return firebaseAuth;
    }

    public FirebaseFirestore getFirebaseFirestore() {
        if (firebaseFirestore == null) {
            firebaseFirestore = FirebaseFirestore.getInstance();
        }
        return firebaseFirestore;
    }

}
