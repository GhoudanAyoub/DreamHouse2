package com.example.dream_house2.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class HomeFragment extends Fragment {

    private Adapter postAdapter;
    private HomeViewModel homeViewModel;
    private RecyclerView RecycleViewHome;
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        homeViewModel = ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        homeViewModel.GetPosts();
        Swipe();
        postAdapter = new Adapter(getActivity(),root);
        RecycleViewHome = root.findViewById(R.id.recycleviewhome);
        RecycleViewHome.setLayoutManager(new LinearLayoutManager(getActivity()));
        RecycleViewHome.setAdapter(postAdapter);
        homeViewModel.getPostMutableLiveData().observe(getViewLifecycleOwner(), post -> postAdapter.setList(post));

        root.findViewById(R.id.searchButton).setOnClickListener(v -> startActivity(new Intent(getActivity(), filters.class)));

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
                homeViewModel.InsertData(post);
                postAdapter.notifyDataSetChanged();
                Toast.makeText(getActivity(), "Added To Favorites", Toast.LENGTH_SHORT).show();
            }
        };
        new ItemTouchHelper(touchHelper).attachToRecyclerView(RecycleViewHome);
    }
}
