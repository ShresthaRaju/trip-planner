package com.raju.tripplanner.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.textfield.TextInputLayout;
import com.raju.tripplanner.DaoImpl.UserDaoImpl;
import com.raju.tripplanner.R;
import com.raju.tripplanner.models.User;
import com.raju.tripplanner.utils.EditTextValidation;
import com.raju.tripplanner.utils.Error;
import com.raju.tripplanner.utils.Tools;
import com.raju.tripplanner.utils.UserSession;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class UpdateProfileActivity extends AppCompatActivity {
    private CircleImageView profileUpdateDp;
    private TextInputLayout etFirstName, etFamilyName, etEmail, etUsername, etOldPassword, etNewPassword;
    private ProgressBar updateDetailsProgress, changePasswordProgress;
    private Button btnUpdateDetails, btnChangePassword;
    private User authUser;
    private UserDaoImpl userDaoImpl;
    private UserSession userSession;
    private HashMap<String, TextInputLayout> errorMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_profile);

        initComponents();

    }

    private void initComponents() {

        Toolbar toolbar = findViewById(R.id.profile_update_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Update Your Profile");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        authUser = new UserSession(this).getUser();
        userDaoImpl = new UserDaoImpl(this);
        userSession = new UserSession(this);

        profileUpdateDp = findViewById(R.id.profile_update_dp);
        etFirstName = findViewById(R.id.et_update_first_name);
        etFamilyName = findViewById(R.id.et_update_family_name);
        etEmail = findViewById(R.id.et_update_email);
        etUsername = findViewById(R.id.et_update_username);
        etOldPassword = findViewById(R.id.et_old_password);
        etNewPassword = findViewById(R.id.et_new_password);
        btnUpdateDetails = findViewById(R.id.btn_update_profile);
        btnChangePassword = findViewById(R.id.btn_change_password);
        updateDetailsProgress = findViewById(R.id.update_details_progress);
        changePasswordProgress = findViewById(R.id.change_password_progress);

        errorMap = new HashMap<>();
        errorMap.put("firstName", etFirstName);
        errorMap.put("familyName", etFamilyName);
        errorMap.put("email", etEmail);
        errorMap.put("username", etUsername);
        errorMap.put("oldPassword", etOldPassword);
        errorMap.put("newPassword", etNewPassword);

        Picasso.get().load(Tools.BASE_URI + authUser.getDisplayPicture()).into(profileUpdateDp);
        etFirstName.getEditText().setText(authUser.getFirstName());
        etFamilyName.getEditText().setText(authUser.getFamilyName());
        etEmail.getEditText().setText(authUser.getEmail());
        etUsername.getEditText().setText(authUser.getUsername());
    }

    public void updateProfile(View view) {
        if (validProfileDetails()) {
            Tools.toggleVisibility(updateDetailsProgress, btnUpdateDetails, true);
            String firstName = etFirstName.getEditText().getText().toString().trim();
            String familyName = etFamilyName.getEditText().getText().toString().trim();
            String email = etEmail.getEditText().getText().toString().trim();
            String username = etUsername.getEditText().getText().toString().trim();

            userDaoImpl.updateDetails(firstName, familyName, email, username);
        }
    }

    public void changePassword(View view) {
        if (validPasswordDetails()) {
            Tools.toggleVisibility(changePasswordProgress, btnChangePassword, true);
            String oldPassword = etOldPassword.getEditText().getText().toString().trim();
            String newPassword = etNewPassword.getEditText().getText().toString().trim();

            userDaoImpl.changePassword(oldPassword, newPassword);
        }
    }

    private boolean validProfileDetails() {
        if (EditTextValidation.isEmpty(etFirstName) | EditTextValidation.isEmpty(etFamilyName)
                | EditTextValidation.isEmpty(etEmail) | EditTextValidation.isEmpty(etUsername)) {
            Tools.vibrateDevice(this);
            return false;
        }
        return true;
    }

    private boolean validPasswordDetails() {
        if (EditTextValidation.isEmpty(etOldPassword) | EditTextValidation.isEmpty(etNewPassword)) {
            Tools.vibrateDevice(this);
            return false;
        }
        return true;
    }

    @Override
    protected void onStart() {
        super.onStart();
        userDaoImpl.setUserProfileListener(new UserDaoImpl.UserProfileListener() {
            @Override
            public void onDpUploaded(User updatedUser) {

            }

            @Override
            public void onDetailsUpdated(User updatedUser) {
                userSession.startSession(updatedUser, userSession.getAuthToken());
                Tools.toggleVisibility(updateDetailsProgress, btnUpdateDetails, false);
                Toast.makeText(UpdateProfileActivity.this, "Details updated successfully...", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onPasswordChanged() {
                Tools.toggleVisibility(changePasswordProgress, btnChangePassword, false);
                Toast.makeText(UpdateProfileActivity.this, "Password changed successfully...", Toast.LENGTH_SHORT).show();
                finish();
            }

            @Override
            public void onSignedOut() {

            }

            @Override
            public void onError(Error error) {
                Tools.toggleVisibility(updateDetailsProgress, btnUpdateDetails, false);
                Tools.toggleVisibility(changePasswordProgress, btnChangePassword, false);

                for (String key : errorMap.keySet()) {
                    if (error.getField().equals(key)) {
                        errorMap.get(error.getField()).setError(error.getMessage());
                        Tools.vibrateDevice(UpdateProfileActivity.this);
                    }
                }
            }
        });
    }
}
