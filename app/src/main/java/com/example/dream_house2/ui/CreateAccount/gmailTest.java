package com.example.dream_house2.ui.CreateAccount;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProviders;

import com.example.dream_house2.Base_Home;
import com.example.dream_house2.R;
import com.google.android.material.textfield.TextInputLayout;
import com.jakewharton.rxbinding3.view.RxView;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import kotlin.Unit;

public class gmailTest extends Fragment {

    private GmailTestViewModel mViewModel;
    private TextInputLayout Email, Password, Password2, name;
    private CompositeDisposable disposable = new CompositeDisposable();
    private String phone;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.gmail_test_fragment, container, false);

        mViewModel = ViewModelProviders.of(requireActivity()).get(GmailTestViewModel.class);
        Email = root.findViewById(R.id.EmailSignin);
        Password = root.findViewById(R.id.PassworSignin);
        Password2 = root.findViewById(R.id.Password2Signin);
        name = root.findViewById(R.id.NAMESignin);

        mViewModel.createAccount(Email, Password);
        RxView.clicks(root.findViewById(R.id.save))
                .throttleFirst(5, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Unit>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        disposable.add(d);
                    }

                    @Override
                    public void onNext(Unit unit) {
                        if (!validateForm()) return;
                        if (!Matches()) return;
                        mViewModel.getBooleanMutableLiveData().observe((LifecycleOwner) root, task -> {
                            if (task) {
                                Toast.makeText(getActivity(), "Created successfully\uD83D\uDE04 ", Toast.LENGTH_LONG).show();
                                assert getArguments() != null;
                                phone = getArguments().getString("phone");
                                mViewModel.AddUser(Objects.requireNonNull(Email.getEditText()).getText().toString(), Objects.requireNonNull(Password.getEditText()).getText().toString(),
                                        Objects.requireNonNull(name.getEditText()).getText().toString(), phone);
                                startActivity(new Intent(requireActivity(), Base_Home.class));
                            } else {
                                Toast.makeText(getActivity(), "We faced A problem while creating your account\uD83D\uDE14", Toast.LENGTH_LONG).show();
                            }
                        });
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(" ", Objects.requireNonNull(e.getMessage()));
                    }

                    @Override
                    public void onComplete() {
                    }
                });
        return root;
    }

    private boolean Matches() {
        boolean valid = true;
        if (!Objects.requireNonNull(Password.getEditText()).getText().toString().matches(Objects.requireNonNull(Password2.getEditText()).getText().toString())) {
            Password.setError("الا يطابق");
            Password2.setError("لا يطابق");
            valid = false;
        } else {
            Password.setError(null);
            Password2.setError(null);
        }
        return valid;
    }

    private boolean validateForm() {
        boolean valid = true;

        String email1 = Objects.requireNonNull(Email.getEditText()).getText().toString();
        if (TextUtils.isEmpty(email1)) {
            Email.setError("املأ الفراغ من فضلك!!");
            valid = false;
        } else {
            Email.setError(null);
        }

        String password1 = Objects.requireNonNull(Password.getEditText()).getText().toString();
        if (TextUtils.isEmpty(password1)) {
            Password.setError("املأ الفراغ من فضلك!!");
            valid = false;
        } else {
            Password.setError(null);
        }

        String password2 = Objects.requireNonNull(Password2.getEditText()).getText().toString();
        if (TextUtils.isEmpty(password2)) {
            Password2.setError("املأ الفراغ من فضلك!!");
            valid = false;
        } else {
            Password2.setError(null);
        }
        return valid;
    }
}