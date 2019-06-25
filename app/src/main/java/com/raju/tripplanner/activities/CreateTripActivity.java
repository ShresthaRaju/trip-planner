package com.raju.tripplanner.activities;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.textfield.TextInputLayout;
import com.raju.tripplanner.DaoImpl.TripDaoImpl;
import com.raju.tripplanner.MainActivity;
import com.raju.tripplanner.R;
import com.raju.tripplanner.dialogs.DialogDatePicker;
import com.raju.tripplanner.models.Destination;
import com.raju.tripplanner.models.Trip;
import com.raju.tripplanner.utils.EditTextValidation;
import com.raju.tripplanner.utils.Tools;
import com.raju.tripplanner.utils.UserSession;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class CreateTripActivity extends AppCompatActivity {

    private Button btnCreate, btnUpdate;
    private TextInputLayout etTripTitle, etStartDate, etEndDate;
    private int year, month, dayOfMonth;
    private Calendar calendar;
    private long timeInMillis;
    private Destination destination;
    private TripDaoImpl tripDaoImpl;
    private Trip trip;
    private ProgressBar progressCreateTrip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_trip);

        initToolbar();

        initComponents();
    }

    private void initToolbar() {
        Toolbar toolbar = findViewById(R.id.trip_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Your Trip");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close_white);
    }

    private void initComponents() {

        tripDaoImpl = new TripDaoImpl(this);

        etTripTitle = findViewById(R.id.et_trip_title);
        etStartDate = findViewById(R.id.et_start_date);
        etEndDate = findViewById(R.id.et_end_date);
        etEndDate.setEnabled(false);
        btnCreate = findViewById(R.id.btn_create_trip);
        btnUpdate = findViewById(R.id.btn_update_trip);
        progressCreateTrip = findViewById(R.id.progress_create_trip);

        trip = (Trip) getIntent().getSerializableExtra("Trip");

        if (trip != null) {
            etTripTitle.getEditText().setText(trip.getName());
            etStartDate.getEditText().setText(Tools.formatDate("YYYY-MM-dd", trip.getStartDate()));
            etEndDate.getEditText().setText(Tools.formatDate("YYYY-MM-dd", trip.getEndDate()));
            btnCreate.setVisibility(View.GONE);
            btnUpdate.setVisibility(View.VISIBLE);
        } else {
            btnUpdate.setVisibility(View.GONE);
            btnCreate.setVisibility(View.VISIBLE);

            String tripName = "Trip to " + getIntent().getStringExtra("Place_Name");
            etTripTitle.getEditText().setText(tripName);
            destination = (Destination) getIntent().getSerializableExtra("Destination");
        }

        calendar = Calendar.getInstance();

        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

        etStartDate.getEditText().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showStartDatePicker();
            }
        });

        etEndDate.getEditText().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showEndDatePicker();
            }
        });


        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createTrip();
            }
        });

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateTrip();
            }
        });

    }

    private void showStartDatePicker() {
        DialogDatePicker startDate = new DialogDatePicker();

        Bundle args = new Bundle();
        args.putInt("year", year);
        args.putInt("month", month);
        args.putInt("dayOfMonth", dayOfMonth);
        args.putLong("timeInMillis", calendar.getTimeInMillis());

        startDate.setArguments(args);
        startDate.setCallback(startDateListener);
        startDate.show(getSupportFragmentManager(), "START DATE PICKER");
    }

    private void showEndDatePicker() {
        DialogDatePicker endDate = new DialogDatePicker();

        Bundle args1 = new Bundle();
        args1.putInt("year", year);
        args1.putInt("month", month);
        args1.putInt("dayOfMonth", dayOfMonth);
        args1.putLong("timeInMillis", timeInMillis);

        endDate.setArguments(args1);
        endDate.setCallback(endDateListener);
        endDate.show(getSupportFragmentManager(), "END DATE PICKER");
    }

    DatePickerDialog.OnDateSetListener startDateListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            setCalendar(year, month, dayOfMonth, etStartDate.getEditText());
            setCalendar(year, month, dayOfMonth + 7, etEndDate.getEditText());
            timeInMillis = Calendar.getInstance().getTimeInMillis() + (dayOfMonth - CreateTripActivity.this.dayOfMonth) * 86400000;
            etEndDate.setEnabled(true);
        }
    };

    DatePickerDialog.OnDateSetListener endDateListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            setCalendar(year, month, dayOfMonth, etEndDate.getEditText());
        }
    };

    private void setCalendar(int year, int month, int dayOfMonth, EditText editText) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        setDate(calendar, editText);
    }

    private void setDate(Calendar calendar, EditText editText) {
        Date date = calendar.getTime();
        SimpleDateFormat dateFormat = new SimpleDateFormat("YYYY-MM-dd", Locale.getDefault());
        String formattedDate = dateFormat.format(date);
        editText.setText(formattedDate);
    }

    private boolean validateCreateTrip() {

        if (EditTextValidation.isEmpty(etTripTitle) | EditTextValidation.isEmpty(etStartDate) | EditTextValidation.isEmpty(etEndDate)) {
            Tools.vibrateDevice(this);
            return false;
        }
        return true;
    }

    private void createTrip() {
        if (validateCreateTrip()) {
            Tools.toggleVisibility(progressCreateTrip, btnCreate, true);
            String tripName = etTripTitle.getEditText().getText().toString().trim();
            String startDate = etStartDate.getEditText().getText().toString().trim();
            String endDate = etEndDate.getEditText().getText().toString().trim();

            Trip trip = new Trip(tripName, startDate, endDate, destination, new UserSession(this).getUser().getId());
            tripDaoImpl.createTrip(trip);
        }
    }

    private void updateTrip() {
        if (validateCreateTrip()) {
            Tools.toggleVisibility(progressCreateTrip, btnUpdate, true);
            String tripName = etTripTitle.getEditText().getText().toString().trim();
            String startDate = etStartDate.getEditText().getText().toString().trim();
            String endDate = etEndDate.getEditText().getText().toString().trim();

            Trip tripToUpdate = new Trip(tripName, startDate, endDate);
            tripDaoImpl.updateTrip(trip.getId(), tripToUpdate);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        tripDaoImpl.setTripActionsListener(new TripDaoImpl.TripActionsListener() {
            @Override
            public void onTripsReceived(List<Trip> myTrips) {

            }

            @Override
            public void onTripCreated(Trip trip) {
                Tools.toggleVisibility(progressCreateTrip, btnCreate, false);
                Intent viewTrip = new Intent(CreateTripActivity.this, ViewTripActivity.class);
                viewTrip.putExtra("TRIP", trip);
                startActivity(viewTrip);
                finish();
            }

            @Override
            public void onTripUpdated() {
                Tools.toggleVisibility(progressCreateTrip, btnUpdate, false);
                Toast.makeText(CreateTripActivity.this, "Trip updated successfully", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(CreateTripActivity.this, MainActivity.class));
                finishAffinity();
                finish();
            }

            @Override
            public void onTripDeleted() {

            }
        });
    }
}
