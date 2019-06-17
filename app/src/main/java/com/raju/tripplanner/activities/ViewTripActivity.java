package com.raju.tripplanner.activities;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.raju.tripplanner.ApiCalls.TripAPI;
import com.raju.tripplanner.R;
import com.raju.tripplanner.dialogs.ConfirmationDialog;
import com.raju.tripplanner.models.Trip;
import com.raju.tripplanner.utils.RetrofitClient;
import com.raju.tripplanner.utils.Tools;
import com.raju.tripplanner.utils.UserSession;
import com.squareup.picasso.Picasso;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ViewTripActivity extends AppCompatActivity implements ConfirmationDialog.ConfirmationDialogListener {
    private ImageView viewTripImage;
    private Trip trip;
    private TripAPI tripAPI;

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
        tripAPI = RetrofitClient.getInstance().create(TripAPI.class);
    }

    @Override
    protected void onStart() {
        super.onStart();

        populateTrip();
    }

    private void populateTrip() {
        trip = (Trip) getIntent().getSerializableExtra("TRIP");

        Picasso.get().load(trip.getDestination().getPhotoUrl()).into(viewTripImage);
        getSupportActionBar().setTitle(trip.getName());
        getSupportActionBar().setSubtitle(Tools.formatDate(trip.getStartDate()) + " -" + Tools.formatDate(trip.getEndDate()));

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
                Toast.makeText(this, "edited", Toast.LENGTH_SHORT).show();
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

        ConfirmationDialog confirmationDialog = new ConfirmationDialog(title, message);
        confirmationDialog.show(getSupportFragmentManager(), "DELETE TRIP");
    }

    @Override
    public void onOK() {
        deleteTrip();
    }

    @Override
    public void onCancel() {
        Toast.makeText(this, "cancelled", Toast.LENGTH_SHORT).show();
    }

    private void deleteTrip() {
        Call<Void> deleteTripCall = tripAPI.deleteTrip("Bearer " + new UserSession(this).getAuthToken(), trip.getId());
        deleteTripCall.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(ViewTripActivity.this, "ERROR: " + response.code() + " " + response.message(), Toast.LENGTH_LONG).show();
                    return;
                }

                Toast.makeText(ViewTripActivity.this, "Trip deleted successfully", Toast.LENGTH_SHORT).show();
                finish();
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(ViewTripActivity.this, "FAILED: " + t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
