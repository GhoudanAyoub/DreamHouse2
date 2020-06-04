package com.example.dream_house2.ui.NewPost;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import com.bumptech.glide.Glide;
import com.example.dream_house2.R;
import com.google.android.material.textfield.TextInputLayout;
import com.jakewharton.rxbinding3.view.RxView;

import java.util.ArrayList;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import kotlin.Unit;

public class NewPost extends AppCompatActivity {

    private final static int Image_Request = 1;
    private ArrayList<Uri> ImageList = new ArrayList<>();
    private TextInputLayout cityadd, descadd;
    private SeekBar priceadd, roomadd;
    private RadioGroup radioGroup;
    private String city, desc, price, room, type;
    private NewPostModelView newPostModelView;
    private ProgressDialog progressDialog;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private ImageView imageSwitcher;
    private int cuurentindex = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_post);
        newPostModelView = ViewModelProviders.of(this).get(NewPostModelView.class);
        views();
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Uploading ..........");
        imageSwitcher = findViewById(R.id.pager);

        findViewById(R.id.imageButton3).setOnClickListener(y -> {
            if (cuurentindex > 0) {
                cuurentindex -= 1;
                Glide.with(getApplicationContext())
                        .load(ImageList.get(cuurentindex))
                        .centerCrop()
                        .into(imageSwitcher);
            }
        });
        findViewById(R.id.imageButton4).setOnClickListener(y -> {
            if (cuurentindex < ImageList.size() - 1) {
                cuurentindex += 1;
                Glide.with(getApplicationContext())
                        .load(ImageList.get(cuurentindex))
                        .centerCrop()
                        .into(imageSwitcher);
            }
        });
        findViewById(R.id.imageButton2).setOnClickListener(b -> openImageFile());
        newPostModelView.getMutableLiveData().observe(this, s -> Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show());
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
                        SaveData();
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

    private void views() {
        cityadd = findViewById(R.id.cityadd);
        descadd = findViewById(R.id.descadd);
        priceadd = findViewById(R.id.priceadd);
        roomadd = findViewById(R.id.roomadd);
        radioGroup = findViewById(R.id.group);
        priceadd.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                price = String.valueOf(progress);
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
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        city = String.valueOf(Objects.requireNonNull(cityadd.getEditText()).getText());
        desc = String.valueOf(Objects.requireNonNull(descadd.getEditText()).getText());
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
        Intent galinten = new Intent();
        galinten.setType("image/*");
        galinten.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        galinten.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(galinten, "SELECT IMAGE"), Image_Request);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if (requestCode == Image_Request && resultCode == RESULT_OK && data != null && data.getData() != null) {
                int count = Objects.requireNonNull(data.getClipData()).getItemCount();
                int CurrentImageSelect = 0;
                while (CurrentImageSelect < count) {
                    Uri ImageUri = data.getClipData().getItemAt(CurrentImageSelect).getUri();
                    ImageList.add(ImageUri);
                    CurrentImageSelect = CurrentImageSelect + 1;
                }
            }
        } catch (Exception e) {
            Log.e("AddPost", e.getMessage());
        }
    }

    private void SaveData() {
        newPostModelView.upload(city, price, room, desc, type, ImageList);
    }
}
