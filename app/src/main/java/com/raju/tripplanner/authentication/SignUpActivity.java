package com.raju.tripplanner.authentication;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.raju.tripplanner.DAO.AuthAPI;
import com.raju.tripplanner.R;
import com.raju.tripplanner.models.User;
import com.raju.tripplanner.utils.APIError;
import com.raju.tripplanner.utils.ApiResponse.UserResponse;
import com.raju.tripplanner.utils.EditTextValidation;
import com.raju.tripplanner.utils.RetrofitClient;
import com.raju.tripplanner.utils.Tools;

import java.io.IOException;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignUpActivity extends AppCompatActivity {

    private TextInputLayout etFirstName, etFamilyName, signUpEmail, signUpUsername, signUpPassword;
    private Button btnGetStarted;
    private ProgressBar signUpProgress;
    private AuthAPI authAPI;
    private HashMap<String, TextInputLayout> errorMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        getWindow().setBackgroundDrawableResource(R.drawable.bg_sign_up);

        initComponents();
    }

    private void initComponents() {
        etFirstName = findViewById(R.id.et_first_name);
        etFamilyName = findViewById(R.id.et_family_name);
        signUpEmail = findViewById(R.id.et_sign_up_email);
        signUpUsername = findViewById(R.id.et_sign_up_username);
        signUpPassword = findViewById(R.id.et_sign_up_password);
        btnGetStarted = findViewById(R.id.btn_get_started);
        signUpProgress = findViewById(R.id.sign_up_progress);

        errorMap = new HashMap<>();
        errorMap.put("firstName", etFirstName);
        errorMap.put("familyName", etFamilyName);
        errorMap.put("email", signUpEmail);
        errorMap.put("username", signUpUsername);
        errorMap.put("password", signUpPassword);

        btnGetStarted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signUp();
            }
        });

//        retrofit client custom class
        authAPI = RetrofitClient.getInstance().create(AuthAPI.class);
    }

    //    validate sign up fields
    private boolean validSignUpDetails() {
        if (EditTextValidation.isEmpty(etFirstName) | EditTextValidation.isEmpty(etFamilyName) | EditTextValidation.isEmpty(signUpEmail)
                | EditTextValidation.isEmpty(signUpUsername) | EditTextValidation.isEmpty(signUpPassword)) {
            Tools.vibrateDevice(this);
            return false;
        }
        return true;
    }

    //    register new user
    private void signUp() {

        if (validSignUpDetails()) {
            Tools.toggleVisibility(signUpProgress, btnGetStarted, true);
            String firstName = etFirstName.getEditText().getText().toString().trim();
            String familyName = etFamilyName.getEditText().getText().toString().trim();
            String email = signUpEmail.getEditText().getText().toString().trim();
            String username = signUpUsername.getEditText().getText().toString().trim();
            String password = signUpPassword.getEditText().getText().toString().trim();

            Call<UserResponse> signUpCall = authAPI.registerUser(new User(firstName, familyName, email, username, password));

            // enqueue method runs the api call in background thread
            signUpCall.enqueue(new Callback<UserResponse>() {
                @Override
                public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                    if (!response.isSuccessful()) {
                        Gson gson = new GsonBuilder().create();
                        APIError apiError = new APIError();
                        Tools.toggleVisibility(signUpProgress, btnGetStarted, false);
                        try {
                            apiError = gson.fromJson(response.errorBody().string(), APIError.class);
                            for (String key : errorMap.keySet()) {
                                if (apiError.getError().getField().equals(key)) {
                                    errorMap.get(apiError.getError().getField()).setError(apiError.getError().getMessage());
                                    Tools.vibrateDevice(SignUpActivity.this);
                                }
                            }
                        } catch (IOException e) {
                            Log.e("IOException", e.getMessage());
                        }
                        return;
                    }

                    Toast.makeText(SignUpActivity.this, "Sign up successful...", Toast.LENGTH_LONG).show();
                    finish();
                }

                @Override
                public void onFailure(Call<UserResponse> call, Throwable t) {
//                Throwable is the super class of Exception class
                    Tools.toggleVisibility(signUpProgress, btnGetStarted, false);
                    Toast.makeText(SignUpActivity.this, "FAILED: " + t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    //close sign up screen
    public void closeSignUpScreen(View view) {
        finish();
    }

}
