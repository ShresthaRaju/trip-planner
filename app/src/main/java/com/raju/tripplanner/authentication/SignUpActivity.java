package com.raju.tripplanner.authentication;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputLayout;
import com.raju.tripplanner.DAO.AuthAPI;
import com.raju.tripplanner.R;
import com.raju.tripplanner.models.User;
import com.raju.tripplanner.utils.AuthApiResponse;
import com.raju.tripplanner.utils.EditTextValidation;
import com.raju.tripplanner.utils.RetrofitClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignUpActivity extends AppCompatActivity {

    private TextInputLayout signUpEmail, signUpUsername, signUpPassword;
    private Button btnGetStarted;
    private AuthAPI authAPI;

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
        btnGetStarted = findViewById(R.id.btn_get_started);

        btnGetStarted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateSignUp();
            }
        });

//        retrofit client custom class
        authAPI = RetrofitClient.getInstance().create(AuthAPI.class);
    }

    //    validate sign up fields
    private void validateSignUp() {
        if (EditTextValidation.isEmpty(signUpEmail) && EditTextValidation.isEmpty(signUpUsername)
                && EditTextValidation.isEmpty(signUpPassword)) {
            return;
        } else {
            String email = signUpEmail.getEditText().getText().toString().trim();
            String username = signUpUsername.getEditText().getText().toString().trim();
            String password = signUpPassword.getEditText().getText().toString().trim();

            User newUser = new User(email, username, password);
            signUp(newUser);
        }
    }

    //    register new user
    private void signUp(User user) {

        Call<AuthApiResponse> signUpCall = authAPI.registerUser(user);

        // enqueue method runs the api call in background thread
        signUpCall.enqueue(new Callback<AuthApiResponse>() {
            @Override
            public void onResponse(Call<AuthApiResponse> call, Response<AuthApiResponse> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(SignUpActivity.this, response.code() + " " + response.message(), Toast.LENGTH_SHORT).show();
                    return;
                }

                Toast.makeText(SignUpActivity.this, "Sign Up Successful !", Toast.LENGTH_SHORT).show();
                finish();

//                AuthApiResponse signUpApiResponse = response.body();
//                Log.i("CREATED_USER", signUpApiResponse.getUser().getEmail());
            }

            @Override
            public void onFailure(Call<AuthApiResponse> call, Throwable t) {
//                Throwable is the super class of Exception class
                Toast.makeText(SignUpActivity.this, "ERROR:\n" + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    //close sign up screen
    public void closeSignUpScreen(View view) {
        finish();
    }

}
