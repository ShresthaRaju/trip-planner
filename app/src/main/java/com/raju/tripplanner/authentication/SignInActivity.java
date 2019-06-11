package com.raju.tripplanner.authentication;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputLayout;
import com.raju.tripplanner.DAO.AuthAPI;
import com.raju.tripplanner.MainActivity;
import com.raju.tripplanner.R;
import com.raju.tripplanner.models.User;
import com.raju.tripplanner.utils.EditTextValidation;
import com.raju.tripplanner.utils.RetrofitClient;
import com.raju.tripplanner.utils.SignInResponse;
import com.raju.tripplanner.utils.UserSession;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignInActivity extends AppCompatActivity {

    private TextInputLayout signInEmail, signInPassword;
    private AuthAPI authAPI;
    private UserSession userSession;
    private Vibrator vibrator;

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

        FloatingActionButton fabSignIn = findViewById(R.id.fab_sign_in);
        fabSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateSignIn();
            }
        });

        authAPI = RetrofitClient.getInstance().create(AuthAPI.class);

        vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
    }

    private void validateSignIn() {
        if (EditTextValidation.isEmpty(signInEmail) && EditTextValidation.isEmpty(signInPassword)) {
            return;
        } else {
            String email = signInEmail.getEditText().getText().toString().trim();
            String password = signInPassword.getEditText().getText().toString().trim();

            User user = new User(email, password);
            signIn(user);
        }
    }

    public void signIn(User user) {

        Call<SignInResponse> signInCall = authAPI.signIn(user);

        signInCall.enqueue(new Callback<SignInResponse>() {
            @Override
            public void onResponse(Call<SignInResponse> call, Response<SignInResponse> response) {
                if (!response.isSuccessful()) {
                    if (response.code() == 404) {
                        signInEmail.setError("User does not exist !");
                        vibrateDevice();
                    } else {
                        signInEmail.setError("Invalid credentials!!!");
                        vibrateDevice();
                    }
                    return;
                }

                // Log the response in JSON format
                // Log.i("sign", new Gson().toJson(response.body()));

                SignInResponse signInResponse = response.body();
                new UserSession(SignInActivity.this).startSession(signInResponse.getMessage());
                Intent mainActivity = new Intent(new Intent(SignInActivity.this, MainActivity.class));
                mainActivity.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(mainActivity);
                finish();


            }

            @Override
            public void onFailure(Call<SignInResponse> call, Throwable t) {
                Toast.makeText(SignInActivity.this, "ERROR:\n" + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    public void showSignUpScreen(View view) {
        startActivity(new Intent(this, SignUpActivity.class));
    }

    private void vibrateDevice() {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.O) {
            vibrator.vibrate(VibrationEffect.createOneShot(400, VibrationEffect.DEFAULT_AMPLITUDE));
        } else {
            vibrator.vibrate(400);
        }
    }
}
