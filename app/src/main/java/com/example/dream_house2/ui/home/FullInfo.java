package com.example.dream_house2.ui.home;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.request.RequestOptions;
import com.example.dream_house2.Modules.Post;
import com.example.dream_house2.R;
import com.glide.slider.library.SliderLayout;
import com.glide.slider.library.animations.DescriptionAnimation;
import com.glide.slider.library.slidertypes.BaseSliderView;
import com.glide.slider.library.slidertypes.DefaultSliderView;
import com.glide.slider.library.slidertypes.TextSliderView;
import com.glide.slider.library.tricks.ViewPagerEx;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.jakewharton.rxbinding3.view.RxView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import kotlin.Unit;

public class FullInfo extends Fragment implements BaseSliderView.OnSliderClickListener,
        ViewPagerEx.OnPageChangeListener{

    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private SliderLayout mDemoSlider;
    ArrayList<String> listUrl = new ArrayList<>();

    @SuppressLint({"SetTextI18n", "CheckResult"})
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.full_info_fragment, container, false);

        mDemoSlider = root.findViewById(R.id.imageViewlist);
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
        if (post!=null){
            RequestOptions requestOptions = new RequestOptions();
            requestOptions.centerCrop();

            Collections.addAll(listUrl, post.getImages().split(","));

            for (int i = 1; i < listUrl.size(); i++) {
                DefaultSliderView sliderView = new DefaultSliderView(requireContext());
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


            if (post.getRate() ==0) {
                fullinfor_rate.setRating(1);
            } else {
                fullinfor_rate.setRating(post.getRate());
            }
            fullinfo_city.setText(post.getCity());
            if (post.getHome_type()!=null && post.getHome_type().equals("House")) {
                fullinfo_owner_home.setText(post.getHome_type() + " by the " + post.getPost_owner());
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
                    .subscribe(unit -> {});
        }

        return root;
    }

    @Override
    public void onSliderClick(BaseSliderView slider) {

        Toast.makeText(requireActivity(),slider.getBundle().get("extra") + "", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

        Log.d("Slider Demo", "Page Changed: " + position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onStop() {
        mDemoSlider.stopAutoCycle();
        super.onStop();
    }

    @Override
    public void onDestroy() {
        mDemoSlider.stopAutoCycle();
        super.onDestroy();
    }
}
