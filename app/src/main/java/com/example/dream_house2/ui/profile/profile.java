package com.example.dream_house2.ui.profile;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.dream_house2.API.FireBaseClient;
import com.example.dream_house2.Login;
import com.example.dream_house2.Modules.User;
import com.example.dream_house2.R;
import com.example.dream_house2.common.common;
import com.example.dream_house2.ui.NewPost.NewPost;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.storage.StorageReference;
import com.jakewharton.rxbinding3.view.RxView;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import kotlin.Unit;

import static android.app.Activity.RESULT_OK;

public class profile extends Fragment {

    private final static int Image_Request = 1;
    private TextView name, date;
    private Uri ImageList = null;
    private ImageView image;
    private TextInputLayout email, password, phone;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private User users;
    private ProgressDialog progressDialog;

    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.profile_fragment, container, false);

        name = root.findViewById(R.id.textView7);
        image = root.findViewById(R.id.profile_image);
        email = root.findViewById(R.id.emailedittext);
        phone = root.findViewById(R.id.phoneedittext);
        password = root.findViewById(R.id.passedittext);
        date = root.findViewById(R.id.textView11);


        progressDialog = new ProgressDialog(requireContext());
        progressDialog.setMessage("Uploading ..........");

        root.findViewById(R.id.logout).setOnClickListener(v -> {
            FireBaseClient.GetInstance().getFirebaseAuth().signOut();
            startActivity(new Intent(requireActivity(), Login.class));
        });
        root.findViewById(R.id.imageView).setOnClickListener(v -> openImageFile());
        root.findViewById(R.id.imageButton).setOnClickListener(v -> {
            Intent i = new Intent(requireActivity(), NewPost.class);
        if (users.getPhone()!=null){
            i.putExtra("num",users.getPhone());
        }
            startActivity(i);
        });
        RxView.clicks(root.findViewById(R.id.save))
                .throttleFirst(3, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Unit>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        compositeDisposable.add(d);
                    }

                    @Override
                    public void onNext(Unit unit) {
                        if (ImageList != null) UpdateUser(users);
                        progressDialog.show();
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("GetProfileException", Objects.requireNonNull(e.getMessage()));
                    }

                    @Override
                    public void onComplete() {
                    }
                });
        return root;
    }

    private void openImageFile() {
        Intent galinten = new Intent();
        galinten.setType("image/*");
        galinten.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(galinten, "SELECT IMAGE"), Image_Request);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if (requestCode == Image_Request && resultCode == RESULT_OK && data != null && data.getData() != null) {
                ImageList = data.getData();
                Glide.with(this).load(ImageList).into(image);
            }
        } catch (Exception e) {
            Log.e("AddPost", e.getMessage());
        }
    }

    private void UpdateUser(User u) {
        final StorageReference ImageFolder = FireBaseClient.GetInstance().getFirebaseStorage()
                .getReference().child(common.Users_DataBase_Table);
        ImageFolder.child("PDP/")
                .child(Objects.requireNonNull(ImageList.getLastPathSegment()))
                .putFile(ImageList).addOnSuccessListener(taskSnapshot ->

                ImageFolder.child("PDP/")
                        .child(Objects.requireNonNull(ImageList.getLastPathSegment()))
                        .getDownloadUrl().addOnSuccessListener(uri ->
                        FireBaseClient.GetInstance().getFirebaseFirestore()
                                .collection(common.Users_DataBase_Table)
                                .document(u.getName())
                                .update("image", uri.toString())
                                .addOnCompleteListener(task -> {
                                    if (task.isSuccessful()) {
                                        progressDialog.dismiss();
                                        Log.e("", "Updated successes");
                                    }
                                })
                ));

    }

    @Override
    public void onStart() {
        super.onStart();
        GetUsers();
    }

    private void GetUsers() {
        FireBaseClient.GetInstance().getFirebaseFirestore()
                .collection(common.Users_DataBase_Table)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult())) {
                            try {
                                users = document.toObject(User.class);
                                if (users.getGmail().equalsIgnoreCase(common.Current_Client_Gmail)) {
                                    name.setText(users.getName());
                                    date.setText("Since " + users.getDate());
                                    Objects.requireNonNull(email.getEditText()).setText(users.getGmail());
                                    Objects.requireNonNull(phone.getEditText()).setText(users.getPhone());
                                    Objects.requireNonNull(password.getEditText()).setText(users.getPassword());
                                    if (users.getImage() != null) {
                                        Glide.with(getContext()).load(users.getImage())
                                                .into(image);
                                    }
                                }
                            } catch (Throwable t) {
                                Log.e("GetUserException", Objects.requireNonNull(t.getMessage()));
                            }
                        }
                    }
                });
    }
}
