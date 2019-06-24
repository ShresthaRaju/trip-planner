package com.raju.tripplanner.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.textfield.TextInputLayout;
import com.raju.tripplanner.R;
import com.raju.tripplanner.models.User;
import com.raju.tripplanner.utils.EditTextValidation;
import com.raju.tripplanner.utils.Tools;
import com.raju.tripplanner.utils.UserSession;

public class UpdateProfileActivity extends AppCompatActivity {
    private TextInputLayout firstName, familyName, oldPassword, newPassword;
    private User authUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_profile);

        authUser = new UserSession(this).getUser();

        initComponents();

    }

    private void initComponents() {

        Toolbar toolbar = findViewById(R.id.profile_update_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Update Your Profile");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        firstName = findViewById(R.id.et_update_first_name);
        familyName = findViewById(R.id.et_update_family_name);
        oldPassword = findViewById(R.id.et_old_password);
        newPassword = findViewById(R.id.et_new_password);

        firstName.getEditText().setText(authUser.getFirstName());
        familyName.getEditText().setText(authUser.getFamilyName());
    }

    public void updateProfile(View view) {
        if (validProfileDetails()) {
            Toast.makeText(this, "valid profile", Toast.LENGTH_SHORT).show();
        }
    }

    public void changePassword(View view) {
        if (validPasswordDetails()) {
            Toast.makeText(this, "valid password", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean validProfileDetails() {
        if (EditTextValidation.isEmpty(firstName) | EditTextValidation.isEmpty(familyName)) {
            Tools.vibrateDevice(this);
            return false;
        }
        return true;
    }

    private boolean validPasswordDetails() {
        if (EditTextValidation.isEmpty(oldPassword) | EditTextValidation.isEmpty(newPassword)) {
            Tools.vibrateDevice(this);
            return false;
        }
        return true;
    }
}
