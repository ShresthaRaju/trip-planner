package com.raju.tripplanner.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.raju.tripplanner.DAO.UserAPI;
import com.raju.tripplanner.DaoImpl.UserDaoImpl;
import com.raju.tripplanner.R;
import com.raju.tripplanner.adapters.InvitationAutocompleteAdapter;
import com.raju.tripplanner.models.InvitationItem;
import com.raju.tripplanner.models.User;
import com.raju.tripplanner.utils.Error;
import com.raju.tripplanner.utils.RetrofitClient;
import com.raju.tripplanner.utils.Tools;

import java.util.ArrayList;
import java.util.List;

public class InviteFriendsActivity extends AppCompatActivity {

    private UserAPI userAPI;
    private AutoCompleteTextView friendsAutocomplete;
    private UserDaoImpl userDaoImpl;
    private List<InvitationItem> allUserList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invite_friends);

        userAPI = RetrofitClient.getInstance(Tools.BASE_URL).create(UserAPI.class);
        userDaoImpl = new UserDaoImpl(this);
        allUserList = new ArrayList<>();
        userDaoImpl.getAllUsers();

        initComponents();

        userListener();

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
                    allUserList.add(new InvitationItem(user.getId(), user.getDisplayPicture(), user.getUsername()));
                }

                InvitationAutocompleteAdapter autocompleteAdapter = new InvitationAutocompleteAdapter(InviteFriendsActivity.this, allUserList);
                friendsAutocomplete.setAdapter(autocompleteAdapter);

                friendsAutocomplete.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        InvitationAutocompleteAdapter adapter = (InvitationAutocompleteAdapter) parent.getAdapter();
                        String userId = adapter.getItem(position).getUserId();
                        Toast.makeText(InviteFriendsActivity.this, userId, Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
}
