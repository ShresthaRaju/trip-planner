package com.raju.tripplanner.activities;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.raju.tripplanner.DaoImpl.InvitationDaoImpl;
import com.raju.tripplanner.DaoImpl.TripDaoImpl;
import com.raju.tripplanner.DaoImpl.UserDaoImpl;
import com.raju.tripplanner.R;
import com.raju.tripplanner.adapters.InvitationAutocompleteAdapter;
import com.raju.tripplanner.adapters.TripInviteesAdapter;
import com.raju.tripplanner.models.InvitationItem;
import com.raju.tripplanner.models.Trip;
import com.raju.tripplanner.models.User;
import com.raju.tripplanner.utils.Error;
import com.raju.tripplanner.utils.UserSession;

import java.util.ArrayList;
import java.util.List;

public class InviteFriendsActivity extends AppCompatActivity {

    private AutoCompleteTextView friendsAutocomplete;
    private List<InvitationItem> allUserList;
    private UserDaoImpl userDaoImpl;
    private TripDaoImpl tripDaoImpl;
    private InvitationDaoImpl invitationDaoImpl;
    private String tripId, tripSlug;
    private UserSession userSession;

    private RecyclerView tripInviteesContainer;
    private TripInviteesAdapter tripInviteesAdapter;
    private TextView tvNoInvitee;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invite_friends);

        initComponents();

    }

    private void initComponents() {
        Toolbar toolbar = findViewById(R.id.toolbar_invite_friends);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Invite your friends");

        tripId = getIntent().getStringExtra("TRIP_ID");
        tripSlug = getIntent().getStringExtra("TRIP_SLUG");

        userSession = new UserSession(this);
        allUserList = new ArrayList<>();
        userDaoImpl = new UserDaoImpl(this);
        tripDaoImpl = new TripDaoImpl(this);
        invitationDaoImpl = new InvitationDaoImpl(this);

        userDaoImpl.getAllUsers();
        tripDaoImpl.viewTrip(tripId, tripSlug);

        userListener();
        tripListener();

        friendsAutocomplete = findViewById(R.id.friends_autocomplete);
        tvNoInvitee = findViewById(R.id.tv_no_invitee);

        tripInviteesContainer = findViewById(R.id.trip_invitees_container);
        tripInviteesContainer.setHasFixedSize(true);
        tripInviteesContainer.setLayoutManager(new LinearLayoutManager(this));

        tripInviteesAdapter = new TripInviteesAdapter(this, new ArrayList<>());
        tripInviteesContainer.setAdapter(tripInviteesAdapter);
    }

    private void userListener() {
        userDaoImpl.setUserActionsListener(new UserDaoImpl.UserActionsListener() {
            @Override
            public void onDpUploaded(User updatedUser) {

            }

            @Override
            public void onDetailsUpdated(User updatedUser) {

            }

            @Override
            public void onPasswordChanged() {

            }

            @Override
            public void onSignedOut() {

            }

            @Override
            public void onError(Error error) {

            }

            @Override
            public void onFetchedAllUsers(List<User> allUsers) {

                for (User user : allUsers) {
                    if (!user.getUsername().equals(userSession.getUser().getUsername())) {
                        allUserList.add(new InvitationItem(user.getId(), user.getDisplayPicture(), user.getUsername()));
                    }
                }

                InvitationAutocompleteAdapter autocompleteAdapter = new InvitationAutocompleteAdapter(InviteFriendsActivity.this, allUserList);
                friendsAutocomplete.setAdapter(autocompleteAdapter);

                friendsAutocomplete.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        InvitationAutocompleteAdapter adapter = (InvitationAutocompleteAdapter) parent.getAdapter();
                        String inviteeId = adapter.getItem(position).getUserId();
//                        Toast.makeText(InviteFriendsActivity.this, inviteeId, Toast.LENGTH_SHORT).show();
                        invitationDaoImpl.sendInvitation(inviteeId, tripId);
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                tripDaoImpl.viewTrip(tripId, tripSlug);
                            }
                        }, 3000);
                    }
                });
            }
        });
    }

    private void tripListener() {
        tripDaoImpl.setTripActionsListener(new TripDaoImpl.TripActionsListener() {
            @Override
            public void onTripsReceived(List<Trip> myTrips) {

            }

            @Override
            public void onTripViewed(List<User> invitees) {
                if (invitees.isEmpty()) {
                    tvNoInvitee.setVisibility(View.VISIBLE);
                } else {
                    tvNoInvitee.setVisibility(View.GONE);
                    tripInviteesAdapter.updateInviteeList(invitees);
                }
            }

            @Override
            public void onTripUpdated() {

            }

            @Override
            public void onTripDeleted() {

            }
        });
    }
}
