package com.raju.tripplanner.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.raju.tripplanner.MainActivity;
import com.raju.tripplanner.R;

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View invitationsView = inflater.inflate(R.layout.fragment_invitations, container, false);
        initToolbar(invitationsView);
        return invitationsView;
    }

    private void initToolbar(View view) {
        Toolbar invitationsToolbar = view.findViewById(R.id.invitations_toolbar);
        ((MainActivity) getActivity()).setSupportActionBar(invitationsToolbar);
        if (getArguments() != null) {
            toolbarTitle = getArguments().getString(ARG_PARAM1);
            ((MainActivity) getActivity()).getSupportActionBar().setTitle(toolbarTitle);
        }
    }

}
