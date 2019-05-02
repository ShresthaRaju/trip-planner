package com.raju.tripplanner.authentication;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.raju.tripplanner.R;
import com.raju.tripplanner.utils.EditTextValidation;

import androidx.appcompat.app.AppCompatActivity;

public class SignUpActivity extends AppCompatActivity {

    private TextInputLayout signUpEmail, signUpUsername, signUpPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        initComponents();
    }

    private void initComponents() {
        signUpEmail = findViewById(R.id.et_sign_up_email);
        signUpUsername = findViewById(R.id.et_sign_up_username);
        signUpPassword = findViewById(R.id.et_sign_up_password);
    }

    public void closeSignUpScreen(View view) {
        finish();
    }

    public void signUp(View view) {
        if (EditTextValidation.isEmpty(signUpEmail) && EditTextValidation.isEmpty(signUpUsername)
                && EditTextValidation.isEmpty(signUpPassword)) {
            return;
        }
    }
}
