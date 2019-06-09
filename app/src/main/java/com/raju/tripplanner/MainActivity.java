package com.raju.tripplanner;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.raju.tripplanner.bottomsheets.ProfileBottomSheet;
import com.raju.tripplanner.fragments.HomeFragment;
import com.raju.tripplanner.fragments.InvitationsFragment;
import com.raju.tripplanner.fragments.ProfileFragment;

public class MainActivity extends AppCompatActivity implements ProfileBottomSheet.ProfileBottomSheetListener {

    private ProfileFragment profileFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        profileFragment = ProfileFragment.newInstance("AUTHENTICATED USER");

        initComponents();

        loadFragment(HomeFragment.newInstance("My Trips"));

    }

    private void initComponents() {
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
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
                    selectedFragment = InvitationsFragment.newInstance("Invitations");
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
        super.onBackPressed();
        finish();
    }

    @Override
    public void onImageSelected(String imagePath) {
//        Toast.makeText(this, imagePath, Toast.LENGTH_LONG).show();
        profileFragment.updateDisplayPicture(imagePath);
    }
}
