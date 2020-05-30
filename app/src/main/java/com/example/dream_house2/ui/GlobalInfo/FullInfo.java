package com.example.dream_house2.ui.GlobalInfo;

import android.annotation.SuppressLint;
import android.os.Bundle;
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

public class FullInfo extends Fragment {

    private ImageView imageViewList;
    private FloatingActionButton bookNow, favorButton;
    private RatingBar fullinfor_rate;
    private TextView fullinfo_owner_home, fullinfo_city, fullinfo_price, fullinfo_room, fullinfo_desc;

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

        favorButton.setOnClickListener(v -> FireBaseClient.GetInstance().getFirebaseFirestore()
                .collection(common.Favor_DataBase_Table)
                .document(post.getPost_owner())
                .set(post));
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
