package com.example.dream_house2;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.dream_house2.API.FireBaseClient;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.FirebaseApp;
import com.jakewharton.rxbinding3.view.RxView;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import kotlin.Unit;

public class Login extends AppCompatActivity {

    private TextInputLayout email,password;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        FirebaseApp.initializeApp(getApplicationContext());

        email = findViewById(R.id.gmailEditText);
        password = findViewById(R.id.passEditText);

        findViewById(R.id.signup).setOnClickListener(v->startActivity(new Intent(getApplicationContext(),SignIn.class)));
        RxView.clicks(findViewById(R.id.login))
                .throttleFirst(4, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Unit>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        compositeDisposable.add(d);
                    }

                    @Override
                    public void onNext(Unit unit) {
                        LoginUser(Objects.requireNonNull(email.getEditText()).getText().toString(), Objects.requireNonNull(password.getEditText()).getText().toString());
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("", Objects.requireNonNull(e.getMessage()));
                    }

                    @Override
                    public void onComplete() { }
                });
    }
    @Override
    public void onStart() {
        super.onStart();
        if (FireBaseClient.GetInstance().getFirebaseAuth().getCurrentUser()!=null){
            startActivity(new Intent(getApplicationContext(), Base_Home.class)); }
    }
    private void LoginUser(final String email, final String password) {
        if (!validateForm())return;
        try {
            FireBaseClient.GetInstance().getFirebaseAuth()
                    .signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    startActivity(new Intent(getApplicationContext(), Base_Home.class));
                    Toast.makeText(getApplicationContext(), "Welcome\uD83D\uDE04", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Email Or Password Error", Toast.LENGTH_LONG).show();
                }
            });
        } catch (Exception ex) {
            Toast.makeText(getApplicationContext(), " Check Your Connection Network", Toast.LENGTH_LONG).show();
        }
    }
    private boolean validateForm() {
        boolean valid = true;

        String email1 = Objects.requireNonNull(email.getEditText()).getText().toString();
        if (TextUtils.isEmpty(email1)) {
            email.setError("obligatory");
            valid = false;
        } else {
            email.setError(null);
        }

        String password1 = Objects.requireNonNull(password.getEditText()).getText().toString();
        if (TextUtils.isEmpty(password1)) {
            password.setError("obligatory");
            valid = false;
        } else {
            password.setError(null);
        }
        return valid;
    }
    @Override
    public void onBackPressed() { }
    @Override
    protected void onDestroy() { super.onDestroy();compositeDisposable.clear();}

}
