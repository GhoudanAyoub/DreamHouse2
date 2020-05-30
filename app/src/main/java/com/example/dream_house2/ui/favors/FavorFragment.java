package com.example.dream_house2.ui.favors;

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

import com.example.dream_house2.Adapters.favoritesAdapter;
import com.example.dream_house2.R;
import com.example.dream_house2.ui.filters.filters;

public class FavorFragment extends Fragment {

    private favoritesAdapter favoritesAdapter;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        FavorViewModel notificationsViewModel = ViewModelProviders.of(this).get(FavorViewModel.class);
        View root = inflater.inflate(R.layout.fragment_favor, container, false);
        notificationsViewModel.GetFavors();

        favoritesAdapter = new favoritesAdapter(getActivity());
        RecyclerView RecycleViewFavorites = root.findViewById(R.id.recycleviewfavorites);

        RecycleViewFavorites.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        RecycleViewFavorites.setHasFixedSize(true);
        RecycleViewFavorites.setAdapter(favoritesAdapter);
        notificationsViewModel.GetFavoritesMutableLiveData().observe(getViewLifecycleOwner(), favorites -> favoritesAdapter.setList(favorites));
        return root;
    }
}
