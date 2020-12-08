package com.example.dream_house2.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dream_house2.Adapters.Adapter;
import com.example.dream_house2.Modules.Post;
import com.example.dream_house2.R;
import com.example.dream_house2.ui.home.HomeViewModel;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class FavorFragment extends Fragment {

    private Adapter postAdapter;
    private HomeViewModel homeViewModel;
    private RecyclerView RecycleViewFavorites;
    private TextView descFavor;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        homeViewModel = ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_favor, container, false);
        RecycleViewFavorites = root.findViewById(R.id.recycleviewfavorites);
        descFavor = root.findViewById(R.id.descFavor);
        homeViewModel.GetData();
        Swipe();
        postAdapter = new Adapter(getActivity(),root);


        RecycleViewFavorites.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        RecycleViewFavorites.setHasFixedSize(true);
        RecycleViewFavorites.setAdapter(postAdapter);
        homeViewModel.getFavList().observe(getViewLifecycleOwner(), favorites -> {
            postAdapter.setFavorList(favorites);
            descFavor.setText("You have "+favorites.size()+" favorite flats and  homes");
        });
        return root;
    }

    private void Swipe() {
        ItemTouchHelper.SimpleCallback touchHelper = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                Post post = postAdapter.getCardList(viewHolder.getAdapterPosition());
                homeViewModel.DeleteData(post.getId());
                postAdapter.notifyDataSetChanged();
                Toast.makeText(getActivity(), "Deleted From Favorites", Toast.LENGTH_SHORT).show();
            }
        };
        new ItemTouchHelper(touchHelper).attachToRecyclerView(RecycleViewFavorites);
    }
}
