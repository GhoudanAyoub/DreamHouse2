package com.example.dream_house2.ui.NewPost;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.dream_house2.API.FireBaseClient;
import com.example.dream_house2.Modules.Post;
import com.example.dream_house2.R;
import com.example.dream_house2.common.common;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.jakewharton.rxbinding3.view.RxView;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import kotlin.Unit;

public class NewPost extends AppCompatActivity {

    private final static int Image_Request = 1;
    private ArrayList<Uri> ImageList2 = new ArrayList<Uri>();
    private List<String> stringList = new ArrayList<>();
    private TextInputLayout cityadd, descadd;
    private SeekBar priceadd, roomadd;
    private RadioGroup radioGroup;
    private String city, desc, price, room, type;
    private ProgressDialog progressDialog;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private ImageView imageSwitcher;
    private TextView textView21, textView22;
    private String phone;
    private Post post;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_post);


        textView21 = findViewById(R.id.textView21);
        textView22 = findViewById(R.id.textView22);
        cityadd = findViewById(R.id.cityadd);
        descadd = findViewById(R.id.descadd);
        priceadd = findViewById(R.id.priceadd);
        roomadd = findViewById(R.id.roomadd);
        radioGroup = findViewById(R.id.group);
        if (getIntent().getStringExtra("num")!=null){
            phone = getIntent().getStringExtra("num");
        }else {
            phone="+2126**********";
        }

        priceadd.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                price = String.valueOf(progress);
                textView21.setText(price + " DH");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        roomadd.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                room = String.valueOf(progress);
                textView22.setText(room);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        city = Objects.requireNonNull(cityadd.getEditText()).getText().toString();
        desc = Objects.requireNonNull(descadd.getEditText()).getText().toString();
        radioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            switch (checkedId) {
                case R.id.all:
                    type = "All";
                    break;
                case R.id.house:
                    type = "House";
                    break;
                case R.id.flat:
                    type = "Flat";
                    break;
            }
        });


        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Uploading ..........");
        imageSwitcher = findViewById(R.id.pager);

        findViewById(R.id.imageButton2).setOnClickListener(b -> openImageFile());
        RxView.clicks(findViewById(R.id.add))
                .throttleFirst(5, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Unit>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        compositeDisposable.add(d);
                    }

                    @Override
                    public void onNext(Unit unit) {
                        Toast.makeText(getApplicationContext(), "This will take several times ...", Toast.LENGTH_SHORT).show();
                        progressDialog.show();
                        upload(city, price, room, desc, type);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("AddPost", Objects.requireNonNull(e.getMessage()));
                    }

                    @Override
                    public void onComplete() {
                        progressDialog.dismiss();
                    }
                });
    }


    private void openImageFile() {
        //we will pick images
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        startActivityForResult(intent, Image_Request);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Image_Request) {
            if (resultCode == RESULT_OK) {
                if (data.getClipData() != null) {
                    int count = data.getClipData().getItemCount();

                    int CurrentImageSelect = 0;

                    while (CurrentImageSelect < count) {
                        Uri imageuri = data.getClipData().getItemAt(CurrentImageSelect).getUri();
                        ImageList2.add(imageuri);
                        CurrentImageSelect = CurrentImageSelect + 1;
                    }
                   // textView.setVisibility(View.VISIBLE);
                   // textView.setText("You Have Selected " + ImageList.size() + " Pictures");
                    //Glide.with(this).load(ImageList).into(imageSwitcher);
                }
            }
        }
    }


    public void upload(String city, String price, String room, String desc, String type) {

        //textView.setText("Please Wait ... If Uploading takes Too much time please the button again ");
        progressDialog.show();
        final StorageReference ImageFolder = FirebaseStorage.getInstance().getReference().child("ImageFolder");
        int uploads = 0;
        for (uploads = 0; uploads < ImageList2.size(); uploads++) {
            Uri Image = ImageList2.get(uploads);
            final StorageReference imageName = ImageFolder.child("image/" + Image.getLastPathSegment());

            imageName.putFile(ImageList2.get(uploads))
                    .addOnFailureListener(Throwable::printStackTrace)
                    .addOnSuccessListener(taskSnapshot ->
                            imageName.getDownloadUrl().addOnSuccessListener(uri -> {
                                String url = String.valueOf(uri);
                                stringList.add(url);
                                post = new Post(common.Current_Client, city, price, room, 1, stringList, desc, type,phone);
                                SaveData(post);
                            }));
        }
    }


    private void SaveData(Post post) {
        FirebaseDatabase.getInstance().getReference().child(common.Post_DataBase_Table)
                .push()
                .setValue(post)
                .addOnFailureListener(Throwable::printStackTrace)
                .addOnCompleteListener(task -> {
                    progressDialog.dismiss();
                    //textView.setText("Image Uploaded Successfully");
                    ImageList2.clear();
                });
        FireBaseClient.GetInstance().getFirebaseFirestore()
                .collection(common.Post_DataBase_Table)
                .document()
                .set(post)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(getApplicationContext(), "Done", Toast.LENGTH_LONG);
                    progressDialog.dismiss();
                });
    }
}
