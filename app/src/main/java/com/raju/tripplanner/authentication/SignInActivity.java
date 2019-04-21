package com.raju.tripplanner.authentication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.android.material.textfield.TextInputLayout;
import com.raju.tripplanner.MainActivity;
import com.raju.tripplanner.R;
import com.raju.tripplanner.utils.EditTextValidation;
import com.raju.tripplanner.utils.UserSession;

import androidx.appcompat.app.AppCompatActivity;

public class SignInActivity extends AppCompatActivity {

    private TextInputLayout signInEmail, signInPassword;
    private UserSession userSession;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        userSession = new UserSession(this);

        if (userSession.getSession()) {
            Intent mainActivity = new Intent(this, MainActivity.class);
            mainActivity.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(mainActivity);
            finish();
        }

        getWindow().setBackgroundDrawableResource(R.drawable.bg_sign_in);

        initComponents();
    }

    private void initComponents() {
        signInEmail = findViewById(R.id.et_sign_in_email);
        signInPassword = findViewById(R.id.et_sign_in_password);
    }

    public void signIn(View view) {
        if (!EditTextValidation.isEmpty(signInEmail) && !EditTextValidation.isEmpty(signInPassword)) {
            String email = signInEmail.getEditText().getText().toString().trim();
            String password = signInPassword.getEditText().getText().toString().trim();
            if (email.equals("admin@example.com") && password.equals("admin")) {
                new UserSession(this).startSession();
                Intent mainActivity = new Intent(new Intent(this, MainActivity.class));
                mainActivity.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(mainActivity);
                finish();
            } else {
                signInEmail.setError("Invalid credentials!!!");
            }
        }
    }

    public void showSignUpScreen(View view) {
        startActivity(new Intent(this, SignUpActivity.class));
    }
}
