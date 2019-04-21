package com.raju.tripplanner.dialogs;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;

import com.raju.tripplanner.R;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

public class DialogDatePicker extends AppCompatDialogFragment {
    private int year, month, dayOfMonth;
    private long timeInMillis;
    private DatePickerDialog.OnDateSetListener onDateSetListener;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), R.style.DatePickerStyle,
                onDateSetListener, year, month, dayOfMonth);
        datePickerDialog.getDatePicker().setMinDate(timeInMillis);
        return datePickerDialog;
    }

    @Override
    public void setArguments(@Nullable Bundle args) {
        super.setArguments(args);

        year = args.getInt("year");
        month = args.getInt("month");
        dayOfMonth = args.getInt("dayOfMonth");
        timeInMillis = args.getLong("timeInMillis");
    }

    public void setCallback(DatePickerDialog.OnDateSetListener onDateSetListener) {
        this.onDateSetListener = onDateSetListener;
    }
}
