package com.example.dream_house2.ui.NewPost;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.dream_house2.API.FireBaseClient;
import com.example.dream_house2.Modules.Post;
import com.example.dream_house2.R;
import com.example.dream_house2.common.common;
import com.glide.slider.library.SliderLayout;
import com.glide.slider.library.animations.DescriptionAnimation;
import com.glide.slider.library.slidertypes.BaseSliderView;
import com.glide.slider.library.slidertypes.DefaultSliderView;
import com.glide.slider.library.tricks.ViewPagerEx;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.jakewharton.rxbinding3.view.RxView;

import org.jetbrains.annotations.NotNull;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import kotlin.Unit;
import retrofit2.http.Url;

@SuppressLint({"SetTextI18n","NonConstantResourceId","CheckResult"})
public class NewPost extends AppCompatActivity implements BaseSliderView.OnSliderClickListener,
        ViewPagerEx.OnPageChangeListener{

    private final static int Image_Request = 1;
    private ArrayList<Uri> ImageList2 = new ArrayList<Uri>();
    private EditText cityadd, descadd;
    private SeekBar priceadd, roomadd;
    private RadioGroup radioGroup;
    private String  price, room, type;
    private ProgressDialog progressDialog;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private TextView textView21, textView22,number;
    private String phone;
    private Post post;
    private String strNameList ;
    private SliderLayout mDemoSlider;
    ArrayList<String> listUrl = new ArrayList<>();
    final String key = FirebaseDatabase.getInstance().getReference()
            .child(common.Post_DataBase_Table).push().getKey();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_post);

        _Views();

        findViewById(R.id.imageButton2).setOnClickListener(b -> openImageFile());
        RxView.clicks(findViewById(R.id.add))
                .throttleFirst(5, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Unit>() {
                    @Override
                    public void onSubscribe(@NotNull Disposable d) {
                        compositeDisposable.add(d);
                    }

                    @Override
                    public void onNext(@NotNull Unit unit) {
                        progressDialog.show();
                        upload(cityadd.getText().toString(), price, room, descadd.getText().toString(), type);
                    }

                    @Override
                    public void onError(@NotNull Throwable e) {
                        Log.e("AddPost", Objects.requireNonNull(e.getMessage()));
                    }

                    @Override
                    public void onComplete() {
                        progressDialog.dismiss();
                    }
                });
    }

    private void _Views() {
        mDemoSlider = findViewById(R.id.pager);
        textView21 = findViewById(R.id.textView21);
        textView22 = findViewById(R.id.textView22);
        cityadd = findViewById(R.id.cityadd);
        descadd = findViewById(R.id.descadd);
        priceadd = findViewById(R.id.priceadd);
        roomadd = findViewById(R.id.roomadd);
        radioGroup = findViewById(R.id.group);
        number = findViewById(R.id.number);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Uploading ..........");


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


    }


    private void openImageFile() {
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
                        listUrl.add(imageuri.getLastPathSegment());

                    }
                    number.setVisibility(View.VISIBLE);
                    number.setText(ImageList2.size() + " Pictures Selected ");

                    Glide.with(getApplicationContext())
                            .load(ImageList2.get(1));
/*
                    RequestOptions requestOptions = new RequestOptions();
                    requestOptions.centerCrop();
                    requestOptions.placeholder(R.drawable.img);

                    for (int i = 0; i < listUrl.size(); i++) {
                        DefaultSliderView sliderView = new DefaultSliderView(getApplicationContext());
                        sliderView
                                .image(listUrl.get(i))
                                .setRequestOption(requestOptions)
                                .setProgressBarVisible(true)
                                .setOnSliderClickListener(this);

                        //add your extra information
                        sliderView.bundle(new Bundle());
                        sliderView.getBundle().putString("extra", "House Image");
                        mDemoSlider.addSlider(sliderView);
                    }

                    // set Slider Transition Animation
                    mDemoSlider.setPresetTransformer(SliderLayout.Transformer.Default);
                    mDemoSlider.setPresetTransformer(SliderLayout.Transformer.Accordion);
                    mDemoSlider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
                    mDemoSlider.setCustomAnimation(new DescriptionAnimation());
                    mDemoSlider.setDuration(4000);
                    mDemoSlider.addOnPageChangeListener(this);



 */
                }
            }
        }
    }


    public void upload(String city, String price, String room, String desc, String type) {
        number.setText("Please Wait ... If Uploading takes Too much time please the button again ");

        progressDialog.show();
        final StorageReference ImageFolder = FirebaseStorage.getInstance().getReference().child("ImageFolder");
        int uploads = 0;
        for (uploads = 0; uploads < ImageList2.size(); uploads++) {
            Uri Image = ImageList2.get(uploads);
            final StorageReference imageName = ImageFolder.child("image/" + Image.getLastPathSegment());

            imageName.putFile(ImageList2.get(uploads))
                    .addOnFailureListener(Throwable::printStackTrace)
                    .addOnSuccessListener(taskSnapshot -> imageName.getDownloadUrl().addOnSuccessListener(uri -> {
                        String url = String.valueOf(uri);
                        if (uri!=null) {
                            strNameList +=","+ url ;
                            post = new Post(common.Current_Client, city, price, room, 1, strNameList, desc, type, phone);
                            SaveData(post);
                        }
                    }));


        }
    }


    private void SaveData(Post post) {
        Map<String,Object> map = new HashMap<>();
        map.put(key,post);
        FirebaseDatabase.getInstance().getReference()
                .child(common.Post_DataBase_Table)
                .updateChildren(map)
                .addOnFailureListener(Throwable::printStackTrace)
                .addOnSuccessListener(task -> {
                    progressDialog.dismiss();
                    Toast.makeText(getApplicationContext(), "Done", Toast.LENGTH_SHORT).show();
                });
    }

    @Override
    public void onSliderClick(BaseSliderView slider) {

    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
