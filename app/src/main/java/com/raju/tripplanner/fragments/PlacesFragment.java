package com.raju.tripplanner.fragments;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.raju.tripplanner.DAO.GooglePlacesAPI;
import com.raju.tripplanner.R;
import com.raju.tripplanner.adapters.PlacesAdapter;
import com.raju.tripplanner.models.MapResult.Place;
import com.raju.tripplanner.utils.ApiResponse.NearbySearchResponse;
import com.raju.tripplanner.utils.RetrofitClient;
import com.raju.tripplanner.utils.Tools;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PlacesFragment extends Fragment {

    private static final String ARG_PARAMS1 = "latitude";
    private static final String ARG_PARAMS2 = "longitude";
    private HashMap<String, String> placeTypesMap;
    private AutoCompleteTextView placeTypeAutocomplete;
    private double lat, lng;
    private ProgressBar placeProgress;
    private GooglePlacesAPI googlePlacesAPI;
    private List<Place> placeResults;
    private TextView tvNoPlaces;
    private RecyclerView placesContainer;
    private PlacesAdapter placesAdapter;

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
        View placeView = inflater.inflate(R.layout.fragment_places, container, false);

        placeProgress = placeView.findViewById(R.id.place_progress);
        tvNoPlaces = placeView.findViewById(R.id.tv_no_places);
        placeTypeAutocomplete = placeView.findViewById(R.id.place_type_autocomplete);
        String placeTypesArray[] = placeTypesMap.keySet().toArray(new String[placeTypesMap.size()]);
        ArrayAdapter<String> placeTypesAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, placeTypesArray);
        placeTypeAutocomplete.setAdapter(placeTypesAdapter);

        placeTypeAutocomplete.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                placeProgress.setVisibility(View.VISIBLE);
                fetchNearbyPlace(lat, lng, placeTypesMap.get(parent.getItemAtPosition(position)));
            }
        });

        placesContainer = placeView.findViewById(R.id.places_container);
        placesContainer.setHasFixedSize(true);
        placesContainer.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));

        return placeView;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            lat = getArguments().getDouble(ARG_PARAMS1);
            lng = getArguments().getDouble(ARG_PARAMS2);
        }

        googlePlacesAPI = RetrofitClient.getInstance(Tools.NEARBY_PLACE_API).create(GooglePlacesAPI.class);
        placeResults = new ArrayList<>();

        placeTypesMap = new HashMap<>();
        placeTypesMap.put("Airport", "airport");
        placeTypesMap.put("Amusement Park", "amusement_park");
        placeTypesMap.put("Art Gallery", "art_gallery");
        placeTypesMap.put("Bar", "bar");
        placeTypesMap.put("Bus Station", "bus_station");
        placeTypesMap.put("Cafe", "cafe");
        placeTypesMap.put("Campground", "campground");
        placeTypesMap.put("Car Rental", "car_rental");
        placeTypesMap.put("Clothing Store", "clothing_store");
        placeTypesMap.put("Hospital", "hospital");
        placeTypesMap.put("Laundry", "laundry");
        placeTypesMap.put("Movie Theater", "movie_theater");
        placeTypesMap.put("Museum", "museum");
        placeTypesMap.put("Night Club", "night_club");
        placeTypesMap.put("Park", "park");
        placeTypesMap.put("Pharmacy", "pharmacy");
        placeTypesMap.put("Restaurant", "restaurant");
        placeTypesMap.put("Spa", "spa");
        placeTypesMap.put("Taxi Stand", "taxi_stand");
        placeTypesMap.put("Zoo", "zoo");
    }

    private void fetchNearbyPlace(double lat, double lng, String placeType) {
        Call<NearbySearchResponse> fetchNearbyPlaceCall = googlePlacesAPI.fetchNearbyPlaces(lat + "," + lng, 1000, placeType, HomeFragment.MAP_API);
        fetchNearbyPlaceCall.enqueue(new Callback<NearbySearchResponse>() {
            @Override
            public void onResponse(Call<NearbySearchResponse> call, Response<NearbySearchResponse> response) {
                if (!response.isSuccessful()) {
                    placeProgress.setVisibility(View.GONE);
                    Toast.makeText(getActivity(), "ERROR: " + response.code() + " " + response.message(), Toast.LENGTH_LONG).show();
                    return;
                }

                placeResults.clear();

                for (Place lodge : response.body().getResults()) {
                    if (lodge.getPhotos() != null) {
                        placeResults.add(lodge);
                    }
                }
                placeProgress.setVisibility(View.GONE);
                if (placeResults.isEmpty()) {
                    tvNoPlaces.setVisibility(View.VISIBLE);
                } else {
                    tvNoPlaces.setVisibility(View.GONE);
                    placesAdapter = new PlacesAdapter(getActivity(), placeResults);
                    placesContainer.setAdapter(placesAdapter);
                }
            }

            @Override
            public void onFailure(Call<NearbySearchResponse> call, Throwable t) {
                placeProgress.setVisibility(View.GONE);
                Toast.makeText(getActivity(), "FAILED: " + t.getLocalizedMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

}
