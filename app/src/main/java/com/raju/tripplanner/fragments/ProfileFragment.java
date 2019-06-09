package com.raju.tripplanner.fragments;

import android.Manifest;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.raju.tripplanner.MainActivity;
import com.raju.tripplanner.R;
import com.raju.tripplanner.bottomsheets.ProfileBottomSheet;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private Toolbar profileToolbar;
    private AppBarLayout appBarLayout;
    private CollapsingToolbarLayout collapsingToolbar;
    private CircleImageView displayPicture;
    private ImageView coverPhoto;
    private String toolbarTitle;
    private FloatingActionButton fabProfileEdit;

    private String[] allPermissions = {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
    private static final int REQUEST_PERMISSIONS_ALL = 986;

    //    required empty constructor
    public ProfileFragment() {

    }

    public static ProfileFragment newInstance(String toolbarTitle) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, toolbarTitle);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View profile = inflater.inflate(R.layout.fragment_profile, container, false);
        initComponents(profile);
        return profile;
    }

    private void initComponents(View view) {
        profileToolbar = view.findViewById(R.id.profile_toolbar);
        ((MainActivity) getActivity()).setSupportActionBar(profileToolbar);

        appBarLayout = view.findViewById(R.id.app_bar_layout);
        appBarLayout.addOnOffsetChangedListener(onOffsetChangedListener);

        collapsingToolbar = view.findViewById(R.id.collapsing_toolbar);
        coverPhoto = view.findViewById(R.id.cover_photo);
        displayPicture = view.findViewById(R.id.display_picture);


        fabProfileEdit = view.findViewById(R.id.fab_profile_edit);
        fabProfileEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (hasAllPermissions(allPermissions)) {
                    ProfileBottomSheet.newInstance().show(getChildFragmentManager(), "PROFILE BOTTOM SHEET");
                } else {
                    requestPermissions(allPermissions, REQUEST_PERMISSIONS_ALL);
                }
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
        Picasso.get().load(Uri.parse("file://" + imagePath)).into(coverPhoto);
    }
}
