package com.raju.tripplanner.authentication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.raju.tripplanner.MainActivity;
import com.raju.tripplanner.R;
import com.raju.tripplanner.utils.EditTextValidation;

import androidx.appcompat.app.AppCompatActivity;

public class SignInActivity extends AppCompatActivity {

    private TextInputLayout signInEmail, signInPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        getWindow().setBackgroundDrawableResource(R.drawable.bg_sign_in);

        initComponents();
    }

    private void initComponents() {
        signInEmail = findViewById(R.id.et_sign_in_email);
        signInPassword = findViewById(R.id.et_sign_in_password);
    }

    public void signIn(View view) {
        if (EditTextValidation.validateField(signInEmail) && EditTextValidation.validateField(signInPassword)) {
            startActivity(new Intent(this, MainActivity.class));
        }
    }

    public void showSignUpScreen(View view) {
        startActivity(new Intent(this, SignUpActivity.class));
    }
}
