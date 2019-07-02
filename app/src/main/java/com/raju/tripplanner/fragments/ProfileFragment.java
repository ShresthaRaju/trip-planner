package com.raju.tripplanner.fragments;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.raju.tripplanner.DaoImpl.UserDaoImpl;
import com.raju.tripplanner.MainActivity;
import com.raju.tripplanner.R;
import com.raju.tripplanner.activities.UpdateProfileActivity;
import com.raju.tripplanner.bottomsheets.ProfileBottomSheet;
import com.raju.tripplanner.models.User;
import com.raju.tripplanner.utils.Error;
import com.raju.tripplanner.utils.Tools;
import com.raju.tripplanner.utils.UserSession;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private AppBarLayout appBarLayout;
    private CollapsingToolbarLayout collapsingToolbar;
    private CircleImageView displayPicture;
    private ImageView coverPhoto;
    private String toolbarTitle;
    private FloatingActionButton fabProfileEdit;
    private User authUser;
    private Button btnUploadDp;
    private UserDaoImpl userDaoImpl;
    private UserSession userSession;

    private TextView email, username;

    private String[] allPermissions = {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
    private static final int REQUEST_PERMISSIONS_ALL = 986;

    //    required empty constructor
    public ProfileFragment() {

    }

    public static ProfileFragment newInstance(String toolbarTitle) {
        ProfileFragment profileFragment = new ProfileFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, toolbarTitle);
        profileFragment.setArguments(args);
        return profileFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View profile = inflater.inflate(R.layout.fragment_profile, container, false);
        initToolbar(profile);
        initComponents(profile);
        return profile;
    }

    private void initToolbar(View view) {
        Toolbar profileToolbar = view.findViewById(R.id.profile_toolbar);
        ((MainActivity) getActivity()).setSupportActionBar(profileToolbar);
        ((MainActivity) getActivity()).getSupportActionBar().setTitle(toolbarTitle);
    }

    private void initComponents(View view) {

        appBarLayout = view.findViewById(R.id.app_bar_layout);
        appBarLayout.addOnOffsetChangedListener(onOffsetChangedListener);

        collapsingToolbar = view.findViewById(R.id.collapsing_toolbar);
        coverPhoto = view.findViewById(R.id.cover_photo);
        displayPicture = view.findViewById(R.id.display_picture);

        email = view.findViewById(R.id.txt_p_email_value);
        username = view.findViewById(R.id.txt_p_username_value);

        email.setText(authUser.getEmail());
        username.setText(authUser.getUsername());

        btnUploadDp = view.findViewById(R.id.btn_upload);
        fabProfileEdit = view.findViewById(R.id.fab_profile_edit);

        Picasso.get().load(Tools.IMAGE_URI + userSession.getUser().getDisplayPicture()).into(displayPicture);

        btnUploadDp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (hasAllPermissions(allPermissions)) {
                    ProfileBottomSheet.newInstance().show(getChildFragmentManager(), "PROFILE BOTTOM SHEET");
                } else {
                    requestPermissions(allPermissions, REQUEST_PERMISSIONS_ALL);
                }
            }
        });

        fabProfileEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), UpdateProfileActivity.class));
            }
        });
    }

    private AppBarLayout.OnOffsetChangedListener onOffsetChangedListener = new AppBarLayout.OnOffsetChangedListener() {
        @Override
        public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
            // scaling the display picture with scroll
            int min_height = ViewCompat.getMinimumHeight(collapsingToolbar) * 2;
            float scale = (float) (min_height + verticalOffset) / min_height;
            displayPicture.setScaleX(scale >= 0 ? scale : 0);
            displayPicture.setScaleY(scale >= 0 ? scale : 0);

            if (Math.abs(verticalOffset) == appBarLayout.getTotalScrollRange()) { //toolbar is fully collapsed
                if (getArguments() != null) {
                    toolbarTitle = getArguments().getString(ARG_PARAM1);
                    ((MainActivity) getActivity()).getSupportActionBar().setTitle(toolbarTitle);
                }

            } else { //expanded
                ((MainActivity) getActivity()).getSupportActionBar().setTitle(" ");
            }
        }
    };

    private boolean hasAllPermissions(String... permissions) {
        if (permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(getActivity(), permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_PERMISSIONS_ALL) {
            if (grantResults.length > 0) {
                boolean cameraPermitted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                boolean storagePermitted = grantResults[1] == PackageManager.PERMISSION_GRANTED;

                if (cameraPermitted && storagePermitted) {
                    Log.i("PERMITTED", "ALL PERMISSIONS GRANTED");
                    ProfileBottomSheet.newInstance().show(getChildFragmentManager(), "PROFILE BOTTOM SHEET");

                } else {
                    Toast.makeText(getActivity(), "PERMISSIONS DENIED !", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }


    // update display picture
    public void updateDisplayPicture(String imagePath) {

        userDaoImpl.uploadDisplayPicture(imagePath);
        userDaoImpl.setUserActionsListener(new UserDaoImpl.UserActionsListener() {
            @Override
            public void onDpUploaded(User updatedUser) {
                Picasso.get().load(Uri.parse("file://" + imagePath)).into(displayPicture);
                Toast.makeText(getActivity(), "Display picture uploaded successfully", Toast.LENGTH_LONG).show();
                userSession.startSession(updatedUser, userSession.getAuthToken());
            }

            @Override
            public void onDetailsUpdated(User user) {

            }

            @Override
            public void onPasswordChanged() {

            }

            @Override
            public void onSignedOut() {

            }

            @Override
            public void onError(Error error) {

            }

            @Override
            public void onFetchedAllUsers(List<User> allUsers) {

            }
        });

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        authUser = new UserSession(getActivity()).getUser();

        userDaoImpl = new UserDaoImpl(getActivity());

        userSession = new UserSession(getActivity());

    }
}
