package com.raju.tripplanner.activities;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.raju.tripplanner.R;
import com.raju.tripplanner.dialogs.DialogDatePicker;
import com.raju.tripplanner.models.Destination;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class CreateTripActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btnCreate;
    private EditText etTripTitle, etStartDate, etEndDate;
    private int year, month, dayOfMonth;
    private Calendar calendar;
    private long timeInMillis;
    private String tripName;
    private Destination destination;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_trip);

        initToolbar();

        initComponents();

        tripName = "Trip to " + getIntent().getStringExtra("Place_Name");
        etTripTitle.setText(tripName);
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

        etTripTitle = findViewById(R.id.et_trip_title);

        etStartDate = findViewById(R.id.et_start_date);
        etStartDate.setOnClickListener(this);

        etEndDate = findViewById(R.id.et_end_date);
        etEndDate.setEnabled(false);
        etEndDate.setOnClickListener(this);

        btnCreate = findViewById(R.id.btn_create_trip);
        btnCreate.setOnClickListener(this);

        calendar = Calendar.getInstance();

        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.et_start_date:
                DialogDatePicker startDate = new DialogDatePicker();

                Bundle args = new Bundle();
                args.putInt("year", year);
                args.putInt("month", month);
                args.putInt("dayOfMonth", dayOfMonth);
                args.putLong("timeInMillis", calendar.getTimeInMillis());

                startDate.setArguments(args);
                startDate.setCallback(startDateListener);
                startDate.show(getSupportFragmentManager(), "START DATE PICKER");
                break;

            case R.id.et_end_date:
                DialogDatePicker endDate = new DialogDatePicker();

                Bundle args1 = new Bundle();
                args1.putInt("year", year);
                args1.putInt("month", month);
                args1.putInt("dayOfMonth", dayOfMonth);
                args1.putLong("timeInMillis", timeInMillis);

                endDate.setArguments(args1);
                endDate.setCallback(endDateListener);
                endDate.show(getSupportFragmentManager(), "END DATE PICKER");
                break;

            case R.id.btn_create_trip:
                Toast.makeText(this, "successfulllllll", Toast.LENGTH_SHORT).show();
                finish();
        }
    }

    DatePickerDialog.OnDateSetListener startDateListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            setCalendar(year, month, dayOfMonth, etStartDate);
            setCalendar(year, month, dayOfMonth + 7, etEndDate);
            timeInMillis = Calendar.getInstance().getTimeInMillis() + (dayOfMonth - CreateTripActivity.this.dayOfMonth) * 86400000;
            etEndDate.setEnabled(true);
        }
    };

    DatePickerDialog.OnDateSetListener endDateListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            setCalendar(year, month, dayOfMonth, etEndDate);
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
}
