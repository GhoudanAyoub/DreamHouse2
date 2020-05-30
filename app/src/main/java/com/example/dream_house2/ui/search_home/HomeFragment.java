package com.example.dream_house2.ui.search_home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.dream_house2.Adapters.postAdapter;
import com.example.dream_house2.R;
import com.example.dream_house2.ui.filters.filters;

public class HomeFragment extends Fragment {

    private postAdapter postAdapter;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel = ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        homeViewModel.GetPosts();

        postAdapter = new postAdapter(getActivity(),root);
        RecyclerView RecycleViewHome = root.findViewById(R.id.recycleviewhome);

        root.findViewById(R.id.searchButton).setOnClickListener(v -> startActivity(new Intent(getActivity(), filters.class)));

        RecycleViewHome.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        RecycleViewHome.setHasFixedSize(true);
        RecycleViewHome.setAdapter(postAdapter);
        homeViewModel.getPostMutableLiveData().observe(getViewLifecycleOwner(), post -> postAdapter.setList(post));
        return root;
    }
}
