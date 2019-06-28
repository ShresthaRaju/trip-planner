package com.raju.tripplanner.fragments;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.raju.tripplanner.R;

public class PlacesFragment extends Fragment {

    private static final String ARG_PARAMS1 = "latitude";
    private static final String ARG_PARAMS2 = "longitude";

    public PlacesFragment() {
    }

    public static PlacesFragment newInstance(double latitude, double longitude) {
        PlacesFragment placesFragment = new PlacesFragment();
        Bundle args = new Bundle();
        args.putDouble(ARG_PARAMS1, latitude);
        args.putDouble(ARG_PARAMS2, longitude);
        placesFragment.setArguments(args);
        return placesFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_places, container, false);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            double lat = getArguments().getDouble(ARG_PARAMS1);
            double lng = getArguments().getDouble(ARG_PARAMS2);
            Toast.makeText(getActivity(), "From places" + lat + "," + lng, Toast.LENGTH_SHORT).show();
        }
    }

}
