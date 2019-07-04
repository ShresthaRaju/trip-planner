package com.raju.tripplanner.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.raju.tripplanner.DaoImpl.InvitationDaoImpl;
import com.raju.tripplanner.MainActivity;
import com.raju.tripplanner.R;
import com.raju.tripplanner.adapters.MyInvitationsAdapter;
import com.raju.tripplanner.models.Invitation;

import java.util.ArrayList;
import java.util.List;

public class InvitationsFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private static final String ARG_PARAM1 = "param1";
    private String toolbarTitle;
    private SwipeRefreshLayout swipeRefreshInvitations;
    private RecyclerView myInvitationsContainer;
    private TextView tvNoInvitation;

    private InvitationDaoImpl invitationDaoImpl;
    private MyInvitationsAdapter myInvitationsAdapter;

    public InvitationsFragment() {
    }

    public static InvitationsFragment newInstance(String toolbarTitle) {
        InvitationsFragment invitationsFragment = new InvitationsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, toolbarTitle);
        invitationsFragment.setArguments(args);
        return invitationsFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View invitationsView = inflater.inflate(R.layout.fragment_invitations, container, false);
        initToolbar(invitationsView);

        swipeRefreshInvitations = invitationsView.findViewById(R.id.swipe_refresh_invitations);
        swipeRefreshInvitations.setOnRefreshListener(this);
        swipeRefreshInvitations.setColorSchemeColors(ContextCompat.getColor(getActivity(), R.color.teal_500));

        tvNoInvitation = invitationsView.findViewById(R.id.tv_no_invitation);

        myInvitationsContainer = invitationsView.findViewById(R.id.my_invitations_container);
        myInvitationsContainer.setHasFixedSize(true);
        myInvitationsContainer.setLayoutManager(new LinearLayoutManager(getActivity()));

        myInvitationsAdapter = new MyInvitationsAdapter(getActivity(), new ArrayList<>());
        myInvitationsContainer.setAdapter(myInvitationsAdapter);

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

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        swipeRefreshInvitations.setRefreshing(true);
        invitationDaoImpl = new InvitationDaoImpl(getActivity());

        invitationDaoImpl.getMyInvitations();

        invitationDaoImpl.setInvitationsListener(new InvitationDaoImpl.InvitationsListener() {
            @Override
            public void onInvitationsFetched(List<Invitation> myInvitations) {
                if (myInvitations.isEmpty()) {
                    swipeRefreshInvitations.setRefreshing(false);
                    tvNoInvitation.setVisibility(View.VISIBLE);
                } else {
                    tvNoInvitation.setVisibility(View.GONE);
                    myInvitationsAdapter.updateInvitationList(myInvitations);
                    swipeRefreshInvitations.setRefreshing(false);
                }
            }
        });
    }

    @Override
    public void onRefresh() {
        invitationDaoImpl.getMyInvitations();
    }
}
