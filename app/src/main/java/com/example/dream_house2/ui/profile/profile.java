package com.example.dream_house2.ui.profile;

import android.annotation.SuppressLint;
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
import androidx.lifecycle.ViewModelProviders;

import com.example.dream_house2.R;
import com.example.dream_house2.common.common;
import com.google.android.material.textfield.TextInputLayout;
import com.jakewharton.rxbinding3.view.RxView;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import kotlin.Unit;

public class profile extends Fragment {

    private ProfileViewModel mViewModel;
    private TextView name, date;
    private ImageView image;
    private TextInputLayout email, password, phone;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mViewModel = ViewModelProviders.of(this).get(ProfileViewModel.class);
        View root = inflater.inflate(R.layout.profile_fragment, container, false);
        mViewModel.GetUsers();
        views(root);

        mViewModel.getUsersMutableLiveData().observe(requireActivity(), users -> {
            if (users.getName().equals(common.Current_Client)){
                name.setText(users.getName());
                date.setText("Since " + users.getDate());
                Objects.requireNonNull(email.getEditText()).setText(users.getGmail());
                Objects.requireNonNull(phone.getEditText()).setText(users.getPhone());
                Objects.requireNonNull(password.getEditText()).setText(users.getPassword());
            }
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

    private void views(View root) {
        name = root.findViewById(R.id.textView7);
        image = root.findViewById(R.id.profile_image);
        email = root.findViewById(R.id.emailedittext);
        password = root.findViewById(R.id.phoneedittext);
        phone = root.findViewById(R.id.passedittext);
        date = root.findViewById(R.id.textView11);
    }
}
