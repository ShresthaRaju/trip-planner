package com.raju.tripplanner.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.raju.tripplanner.MainActivity;
import com.raju.tripplanner.R;

import androidx.fragment.app.Fragment;

public class InvitationsFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private String toolbarTitle;


    public InvitationsFragment() {
    }

    public static InvitationsFragment newInstance(String toolbarTitle) {
        InvitationsFragment fragment = new InvitationsFragment();
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
        return inflater.inflate(R.layout.fragment_invitations, container, false);
    }

}
