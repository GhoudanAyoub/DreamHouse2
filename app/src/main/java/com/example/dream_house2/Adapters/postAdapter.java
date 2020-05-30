package com.example.dream_house2.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.dream_house2.API.FireBaseClient;
import com.example.dream_house2.Modules.Post;
import com.example.dream_house2.R;
import com.example.dream_house2.common.common;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class postAdapter extends RecyclerView.Adapter<postAdapter.postAdapterHolder> {
    private List<Post> List = new ArrayList<>();
    private Context context;
    private View view;

    public postAdapter(Context context, View view) {
        this.context = context;
        this.view = view;
    }

    @NonNull
    @Override
    public postAdapterHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new postAdapterHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.post_layout, parent, false));
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull postAdapterHolder holder, int position) {

        Post post = List.get(position);
        String[] url = post.getImages().split("/");
        Glide
                .with(context)
                .load(url)
                .centerCrop()
                .into(holder.post_imageView);
        holder.post_rate.setRating(post.getRate());
        holder.post_owner_home.setText(post.getHome_type()+" by the "+post.getPost_owner());
        holder.post_city.setText(post.getCity());
        holder.post_price.setText(post.getPrice()+" per night");
        holder.post_room.setText(post.getRoom() +" guests max");
        Bundle bundle = new Bundle();
        bundle.putParcelable("post",post);
        holder.post_imageView.setOnClickListener(v-> Navigation.findNavController(view).navigate(R.id.navigation_fullInfo,bundle));
        /*
        holder.post_floatingActionButton.setOnClickListener(v-> FireBaseClient.GetInstance().getFirebaseFirestore()
                .collection(common.Favor_DataBase_Table)
                .document(post.getPost_owner())
                .set(post));

         */
    }

    @Override
    public int getItemCount() {
        return List.size();
    }

    public void setList(List<Post> moviesList) {
        this.List = moviesList;
        notifyDataSetChanged();
    }

    static class postAdapterHolder extends RecyclerView.ViewHolder {
        private TextView post_owner_home,post_city,post_price,post_room;
        private RatingBar post_rate;
        private FloatingActionButton post_floatingActionButton;
        private ImageView post_imageView;
        public postAdapterHolder(@NonNull View itemView) {
            super(itemView);

            post_owner_home = itemView.findViewById(R.id.post_owner_home);
            post_city = itemView.findViewById(R.id.post_city);
            post_price = itemView.findViewById(R.id.post_price);
            post_room = itemView.findViewById(R.id.post_room);
            post_rate = itemView.findViewById(R.id.post_rate);
            post_floatingActionButton = itemView.findViewById(R.id.post_floatingActionButton);
            post_imageView = itemView.findViewById(R.id.post_imageView);
        }
    }
}