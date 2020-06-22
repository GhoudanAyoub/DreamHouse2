package com.example.dream_house2.ui.booked;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dream_house2.Adapters.postAdapter;
import com.example.dream_house2.R;
public class bookedFragment extends Fragment {

    private postAdapter MyPostAdapter;
    BookedViewModel mViewModel;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mViewModel = ViewModelProviders.of(this).get(BookedViewModel.class);
        View root = inflater.inflate(R.layout.booked_fragment, container, false);
        mViewModel.GetMyPosts();
        MyPostAdapter = new postAdapter(getActivity(),root);
        RecyclerView RecycleViewBooked = root.findViewById(R.id.recycleviewbooked);
        RecycleViewBooked.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        RecycleViewBooked.setHasFixedSize(true);
        RecycleViewBooked.setAdapter(MyPostAdapter);
        mViewModel.getMyPostMutableLiveData().observe(getViewLifecycleOwner(), post -> MyPostAdapter.setList(post));
        return  root;
    }
}
