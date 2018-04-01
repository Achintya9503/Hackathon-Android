package com.pcs.hackathonandroid.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.pcs.hackathonandroid.R;
import com.pcs.hackathonandroid.activities.MP4CaptureActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HomeFragment extends Fragment {

    @BindView(R.id.go_live)
    Button goLive;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        ButterKnife.bind(this, view);

        goLive.setOnClickListener(view1 -> {
            Intent intent = new Intent(getContext(), MP4CaptureActivity.class);
            startActivity(intent);
        });

        return view;
    }
}
