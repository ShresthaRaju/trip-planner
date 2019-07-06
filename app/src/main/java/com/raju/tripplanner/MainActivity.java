package com.raju.tripplanner;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.raju.tripplanner.BroadcastReceiver.NotificationActionReceiver;
import com.raju.tripplanner.bottomsheet.ProfileBottomSheet;
import com.raju.tripplanner.dialogs.ConfirmationDialog;
import com.raju.tripplanner.fragments.HomeFragment;
import com.raju.tripplanner.fragments.InvitationsFragment;
import com.raju.tripplanner.fragments.ProfileFragment;
import com.raju.tripplanner.models.User;
import com.raju.tripplanner.service.TripNotificationService;
import com.raju.tripplanner.utils.UserSession;

public class MainActivity extends AppCompatActivity implements ProfileBottomSheet.ProfileBottomSheetListener, ConfirmationDialog.ConfirmationDialogListener {

    private ProfileFragment profileFragment;
    private ImageButton btnHome;
    private NotificationActionReceiver actionReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        User authUser = new UserSession(this).getUser();
        profileFragment = ProfileFragment.newInstance(authUser.getFirstName() + " " + authUser.getFamilyName());
        actionReceiver = new NotificationActionReceiver();

        initComponents();

        loadFragment(HomeFragment.newInstance("My Trips"));

        startService(new Intent(this, TripNotificationService.class));

    }

    private void initComponents() {
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        btnHome = findViewById(R.id.btn_home);
        btnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadFragment(HomeFragment.newInstance("My Trips"));
            }
        });

        bottomNavigationView.getMenu().getItem(0).setCheckable(false);
        bottomNavigationView.getMenu().getItem(1).setCheckable(false);
        bottomNavigationView.getMenu().getItem(2).setCheckable(false);
        bottomNavigationView.setOnNavigationItemSelectedListener(bottomNavListener);
    }

    private BottomNavigationView.OnNavigationItemSelectedListener bottomNavListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment selectedFragment = null;
            switch (item.getItemId()) {
                case R.id.nav_home:
                    selectedFragment = HomeFragment.newInstance("My Trips");
                    break;

                case R.id.nav_invitations:
                    selectedFragment = InvitationsFragment.newInstance("My Invitations");
                    break;

                case R.id.nav_profile:
                    selectedFragment = profileFragment;
                    break;
            }

            loadFragment(selectedFragment);
            return true;
        }
    };

    // load selected fragment
    private void loadFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).commit();
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        String title = "Quit Trip Planner";
        String message = "Are you sure you want to quit the app?";
        ConfirmationDialog confirmationDialog = new ConfirmationDialog(title, message);
        confirmationDialog.show(getSupportFragmentManager(), "QUIT APP");
    }

    @Override
    public void onImageSelected(String imagePath) {
        profileFragment.updateDisplayPicture(imagePath);
    }

    @Override
    public void onSure() {
        finish();
    }

    @Override
    public void onCancel() {

    }

//    @Override
//    protected void onStart() {
//        super.onStart();
//        IntentFilter intentFilter = new IntentFilter("com.raju.tripplanner");
//        registerReceiver(actionReceiver, intentFilter);
//    }

//    @Override
//    protected void onStop() {
//        super.onStop();
//        unregisterReceiver(actionReceiver);
//    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopService(new Intent(this, TripNotificationService.class));
    }
}
