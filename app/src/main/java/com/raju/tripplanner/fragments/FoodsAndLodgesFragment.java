package com.raju.tripplanner.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.raju.tripplanner.DAO.GooglePlacesAPI;
import com.raju.tripplanner.R;
import com.raju.tripplanner.adapters.LodgesAdapter;
import com.raju.tripplanner.models.MapResult.Place;
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
    private List<Place> nearbyLodges;
    private double lat, lng;
    private RecyclerView lodgesContainer;
    private Button btnShowLodges;
    private TextView tvNoResults;
    private ProgressBar lodgesProgress;
    private LodgesAdapter lodgesAdapter;

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

        View lodgesView = inflater.inflate(R.layout.fragment_foods_and_lodges, container, false);

        lodgesProgress = lodgesView.findViewById(R.id.foods_progress);
        tvNoResults = lodgesView.findViewById(R.id.tv_no_results);

        lodgesContainer = lodgesView.findViewById(R.id.lodges_container);
        lodgesContainer.setHasFixedSize(true);
        lodgesContainer.setLayoutManager(new LinearLayoutManager(getActivity()));

        btnShowLodges = lodgesView.findViewById(R.id.btn_show_lodges);
        btnShowLodges.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lodgesProgress.setVisibility(View.VISIBLE);
                fetchLodges(lat, lng);
            }
        });

        return lodgesView;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        googlePlacesAPI = RetrofitClient.getInstance("https://maps.googleapis.com/maps/api/place/nearbysearch/").create(GooglePlacesAPI.class);
        nearbyLodges = new ArrayList<>();

        if (getArguments() != null) {
            lat = getArguments().getDouble(ARG_PARAMS1);
            lng = getArguments().getDouble(ARG_PARAMS2);
        }
    }

    private void fetchLodges(double lat, double lng) {
        Call<NearbySearchResponse> fetchRestaurantsCall = googlePlacesAPI.fetchPlace(lat + "," + lng, 500, "lodging", HomeFragment.MAP_API);
        fetchRestaurantsCall.enqueue(new Callback<NearbySearchResponse>() {
            @Override
            public void onResponse(Call<NearbySearchResponse> call, Response<NearbySearchResponse> response) {
                if (!response.isSuccessful()) {
                    lodgesProgress.setVisibility(View.GONE);
                    Toast.makeText(getActivity(), "ERROR: " + response.code() + " " + response.message(), Toast.LENGTH_LONG).show();
                    return;
                }

                for (Place lodge : response.body().getResults()) {
                    if (lodge.getPhotos() != null) {
                        nearbyLodges.add(lodge);
                    }
                }
                lodgesProgress.setVisibility(View.GONE);
                if (nearbyLodges.isEmpty()) {
                    tvNoResults.setVisibility(View.VISIBLE);
                } else {
                    lodgesAdapter = new LodgesAdapter(getActivity(), nearbyLodges);
                    lodgesContainer.setAdapter(lodgesAdapter);
                }
            }

            @Override
            public void onFailure(Call<NearbySearchResponse> call, Throwable t) {
                lodgesProgress.setVisibility(View.GONE);
                Toast.makeText(getActivity(), "FAILED: " + t.getLocalizedMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}
