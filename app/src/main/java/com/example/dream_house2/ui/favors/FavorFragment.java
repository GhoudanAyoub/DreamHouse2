package com.example.dream_house2.ui.favors;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.example.dream_house2.R;

public class FavorFragment extends Fragment {

    private FavorViewModel notificationsViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        notificationsViewModel =
                ViewModelProviders.of(this).get(FavorViewModel.class);
        View root = inflater.inflate(R.layout.fragment_favor, container, false);
        return root;
    }
}
