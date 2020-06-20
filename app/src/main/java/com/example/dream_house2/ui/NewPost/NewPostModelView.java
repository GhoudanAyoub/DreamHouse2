package com.example.dream_house2.ui.NewPost;

import android.net.Uri;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.dream_house2.API.FireBaseClient;
import com.example.dream_house2.Modules.Post;
import com.example.dream_house2.common.common;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.StorageReference;

import java.util.Objects;


public class NewPostModelView extends ViewModel {

    private MutableLiveData<String> mutableLiveData ;

    public LiveData<String> getMutableLiveData() {
        if (mutableLiveData==null){
            mutableLiveData = new MutableLiveData<>();
        }
        return mutableLiveData;
    }

    public void upload(String city, String price, String room, String desc, String type, Uri ImageList) {
        final StorageReference ImageFolder =  FireBaseClient.GetInstance().getFirebaseStorage()
                .getReference().child(common.Image_Documment_Storage);

        final StorageReference imagename = ImageFolder.child("image/").child(Objects.requireNonNull(ImageList.getLastPathSegment()));
        imagename.putFile(ImageList).addOnSuccessListener(taskSnapshot -> {
                    Task<Uri> download1 = Objects.requireNonNull(taskSnapshot.getMetadata()).getReference().getDownloadUrl();
                    Post post = new Post(common.Current_Client, city, price, room, null, download1.toString(), desc, type);
                    SaveData(post);
                }
        );
    }
    private void SaveData(Post post){
        FireBaseClient.GetInstance().getFirebaseDatabase()
                .getReference(common.Post_DataBase_Table)
                .push()
                .setValue(post)
                .addOnSuccessListener(aVoid -> mutableLiveData.setValue("Done"));
    }
}
