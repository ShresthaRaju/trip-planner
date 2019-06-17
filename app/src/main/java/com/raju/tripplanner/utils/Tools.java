package com.raju.tripplanner.utils;

import android.content.Context;
import android.os.Build;
import android.os.VibrationEffect;
import android.os.Vibrator;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import static android.content.Context.VIBRATOR_SERVICE;

public class Tools {

    public static void vibrateDevice(Context context) {
        Vibrator vibrator = (Vibrator) context.getSystemService(VIBRATOR_SERVICE);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.O) {
            vibrator.vibrate(VibrationEffect.createOneShot(300, VibrationEffect.DEFAULT_AMPLITUDE));
        } else {
            vibrator.vibrate(300);
        }
    }

    public static String formatDate(String date) {
        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        SimpleDateFormat outputFormat = new SimpleDateFormat("MMM dd");

        try {
            String formattedDate = outputFormat.format(inputFormat.parse(date));
            return formattedDate;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "";
    }
}
