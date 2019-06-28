package com.raju.tripplanner.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.raju.tripplanner.DAO.GooglePlacesAPI;
import com.raju.tripplanner.R;
import com.raju.tripplanner.models.MapResult.Restaurant;
import com.raju.tripplanner.utils.ApiResponse.NearbySearchResponse;
import com.raju.tripplanner.utils.RetrofitClient;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FoodsAndLodgesFragment extends Fragment {

    private static final String ARG_PARAMS1 = "latitude";
    private static final String ARG_PARAMS2 = "longitude";
    private GooglePlacesAPI googlePlacesAPI;
    private List<Restaurant> nearbyRestaurants;

    public FoodsAndLodgesFragment() {

    }

    public static FoodsAndLodgesFragment newInstance(double latitude, double longitude) {
        FoodsAndLodgesFragment foodsAndLodgesFragment = new FoodsAndLodgesFragment();
        Bundle args = new Bundle();
        args.putDouble(ARG_PARAMS1, latitude);
        args.putDouble(ARG_PARAMS2, longitude);
        foodsAndLodgesFragment.setArguments(args);
        return foodsAndLodgesFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_foods_and_lodges, container, false);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        googlePlacesAPI = RetrofitClient.getInstance("https://maps.googleapis.com/maps/api/place/nearbysearch/").create(GooglePlacesAPI.class);
        nearbyRestaurants = new ArrayList<>();

        if (getArguments() != null) {
            double lat = getArguments().getDouble(ARG_PARAMS1);
            double lng = getArguments().getDouble(ARG_PARAMS2);
            Toast.makeText(getActivity(), "From foods" + lat + "," + lng, Toast.LENGTH_SHORT).show();
            fetchLodges(lat, lng);
        }


    }

    private void fetchLodges(double lat, double lng) {
        Call<NearbySearchResponse> fetchLodgesCall = googlePlacesAPI.fetchLodges(lat + "," + lng, 500, "restaurant", HomeFragment.MAP_API);
        fetchLodgesCall.enqueue(new Callback<NearbySearchResponse>() {
            @Override
            public void onResponse(Call<NearbySearchResponse> call, Response<NearbySearchResponse> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(getActivity(), "ERROR: " + response.code() + " " + response.message(), Toast.LENGTH_LONG).show();
                    return;
                }

                for (Restaurant res : response.body().getResults()) {
                    if (res.getPhotos() != null) {
                        nearbyRestaurants.add(res);
                    }
                }
                int index = 0;
                for (Restaurant restaurant : nearbyRestaurants) {
                    Log.i("restaurant", index + "=>" + restaurant.getName() + ", " + restaurant.getRating() + ", " +
                            restaurant.getPhotos().get(0).getPhoto_reference() + "\n");
                    index++;
                }

            }

            @Override
            public void onFailure(Call<NearbySearchResponse> call, Throwable t) {
                Toast.makeText(getActivity(), "FAILED: " + t.getLocalizedMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}
