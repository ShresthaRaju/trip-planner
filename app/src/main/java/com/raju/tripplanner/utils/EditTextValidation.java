package com.raju.tripplanner.utils;

import com.google.android.material.textfield.TextInputLayout;

public class EditTextValidation {

    public static boolean validateField(TextInputLayout textInputLayout) {
        if (textInputLayout.getEditText().getText().toString().trim().isEmpty()) {
            textInputLayout.setError(textInputLayout.getEditText().getHint().toString() + " is required");
            return false;
        } else {
            textInputLayout.setError(null);
            return true;
        }
    }

}
