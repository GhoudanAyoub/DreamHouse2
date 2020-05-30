package com.example.dream_house2.ui.NewPost;

import android.net.Uri;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.dream_house2.API.FireBaseClient;
import com.example.dream_house2.Modules.Post;
import com.example.dream_house2.common.common;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class NewPostModelView extends ViewModel {

    private MutableLiveData<String> mutableLiveData ;
    private List<String> ImageUri = new ArrayList<>();

    public LiveData<String> getMutableLiveData() {
        if (mutableLiveData==null){
            mutableLiveData = new MutableLiveData<>();
        }
        return mutableLiveData;
    }

    public void upload(String city, String price, String room, String desc, String type, ArrayList<Uri> ImageList) {
        final StorageReference ImageFolder =  FireBaseClient.GetInstance().getFirebaseStorage()
                .getReference().child(common.Image_Documment_Storage);

        for (int uploads =0; uploads < ImageList.size(); uploads++) {
            Uri Image  = ImageList.get(uploads);
            final StorageReference imagename  = ImageFolder.child("image/"+Image.getLastPathSegment());
            imagename.putFile(ImageList.get(uploads)).addOnSuccessListener(taskSnapshot ->
                    imagename.getDownloadUrl().addOnSuccessListener(uri -> {
                        String url = String.valueOf(uri);
                        ImageUri.add(url);
                    }));
        }
        Post post = new Post(common.Current_Client,city,price,room, null,desc,type,ImageUri);
        SaveData(post);
    }
    private void SaveData(Post post){
        FireBaseClient.GetInstance().getFirebaseFirestore()
                .collection(common.Post_DataBase_Table)
                .document()
                .set(post)
                .addOnSuccessListener(aVoid -> mutableLiveData.setValue("Done"));
    }
}
