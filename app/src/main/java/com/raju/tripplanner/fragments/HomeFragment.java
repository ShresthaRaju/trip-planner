package com.raju.tripplanner.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.raju.tripplanner.MainActivity;
import com.raju.tripplanner.R;
import com.raju.tripplanner.activities.CreateTripActivity;
import com.raju.tripplanner.authentication.SignInActivity;
import com.raju.tripplanner.utils.UserSession;

import androidx.fragment.app.Fragment;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";

    // TODO: Rename and change types of parameters
    private String toolbarTitle;

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     * <p>
     * //     * @param param1 Parameter 1.
     * //     * @param param2 Parameter 2.
     *
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String toolbarTitle) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, toolbarTitle);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            toolbarTitle = getArguments().getString(ARG_PARAM1);
            ((MainActivity) getActivity()).getSupportActionBar().setTitle(toolbarTitle);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);
        initComponents(rootView);
        return rootView;
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

    private void showCreateTripActivity() {
        startActivity(new Intent(getActivity(), CreateTripActivity.class));
    }

}
