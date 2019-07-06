package com.raju.tripplanner.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.raju.tripplanner.DaoImpl.InvitationDaoImpl;
import com.raju.tripplanner.DaoImpl.UserDaoImpl;
import com.raju.tripplanner.R;
import com.raju.tripplanner.adapters.InvitationAutocompleteAdapter;
import com.raju.tripplanner.models.InvitationItem;
import com.raju.tripplanner.models.User;
import com.raju.tripplanner.utils.Error;
import com.raju.tripplanner.utils.UserSession;

import java.util.ArrayList;
import java.util.List;

public class InviteFriendsActivity extends AppCompatActivity {

    private AutoCompleteTextView friendsAutocomplete;
    private List<InvitationItem> allUserList;
    private UserDaoImpl userDaoImpl;
    private InvitationDaoImpl invitationDaoImpl;
    private String tripId;
    private UserSession userSession;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invite_friends);

        userSession = new UserSession(this);
        allUserList = new ArrayList<>();
        userDaoImpl = new UserDaoImpl(this);
        invitationDaoImpl = new InvitationDaoImpl(this);
        userDaoImpl.getAllUsers();

        initComponents();

        userListener();

        tripId = getIntent().getStringExtra("TRIP_ID");

    }

    private void initComponents() {
        Toolbar toolbar = findViewById(R.id.toolbar_invite_friends);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Invite your friends");

        friendsAutocomplete = findViewById(R.id.friends_autocomplete);
    }

    @Override
    protected void onStart() {
        super.onStart();
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
                    }
                });
            }
        });
    }
}
