package com.example.dream_house2.ui.GlobalInfo;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.dream_house2.API.FireBaseClient;
import com.example.dream_house2.Modules.Post;
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

    private ImageView imageViewList;
    private FloatingActionButton bookNow, favorButton;
    private RatingBar fullinfor_rate;
    private TextView fullinfo_owner_home, fullinfo_city, fullinfo_price, fullinfo_room, fullinfo_desc;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.full_info_fragment, container, false);

        views(root);
        assert getArguments() != null;
        Post post = getArguments().getParcelable("post");
        assert post != null;
        String[] url = post.getImages().split("/");
        Glide.with(requireActivity())
                .load(url)
                .centerCrop()
                .into(imageViewList);
        fullinfor_rate.setRating(post.getRate());
        fullinfo_owner_home.setText(post.getHome_type() + " by the " + post.getPost_owner());
        fullinfo_city.setText(post.getCity());
        fullinfo_price.setText(post.getPrice() + " per night");
        fullinfo_room.setText(post.getRoom() + " guests max");
        fullinfo_desc.setText(post.getDescription());

        bookNow.setOnClickListener(v->{});
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
                                .document(post.getPost_owner())
                                .set(post);
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

    private void views(View root) {
        imageViewList = root.findViewById(R.id.imageViewlist);
        bookNow = root.findViewById(R.id.fullinfo_floatingActionButton3);
        favorButton = root.findViewById(R.id.fullinfo_floatingActionButton2);
        fullinfor_rate = root.findViewById(R.id.fullinfor_rate);
        fullinfo_owner_home = root.findViewById(R.id.fullinfo_owner_home);
        fullinfo_city = root.findViewById(R.id.fullinfo_city);
        fullinfo_price = root.findViewById(R.id.fullinfo_price);
        fullinfo_room = root.findViewById(R.id.fullinfo_room);
        fullinfo_desc = root.findViewById(R.id.fullinfo_desc);
    }
}
