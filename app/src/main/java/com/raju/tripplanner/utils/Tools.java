package com.raju.tripplanner.utils;

import android.content.Context;
import android.os.Build;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;

import static android.content.Context.VIBRATOR_SERVICE;

public class Tools {

    //    public static final String BASE_URL = "http:172.26.0.86:7000/api/";
//    public static final String IMAGE_URI = "http:172.26.0.86:7000/uploads/";
    public static final String BASE_URL = "http:192.168.0.101:7000/api/";
    public static final String IMAGE_URI = "http:192.168.0.101:7000/uploads/";
    public static final String NEARBY_PLACE_API = "https://maps.googleapis.com/maps/api/place/nearbysearch/";

    public static void vibrateDevice(Context context) {
        Vibrator vibrator = (Vibrator) context.getSystemService(VIBRATOR_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vibrator.vibrate(VibrationEffect.createOneShot(300, VibrationEffect.DEFAULT_AMPLITUDE));
        }
    }

    public static String formatDate(String inputPattern, String outputPattern, String date) {
        SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern, Locale.getDefault());
        SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern, Locale.getDefault());

        try {
            return outputFormat.format(inputFormat.parse(date));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static void toggleVisibility(ProgressBar progressBar, Button button, boolean visibility) {
        if (visibility) {
            progressBar.setVisibility(View.VISIBLE);
            button.setVisibility(View.GONE);
        } else {
            progressBar.setVisibility(View.GONE);
            button.setVisibility(View.VISIBLE);
        }
    }
}
