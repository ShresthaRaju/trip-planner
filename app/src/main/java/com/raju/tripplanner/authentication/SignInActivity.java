package com.raju.tripplanner.authentication;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
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
import com.raju.tripplanner.dialogs.DialogProgress;
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
    private GoogleSignInClient signInClient;
    private SignInButton googleSignIn;
    private static int RC_GOOGLE_SIGN_IN = 01;
    private DialogProgress dialogProgress;

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

        GoogleSignInOptions signInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        signInClient = GoogleSignIn.getClient(this, signInOptions);

        googleSignIn = findViewById(R.id.btn_google_sign_in);
        googleSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                googleSignIn();
            }
        });
    }

    private void validateSignIn() {
        if (EditTextValidation.isEmpty(signInEmail) && EditTextValidation.isEmpty(signInPassword)) {
            vibrateDevice();
            return;
        } else {
            String email = signInEmail.getEditText().getText().toString().trim();
            String password = signInPassword.getEditText().getText().toString().trim();

            User user = new User(email, password);
            signIn(user);
        }
    }

    public void signIn(User user) {

        dialogProgress = new DialogProgress("Authenticating...");
        dialogProgress.setCancelable(false);
        dialogProgress.show(getSupportFragmentManager(), "SIGN IN PROGRESS");

        Call<SignInResponse> signInCall = authAPI.signIn(user);

        signInCall.enqueue(new Callback<SignInResponse>() {
            @Override
            public void onResponse(Call<SignInResponse> call, Response<SignInResponse> response) {
                if (!response.isSuccessful()) {
                    dialogProgress.dismiss();
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
                dialogProgress.dismiss();

                SignInResponse signInResponse = response.body();
                new UserSession(SignInActivity.this).startSession(signInResponse.getMessage(), signInResponse.getAuthToken());
                Intent mainActivity = new Intent(new Intent(SignInActivity.this, MainActivity.class));
                mainActivity.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(mainActivity);
                finish();


            }

            @Override
            public void onFailure(Call<SignInResponse> call, Throwable t) {
                dialogProgress.dismiss();
                Toast.makeText(SignInActivity.this, "ERROR:\n" + t.getLocalizedMessage(), Toast.LENGTH_LONG).show();
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

//    private void configureGoogleSignIn() {
//
//    }

    private void googleSignIn() {
        Intent googleSignIn = signInClient.getSignInIntent();
        startActivityForResult(googleSignIn, RC_GOOGLE_SIGN_IN);
    }

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
//            details += account.getFamilyName() + " ";
//            details += account.getGivenName() + " ";
//            details += account.getEmail() + " ";
//            details += account.getId() + " ";
//            Uri photo = account.getPhotoUrl();
//            details += photo;
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
