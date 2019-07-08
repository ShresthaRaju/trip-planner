package com.raju.tripplanner.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.raju.tripplanner.DaoImpl.TripDaoImpl;
import com.raju.tripplanner.R;
import com.raju.tripplanner.adapters.TripOptionsPagerAdapter;
import com.raju.tripplanner.dialogs.ConfirmationDialog;
import com.raju.tripplanner.models.Trip;
import com.raju.tripplanner.utils.Tools;
import com.raju.tripplanner.utils.UserSession;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ViewTripActivity extends AppCompatActivity implements ConfirmationDialog.ConfirmationDialogListener, TabLayout.OnTabSelectedListener {
    private ImageView viewTripImage;
    private Trip trip;
    private TripDaoImpl tripDaoImpl;
    private ConfirmationDialog confirmationDialog;
    private ProgressBar progressDeleteTrip;
    private ViewPager viewPager;
    private TabLayout tabLayout;
    private FloatingActionButton fabInviteFriends;
    private boolean isTripOwner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_trip);

        initToolbar();

        initComponents();

    }

    private void initToolbar() {
        Toolbar toolbar = findViewById(R.id.view_trip_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void initComponents() {
        trip = (Trip) getIntent().getSerializableExtra("TRIP");
        isTripOwner = new UserSession(this).getUser().getId().equals(trip.getCreator());

        tabLayout = findViewById(R.id.trip_tabs);
        viewPager = findViewById(R.id.viewPager);
        fabInviteFriends = findViewById(R.id.fab_invite_friends);
        tripDaoImpl = new TripDaoImpl(this);
        viewTripImage = findViewById(R.id.view_trip_image);
        progressDeleteTrip = findViewById(R.id.progress_delete_trip);

        if (!isTripOwner) {
            fabInviteFriends.setVisibility(View.GONE);
        } else {
            fabInviteFriends.setVisibility(View.VISIBLE);
        }

        fabInviteFriends.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent inviteFriends = new Intent(ViewTripActivity.this, InviteFriendsActivity.class);
                inviteFriends.putExtra("TRIP_ID", trip.getId());
                startActivity(inviteFriends);
            }
        });

        viewPager.setAdapter(new TripOptionsPagerAdapter(getSupportFragmentManager(), trip.getDestination().getLat(), trip.getDestination().getLng()));
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        tabLayout.addOnTabSelectedListener(this);

    }

    @Override
    protected void onStart() {
        super.onStart();
        populateTrip();

        tripDaoImpl.setTripActionsListener(new TripDaoImpl.TripActionsListener() {
            @Override
            public void onTripsReceived(List<Trip> myTrips) {

            }

            @Override
            public void onTripCreated(Trip trip) {

            }

            @Override
            public void onTripUpdated() {

            }

            @Override
            public void onTripDeleted() {
                progressDeleteTrip.setVisibility(View.GONE);
                Toast.makeText(ViewTripActivity.this, "Trip deleted successfully", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

    private void populateTrip() {

        getSupportActionBar().setTitle(trip.getName());
        getSupportActionBar().setSubtitle(Tools.formatDate("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", "MMM dd", trip.getStartDate()) + " -"
                + Tools.formatDate("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", "MMM dd", trip.getEndDate()));

        Picasso.get().load(trip.getDestination().getPhotoUrl()).into(viewTripImage);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_view_trip_activity, menu);
        MenuItem edit = menu.findItem(R.id.action_edit);
        MenuItem delete = menu.findItem(R.id.action_delete);

        if (!isTripOwner) {
            edit.setVisible(false);
            delete.setVisible(false);
        } else {
            edit.setVisible(true);
            delete.setVisible(true);
        }

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_edit:
                Intent editTrip = new Intent(this, CreateTripActivity.class);
                editTrip.putExtra("Trip", trip);
                startActivity(editTrip);
                return true;

            case R.id.action_delete:
                showConfirmationDialog();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void showConfirmationDialog() {
        String title = "Delete Trip";
        String message = "Are you sure you want to delete this trip? It cannot be undone !";

        confirmationDialog = new ConfirmationDialog(title, message);
        confirmationDialog.show(getSupportFragmentManager(), "DELETE TRIP");
    }

    @Override
    public void onSure() {
        progressDeleteTrip.setVisibility(View.VISIBLE);
        tripDaoImpl.deleteTrip(trip.getId());
    }

    @Override
    public void onCancel() {
        confirmationDialog.dismiss();
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        switch (tab.getPosition()) {
            case 0:
                viewPager.setCurrentItem(0);
                break;

            case 1:
                viewPager.setCurrentItem(1);
                break;

            case 2:
                viewPager.setCurrentItem(2);
        }
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }

}
