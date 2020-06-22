package com.example.dream_house2.ui.GlobalInfo;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.dream_house2.API.FireBaseClient;
import com.example.dream_house2.Modules.Post;
import com.example.dream_house2.Modules.favorites;
import com.example.dream_house2.R;
import com.example.dream_house2.common.common;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.jakewharton.rxbinding3.view.RxView;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import kotlin.Unit;

public class FullInfo extends Fragment {

    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.full_info_fragment, container, false);

        ImageView imageViewList = root.findViewById(R.id.imageViewlist);
        FloatingActionButton bookNow = root.findViewById(R.id.fullinfo_floatingActionButton3);
        FloatingActionButton favorButton = root.findViewById(R.id.fullinfo_floatingActionButton2);
        RatingBar fullinfor_rate = root.findViewById(R.id.fullinfor_rate);
        TextView fullinfo_owner_home = root.findViewById(R.id.fullinfo_owner_home);
        TextView fullinfo_city = root.findViewById(R.id.fullinfo_city);
        TextView fullinfo_price = root.findViewById(R.id.fullinfo_price);
        TextView fullinfo_room = root.findViewById(R.id.fullinfo_room);
        TextView fullinfo_desc = root.findViewById(R.id.fullinfo_desc);

        assert getArguments() != null;
        Post post = getArguments().getParcelable("post");
        assert post != null;
        Glide.with(requireActivity())
                .load(post.getImages())
                .centerCrop()
                .into(imageViewList);

        if (post.getRate() == null) {
            fullinfor_rate.setRating(1);
        } else {
            fullinfor_rate.setRating(post.getRate());
        }
        fullinfo_owner_home.setText(post.getHome_type() + " by the " + post.getPost_owner());
        fullinfo_city.setText(post.getCity());
        if (post.getHome_type().equals("House")) {
            fullinfo_price.setText(post.getPrice() + " DH");
        } else {
            fullinfo_price.setText(post.getPrice() + " DH per night");
        }
        fullinfo_room.setText(post.getRoom() + " guests max");
        fullinfo_desc.setText(post.getDescription());

        bookNow.setOnClickListener(v -> startActivity(new Intent(Intent.ACTION_DIAL).setData(Uri.parse("tel:" + post.getNum()))));
        RxView.clicks(favorButton)
                .throttleFirst(2, TimeUnit.HOURS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Unit>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        compositeDisposable.add(d);
                    }

                    @Override
                    public void onNext(Unit unit) {
                        FireBaseClient.GetInstance().getFirebaseFirestore()
                                .collection(common.Favor_DataBase_Table)
                                .document()
                                .set(new favorites(post)).addOnSuccessListener(aVoid ->
                                Toast.makeText(requireActivity(), "Favored", Toast.LENGTH_LONG).show());
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("FullInfo", Objects.requireNonNull(e.getMessage()));
                    }

                    @Override
                    public void onComplete() { }
                });
        return root;
    }

}
