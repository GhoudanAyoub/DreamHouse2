package com.example.dream_house2.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dream_house2.Adapters.Adapter;
import com.example.dream_house2.Modules.Post;
import com.example.dream_house2.R;
import com.example.dream_house2.ui.home.HomeViewModel;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class bookedFragment extends Fragment {

    private Adapter MyPostAdapter;
    private HomeViewModel mViewModel;
    private int flat=0,house=0;
    private TextView descOwned;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mViewModel = ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.booked_fragment, container, false);
        mViewModel.GetMyPosts();
        MyPostAdapter = new Adapter(getActivity(),root);
        descOwned = root.findViewById(R.id.descOwned);
        RecyclerView RecycleViewBooked = root.findViewById(R.id.recycleviewbooked);
        RecycleViewBooked.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        RecycleViewBooked.setHasFixedSize(true);
        RecycleViewBooked.setAdapter(MyPostAdapter);
        mViewModel.getbookedMutableLiveData().observe(getViewLifecycleOwner(), post -> {
            if(post==null){
                descOwned.setText("You Didnt Post No Estate For The Moment");
            } else {
                MyPostAdapter.setList(post);
                for (Post p : post) {
                    if (p.getHome_type().toLowerCase().equals("house")) {
                        house++;
                    } else if (p.getHome_type().toLowerCase().equals("flats")) {
                        flat++;
                    } else {
                        descOwned.setText("You owned " + flat + " flats and " + house + " homes");
                    }
                }
            }
        });
        return  root;
    }
}
