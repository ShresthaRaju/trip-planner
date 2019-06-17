package com.raju.tripplanner.activities;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.textfield.TextInputLayout;
import com.raju.tripplanner.ApiCalls.TripAPI;
import com.raju.tripplanner.R;
import com.raju.tripplanner.dialogs.DialogDatePicker;
import com.raju.tripplanner.models.Destination;
import com.raju.tripplanner.models.Trip;
import com.raju.tripplanner.utils.ApiResponse.TripResponse;
import com.raju.tripplanner.utils.EditTextValidation;
import com.raju.tripplanner.utils.RetrofitClient;
import com.raju.tripplanner.utils.Tools;
import com.raju.tripplanner.utils.UserSession;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreateTripActivity extends AppCompatActivity {

    private Button btnCreate;
    private TextInputLayout etTripTitle, etStartDate, etEndDate;
    private int year, month, dayOfMonth;
    private Calendar calendar;
    private long timeInMillis;
    private String tripName;
    private Destination destination;
    private TripAPI tripAPI;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_trip);

        initToolbar();

        initComponents();

        tripName = "Trip to " + getIntent().getStringExtra("Place_Name");
        etTripTitle.getEditText().setText(tripName);
        destination = (Destination) getIntent().getSerializableExtra("Destination");
    }

    private void initToolbar() {
        Toolbar toolbar = findViewById(R.id.trip_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Create your trip");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close_white);
    }

    private void initComponents() {

        calendar = Calendar.getInstance();

        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

        tripAPI = RetrofitClient.getInstance().create(TripAPI.class);

        etTripTitle = findViewById(R.id.et_trip_title);

        etStartDate = findViewById(R.id.et_start_date);
        etStartDate.getEditText().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showStartDatePicker();
            }
        });

        etEndDate = findViewById(R.id.et_end_date);
        etEndDate.setEnabled(false);
        etEndDate.getEditText().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showEndDatePicker();
            }
        });

        btnCreate = findViewById(R.id.btn_create_trip);
        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateCreateTrip();
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
        SimpleDateFormat dateFormat = new SimpleDateFormat("YYYY-MM-dd");
        String formattedDate = dateFormat.format(date);
        editText.setText(formattedDate);
    }

    private void validateCreateTrip() {

        if (EditTextValidation.isEmpty(etTripTitle) | EditTextValidation.isEmpty(etStartDate) | EditTextValidation.isEmpty(etEndDate)) {
            Tools.vibrateDevice(this);
            return;
        }

        tripName = etTripTitle.getEditText().getText().toString().trim();
        String startDate = etStartDate.getEditText().getText().toString().trim();
        String endDate = etEndDate.getEditText().getText().toString().trim();

        Trip trip = new Trip(tripName, startDate, endDate, destination, new UserSession(this).getUserId());
        createTrip(trip);

    }

    private void createTrip(Trip trip) {

        Call<TripResponse> createTripCall = tripAPI.createTrip("Bearer " + new UserSession(this).getAuthToken(), trip);
        createTripCall.enqueue(new Callback<TripResponse>() {
            @Override
            public void onResponse(Call<TripResponse> call, Response<TripResponse> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(CreateTripActivity.this, "ERROR: " + response.code() + " " + response.message(), Toast.LENGTH_LONG).show();
                    return;
                }

                TripResponse tripResponse = response.body();
                Intent viewTrip = new Intent(CreateTripActivity.this, ViewTripActivity.class);
                viewTrip.putExtra("TRIP", tripResponse.getTrip());
                startActivity(viewTrip);
                finish();

            }

            @Override
            public void onFailure(Call<TripResponse> call, Throwable t) {
                Toast.makeText(CreateTripActivity.this, "FAILED: " + t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }
}
