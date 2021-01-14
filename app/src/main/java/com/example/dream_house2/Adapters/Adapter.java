package com.example.dream_house2.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.dream_house2.Modules.Post;
import com.example.dream_house2.R;
import com.example.dream_house2.common.common;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;


public class Adapter extends RecyclerView.Adapter<Adapter.postAdapterHolder> {
    private List<Post> List = new ArrayList<>();
    private List<Post> FavorList = new ArrayList<>();
    private Context context;
    private View view;

    public Adapter(Context context, View view) {
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
        String[] image = post.getImages().split(",");

        holder.imageSwitcher.setFactory(() -> {
            ImageView imageView = new ImageView(context);
            imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
            imageView.setLayoutParams(new ImageSwitcher.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.FILL_PARENT));
            return imageView;
        });
        // Declare in and out animations and load them using AnimationUtils class
        Animation in = AnimationUtils.loadAnimation(context, android.R.anim.slide_in_left);
        Animation out = AnimationUtils.loadAnimation(context, android.R.anim.slide_out_right);

        // set the animation type to ImageSwitcher
        holder.imageSwitcher.setInAnimation(in);
        holder.imageSwitcher.setOutAnimation(out);
        holder.imageSwitcher.setImageURI(Uri.parse("https:/firebasestorage.googleapis.com/v0/b/dreamhouse-a2d8f.appspot.com/o/ImageFolder%2Fimage%2Fimage%3A76?alt=media&token=8fd9d6db-3414-43de-b59f-c544e79aee42"));

        if (post.getRate()==0){
            holder.post_rate.setRating(1);
        }else {
            holder.post_rate.setRating(post.getRate());
        }
        holder.post_city.setText(post.getCity());
        if (post.getHome_type()!=null && post.getHome_type().equals("House")){
            holder.post_owner_home.setText(post.getHome_type()+" by "+post.getPost_owner());
            holder.post_price.setText(post.getPrice()+" DH");
        }else {
            holder.post_price.setText(post.getPrice()+" DH per night");
        }
        holder.post_price.setText(post.getPrice()+" per night");
        holder.post_room.setText(post.getRoom() +" guests max");
        Bundle bundle = new Bundle();
        bundle.putParcelable("post",post);
        holder.imageSwitcher.setOnClickListener(v-> Navigation.findNavController(view).navigate(R.id.navigation_fullInfo,bundle));

        holder.post_floatingActionButton.setOnClickListener(v-> { });


    }

    @Override
    public int getItemCount() {
        return List.size();
    }

    public void setList(List<Post> moviesList) {
        this.List = moviesList;
        notifyDataSetChanged();
    }
    public void setFavorList(List<Post> moviesList) {
        this.FavorList = moviesList;
        notifyDataSetChanged();
    }

    public Post getCardList(int adapterPosition) {
        return FavorList.get(adapterPosition);
    }

    static class postAdapterHolder extends RecyclerView.ViewHolder {
        private TextView post_owner_home,post_city,post_price,post_room;
        private RatingBar post_rate;
        private FloatingActionButton post_floatingActionButton;
        private ImageSwitcher imageSwitcher;
        public postAdapterHolder(@NonNull View itemView) {
            super(itemView);

            post_owner_home = itemView.findViewById(R.id.post_owner_home);
            post_city = itemView.findViewById(R.id.post_city);
            post_price = itemView.findViewById(R.id.post_price);
            post_room = itemView.findViewById(R.id.post_room);
            post_rate = itemView.findViewById(R.id.post_rate);
            post_floatingActionButton = itemView.findViewById(R.id.post_floatingActionButton);
            imageSwitcher = itemView.findViewById(R.id.post_imageView);
        }
    }
}