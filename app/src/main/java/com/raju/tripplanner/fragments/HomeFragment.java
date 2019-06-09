package com.raju.tripplanner.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.raju.tripplanner.MainActivity;
import com.raju.tripplanner.R;
import com.raju.tripplanner.activities.CreateTripActivity;
import com.raju.tripplanner.authentication.SignInActivity;
import com.raju.tripplanner.utils.UserSession;

public class HomeFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private String toolbarTitle;

    public HomeFragment() {
    }

    public static HomeFragment newInstance(String toolbarTitle) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, toolbarTitle);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View homeView = inflater.inflate(R.layout.fragment_home, container, false);
        initToolbar(homeView);
        initComponents(homeView);
        return homeView;
    }

    private void initComponents(View view) {
        FloatingActionButton createTrip = view.findViewById(R.id.fab_create_trip);
        createTrip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCreateTripActivity();
            }
        });

        Button btnLogout = view.findViewById(R.id.btn_logout);
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new UserSession(getActivity()).endSession();
                Intent signInActivity = new Intent(getActivity(), SignInActivity.class);
                signInActivity.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(signInActivity);
                getActivity().finish();
            }
        });
    }

    private void initToolbar(View view) {
        Toolbar homeToolbar = view.findViewById(R.id.home_toolbar);
        ((MainActivity) getActivity()).setSupportActionBar(homeToolbar);
        if (getArguments() != null) {
            toolbarTitle = getArguments().getString(ARG_PARAM1);
            ((MainActivity) getActivity()).getSupportActionBar().setTitle(toolbarTitle);
        }
    }

    private void showCreateTripActivity() {
        startActivity(new Intent(getActivity(), CreateTripActivity.class));
    }

}
