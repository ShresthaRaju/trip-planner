package com.raju.tripplanner.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.raju.tripplanner.fragments.FoodsAndLodgesFragment;
import com.raju.tripplanner.fragments.PlacesFragment;
import com.raju.tripplanner.fragments.WeatherFragment;

public class TripOptionsAdapter extends FragmentPagerAdapter {

    private double latitude, longitude;

    public TripOptionsAdapter(@NonNull FragmentManager fm, double latitude, double longitude) {
        super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        this.latitude = latitude;
        this.longitude = longitude;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return FoodsAndLodgesFragment.newInstance(latitude,longitude);

            case 1:
                return WeatherFragment.newInstance(latitude,longitude);

            case 2:
                return PlacesFragment.newInstance(latitude,longitude);

            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return 3;
    }
}
