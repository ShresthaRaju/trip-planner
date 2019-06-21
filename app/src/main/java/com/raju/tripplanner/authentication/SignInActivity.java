package com.raju.tripplanner.authentication;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputLayout;
import com.raju.tripplanner.DAO.AuthAPI;
import com.raju.tripplanner.MainActivity;
import com.raju.tripplanner.R;
import com.raju.tripplanner.models.User;
import com.raju.tripplanner.utils.ApiResponse.SignInResponse;
import com.raju.tripplanner.utils.EditTextValidation;
import com.raju.tripplanner.utils.RetrofitClient;
import com.raju.tripplanner.utils.Tools;
import com.raju.tripplanner.utils.UserSession;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignInActivity extends AppCompatActivity {

    private TextInputLayout signInEmail, signInPassword;
    private AuthAPI authAPI;
    private UserSession userSession;
    private GoogleSignInClient signInClient;
    private SignInButton googleSignIn;
    private static int RC_GOOGLE_SIGN_IN = 01;
    private ProgressBar signInProgress;
    private FloatingActionButton fabSignIn;

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

        fabSignIn = findViewById(R.id.fab_sign_in);
        fabSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });

        signInProgress = findViewById(R.id.sign_in_progress);

        authAPI = RetrofitClient.getInstance().create(AuthAPI.class);

        configureGoogleSignIn();

        googleSignIn = findViewById(R.id.btn_google_sign_in);
        googleSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                googleSignIn();
            }
        });
    }

    private boolean validateSignIn() {
        if (EditTextValidation.isEmpty(signInEmail) | EditTextValidation.isEmpty(signInPassword)) {
            Tools.vibrateDevice(this);
            return false;
        }
        return true;
    }

    public void signIn() {

        if (validateSignIn()) {
            fabSignIn.setVisibility(View.GONE);
            signInProgress.setVisibility(View.VISIBLE);

            String email = signInEmail.getEditText().getText().toString().trim();
            String password = signInPassword.getEditText().getText().toString().trim();

            Call<SignInResponse> signInCall = authAPI.signIn(new User(email, password));

            signInCall.enqueue(new Callback<SignInResponse>() {
                @Override
                public void onResponse(Call<SignInResponse> call, Response<SignInResponse> response) {
                    if (!response.isSuccessful()) {
                        signInProgress.setVisibility(View.GONE);
                        fabSignIn.setVisibility(View.VISIBLE);
                        if (response.code() == 404) {
                            signInEmail.setError("User does not exist !");
                            Tools.vibrateDevice(SignInActivity.this);
                        } else {
                            signInEmail.setError("Invalid credentials!!!");
                            Tools.vibrateDevice(SignInActivity.this);
                        }
                        return;
                    }

                    // Log the response in JSON format
//                Log.i("sign", new Gson().toJson(response.body().getUser()));
                    signInProgress.setVisibility(View.GONE);

                    SignInResponse signInResponse = response.body();

                    new UserSession(SignInActivity.this).startSession(signInResponse.getUser(), signInResponse.getAuthToken());
                    Intent mainActivity = new Intent(new Intent(SignInActivity.this, MainActivity.class));
                    mainActivity.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(mainActivity);
                    finish();
                }

                @Override
                public void onFailure(Call<SignInResponse> call, Throwable t) {
                    signInProgress.setVisibility(View.GONE);
                    fabSignIn.setVisibility(View.VISIBLE);
                    Toast.makeText(SignInActivity.this, "FAILED: " + t.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                }
            });
        }
    }

    public void showSignUpScreen(View view) {
        startActivity(new Intent(this, SignUpActivity.class));
    }


    private void configureGoogleSignIn() {
        GoogleSignInOptions signInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        signInClient = GoogleSignIn.getClient(this, signInOptions);
    }

    private void googleSignIn() {
        Intent googleSignIn = signInClient.getSignInIntent();
        startActivityForResult(googleSignIn, RC_GOOGLE_SIGN_IN);
    }

    //
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_GOOGLE_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {

        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            String details = account.getDisplayName() + " ";
            details += account.getFamilyName() + " ";
            details += account.getGivenName() + " ";
            details += account.getEmail() + " ";
            details += account.getId() + " ";
            Uri photo = account.getPhotoUrl();
            details += photo;
            Log.i("Dettt", details);
        } catch (ApiException e) {
            Log.w("Google_Sign_In_Error", "signInResult:failed code=" + e.getStatusCode());
            Toast.makeText(this, e.getStatusCode() + " Failed", Toast.LENGTH_LONG).show();
        }

    }

    @Override
    protected void onStart() {
        super.onStart();
        // Check for existing Google Sign In account, if the user is already signed in
        // the GoogleSignInAccount will be non-null.
        GoogleSignInAccount signedInAccount = GoogleSignIn.getLastSignedInAccount(this);
        if (signedInAccount != null) {
            Intent mainActivity = new Intent(new Intent(SignInActivity.this, MainActivity.class));
            mainActivity.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(mainActivity);
            finish();
        }
    }
}
