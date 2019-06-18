package com.raju.tripplanner.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.raju.tripplanner.DaoImpl.TripDaoImpl;
import com.raju.tripplanner.R;
import com.raju.tripplanner.dialogs.ConfirmationDialog;
import com.raju.tripplanner.models.Trip;
import com.raju.tripplanner.utils.Tools;
import com.squareup.picasso.Picasso;

public class ViewTripActivity extends AppCompatActivity implements ConfirmationDialog.ConfirmationDialogListener {
    private ImageView viewTripImage;
    private Trip trip;
    private TripDaoImpl tripDaoImpl;
    private ConfirmationDialog confirmationDialog;

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
        viewTripImage = findViewById(R.id.view_trip_image);
        tripDaoImpl = new TripDaoImpl(this);
    }

    @Override
    protected void onStart() {
        super.onStart();

        populateTrip();
    }

    private void populateTrip() {
        trip = (Trip) getIntent().getSerializableExtra("TRIP");

        getSupportActionBar().setTitle(trip.getName());
        getSupportActionBar().setSubtitle(Tools.formatDate(trip.getStartDate()) + " -" + Tools.formatDate(trip.getEndDate()));

        Picasso.get().load(trip.getDestination().getPhotoUrl()).into(viewTripImage);

        Toast.makeText(this, trip.getName(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_view_trip_activity, menu);
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
        String title = "Delete Trip ?";
        String message = "Are you sure you want to delete this trip? It cannot be undone !";

        confirmationDialog = new ConfirmationDialog(title, message);
        confirmationDialog.show(getSupportFragmentManager(), "DELETE TRIP");
    }

    @Override
    public void onOK() {
        tripDaoImpl.deleteTrip(trip.getId());
    }

    @Override
    public void onCancel() {
        confirmationDialog.dismiss();
    }
}
