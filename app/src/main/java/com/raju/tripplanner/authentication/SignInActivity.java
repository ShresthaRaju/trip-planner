package com.raju.tripplanner.authentication;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputLayout;
import com.raju.tripplanner.DaoImpl.AuthDaoImpl;
import com.raju.tripplanner.MainActivity;
import com.raju.tripplanner.R;
import com.raju.tripplanner.models.User;
import com.raju.tripplanner.utils.ApiResponse.SignInResponse;
import com.raju.tripplanner.utils.DatabaseHelper;
import com.raju.tripplanner.utils.EditTextValidation;
import com.raju.tripplanner.utils.Error;
import com.raju.tripplanner.utils.Tools;
import com.raju.tripplanner.utils.UserSession;

public class SignInActivity extends AppCompatActivity {

    private TextInputLayout signInEmail, signInPassword;
    private AuthDaoImpl authDaoImpl;
    private ProgressBar signInProgress;
    private FloatingActionButton fabSignIn;
    private DatabaseHelper databaseHelper;
    private SensorManager sensorManager;
    private Sensor proximitySensor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        UserSession userSession = new UserSession(this);

        if (userSession.getSession()) {
            Intent mainActivity = new Intent(this, MainActivity.class);
            mainActivity.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(mainActivity);

            finish();
        }

        getWindow().setBackgroundDrawableResource(R.drawable.bg_sign_in);

        initComponents();

        authListener();

    }

    private void initComponents() {

        authDaoImpl = new AuthDaoImpl();
        databaseHelper = new DatabaseHelper(this);

        signInEmail = findViewById(R.id.et_sign_in_email);
        signInPassword = findViewById(R.id.et_sign_in_password);

        fabSignIn = findViewById(R.id.fab_sign_in);
        fabSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });

        signInProgress = findViewById(R.id.sign_in_progress);

        //proximity sensor
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        proximitySensor = sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);
        if (proximitySensor == null) {
            Toast.makeText(this, "Proximity sensor is not available", Toast.LENGTH_LONG).show();
        }

    }

    private boolean validSignInDetails() {
        if (EditTextValidation.isEmpty(signInEmail) | EditTextValidation.isEmpty(signInPassword)) {
            Tools.vibrateDevice(this);
            return false;
        }
        return true;
    }

    public void signIn() {

        if (validSignInDetails()) {
            fabSignIn.setVisibility(View.GONE);
            signInProgress.setVisibility(View.VISIBLE);

            String email = signInEmail.getEditText().getText().toString().trim();
            String password = signInPassword.getEditText().getText().toString().trim();

            Tools.StrictMode();

            SignInResponse signInResponse = authDaoImpl.signIn(new User(email, password));
            if (signInResponse != null && signInResponse.isSuccess()) {
                signInProgress.setVisibility(View.GONE);

                new UserSession(SignInActivity.this).startSession(signInResponse.getUser(), signInResponse.getAuthToken());

                databaseHelper.insertAuthUser(signInResponse.getUser());

                Intent mainActivity = new Intent(new Intent(SignInActivity.this, MainActivity.class));
                mainActivity.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(mainActivity);
                finish();
            }
        }
    }

    public void showSignUpScreen(View view) {
        startActivity(new Intent(this, SignUpActivity.class));
    }

    private void authListener() {
        authDaoImpl.setAuthListener(new AuthDaoImpl.AuthListener() {

            @Override
            public void onError(Error error) {
                signInProgress.setVisibility(View.GONE);
                fabSignIn.setVisibility(View.VISIBLE);

                if (error.getField().equals("email")) {
                    signInEmail.setError(error.getMessage());
                    Tools.vibrateDevice(SignInActivity.this);
                } else if (error.getField().equals("password")) {
                    signInPassword.setError(error.getMessage());
                    Tools.vibrateDevice(SignInActivity.this);
                }
            }

        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(proximityEventListener, proximitySensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(proximityEventListener);
    }

    private SensorEventListener proximityEventListener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent event) {
            if (event.values[0] <= 0.5) {
                signIn();
            }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }
    };
}
