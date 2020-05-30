package com.example.dream_house2.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.dream_house2.API.FireBaseClient;
import com.example.dream_house2.Modules.Post;
import com.example.dream_house2.Modules.favorites;
import com.example.dream_house2.R;
import com.example.dream_house2.common.common;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;

public class favoritesAdapter extends RecyclerView.Adapter<favoritesAdapter.favoritesAdapterHolder> {
    private List<favorites> List = new ArrayList<>();
    private Context context;

    public favoritesAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public favoritesAdapterHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new favoritesAdapterHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.favor_layout, parent, false));
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull favoritesAdapterHolder holder, int position) {
        Post post = List.get(position).getPost();
        String[] url = post.getImages().split("/");
        Glide
                .with(context)
                .load(url)
                .centerCrop()
                .into(holder.favor_imageView);
        holder.favor_rate.setRating(post.getRate());
        holder.owner_home.setText(post.getHome_type()+" by the "+post.getPost_owner());
        holder.favor_city.setText(post.getCity());
        holder.favor_price.setText(post.getPrice()+" per night");
        holder.favor_room.setText(post.getRoom() +" guests max");
        holder.favor_floatingActionButton.setOnClickListener(v-> FireBaseClient.GetInstance().getFirebaseFirestore()
                .collection(common.Favor_DataBase_Table)
                .document(post.getPost_owner())
                .delete());
    }

    @Override
    public int getItemCount() {
        return List.size();
    }

    public void setList(List<favorites> moviesList) {
        this.List = moviesList;
        notifyDataSetChanged();
    }

    static class favoritesAdapterHolder extends RecyclerView.ViewHolder {
        private TextView owner_home,favor_city,favor_price,favor_room;
        private RatingBar favor_rate;
        private FloatingActionButton favor_floatingActionButton;
        private ImageView favor_imageView;
        favoritesAdapterHolder(@NonNull View itemView) {
            super(itemView);
            owner_home = itemView.findViewById(R.id.owner_home);
            favor_city = itemView.findViewById(R.id.favor_city);
            favor_price = itemView.findViewById(R.id.favor_price);
            favor_room = itemView.findViewById(R.id.favor_room);
            favor_rate = itemView.findViewById(R.id.favor_rate);
            favor_floatingActionButton = itemView.findViewById(R.id.favor_floatingActionButton);
            favor_imageView = itemView.findViewById(R.id.favor_imageView);
        }
    }
}