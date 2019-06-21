package com.raju.tripplanner.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.raju.tripplanner.BuildConfig;
import com.raju.tripplanner.DAO.PlacePhotoAPI;
import com.raju.tripplanner.DaoImpl.TripDaoImpl;
import com.raju.tripplanner.MainActivity;
import com.raju.tripplanner.R;
import com.raju.tripplanner.activities.CreateTripActivity;
import com.raju.tripplanner.adapters.MyTripsAdapter;
import com.raju.tripplanner.authentication.SignInActivity;
import com.raju.tripplanner.models.Destination;
import com.raju.tripplanner.models.Trip;
import com.raju.tripplanner.utils.ApiResponse.PhotoResponse;
import com.raju.tripplanner.utils.UserSession;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

public class HomeFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private static final String ARG_PARAM1 = "param1";
    private String toolbarTitle;
    public static final String MAP_API = BuildConfig.MapApi;
    private static final int AUTOCOMPLETE_REQUEST_CODE = 001;
    private PlacesClient placesClient;
    private PlacePhotoAPI placePhotoAPI;
    private FloatingActionButton createTrip;
    private RecyclerView myTripsContainer;
    private TextView tvNoTrip;
    private MyTripsAdapter myTripsAdapter;
    private TripDaoImpl tripDaoImpl;
    private SwipeRefreshLayout swipeRefreshHome;
    private List<Trip> userTrips;

    public HomeFragment() {
    }

    public static HomeFragment newInstance(String toolbarTitle) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, toolbarTitle);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View homeView = inflater.inflate(R.layout.fragment_home, container, false);

        initToolbar(homeView);

        swipeRefreshHome = homeView.findViewById(R.id.swipe_refresh_home);
        swipeRefreshHome.setOnRefreshListener(this);
        swipeRefreshHome.setColorSchemeColors(getResources().getColor(R.color.teal_500));

        createTrip = homeView.findViewById(R.id.fab_create_trip);
        createTrip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchPlaceAutocomplete();
            }
        });

        tvNoTrip = homeView.findViewById(R.id.tv_no_trip);

        myTripsContainer = homeView.findViewById(R.id.my_trips_container);
        myTripsContainer.setHasFixedSize(true);
        myTripsContainer.setLayoutManager(new LinearLayoutManager(getActivity()));

        myTripsAdapter = new MyTripsAdapter(getActivity(), new ArrayList<Trip>());
        myTripsContainer.setAdapter(myTripsAdapter);

        return homeView;
    }

    private void initToolbar(View view) {
        Toolbar homeToolbar = view.findViewById(R.id.home_toolbar);
        ((MainActivity) getActivity()).setSupportActionBar(homeToolbar);
        if (getArguments() != null) {
            toolbarTitle = getArguments().getString(ARG_PARAM1);
            ((MainActivity) getActivity()).getSupportActionBar().setTitle(toolbarTitle);
        }
    }

    private void initComponents() {

        setHasOptionsMenu(true);

        tripDaoImpl = new TripDaoImpl(getActivity());

        userTrips = new ArrayList<>();

        // Initialize Places.
        Places.initialize(getContext(), MAP_API);

        // Create a new Places client instance.
        placesClient = Places.createClient(getActivity());

        // retrofit client

        Retrofit retrofit = new Retrofit.Builder().baseUrl("https://maps.googleapis.com/maps/api/place/details/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        placePhotoAPI = retrofit.create(PlacePhotoAPI.class);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu_profile, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_sign_out:
                new UserSession(getActivity()).endSession();
                Intent signInActivity = new Intent(getActivity(), SignInActivity.class);
                signInActivity.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(signInActivity);
                getActivity().finish();
                return true;

            default:
                return false;
        }
    }

    private void launchPlaceAutocomplete() {
        if (!Places.isInitialized()) {
            Places.initialize(getContext(), MAP_API);
        }

        // Set the fields to specify which types of place data to return.
        List<Place.Field> fields = Arrays.asList(Place.Field.ID, Place.Field.LAT_LNG, Place.Field.NAME);

        // Start the autocomplete intent.
        Intent intent = new Autocomplete.IntentBuilder(
                AutocompleteActivityMode.OVERLAY, fields)
                .build(getActivity());
        startActivityForResult(intent, AUTOCOMPLETE_REQUEST_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Place selectedPlace = Autocomplete.getPlaceFromIntent(data);
                String placeId = selectedPlace.getId();
                final String placeName = selectedPlace.getName();
                LatLng latLng = selectedPlace.getLatLng();
                final double lat = latLng.latitude;
                final double lng = latLng.longitude;

                Call<PhotoResponse> photoCall = placePhotoAPI.fetchPhoto(placeId, "photo", MAP_API);
                photoCall.enqueue(new Callback<PhotoResponse>() {
                    @Override
                    public void onResponse(Call<PhotoResponse> call, Response<PhotoResponse> response) {
                        if (!response.isSuccessful()) {
                            Toast.makeText(getActivity(), response.code() + "", Toast.LENGTH_LONG).show();
                            return;
                        }
                        PhotoResponse photoResponse = response.body();
                        String photoReference = photoResponse.getPlacePhotoResult().getPhotos().get(0).getPhoto_reference();
                        String photoUrl = "https://maps.googleapis.com/maps/api/place/photo?maxwidth=800&photoreference=" + photoReference + "&key=" + MAP_API;

                        // create new destination
                        Destination destination = new Destination(lat, lng, photoUrl);

                        // pass the destination to create trip intent and start
                        Intent createTrip = new Intent(getActivity(), CreateTripActivity.class);
                        createTrip.putExtra("Place_Name", placeName);
                        createTrip.putExtra("Destination", destination);
                        startActivity(createTrip);
                    }

                    @Override
                    public void onFailure(Call<PhotoResponse> call, Throwable t) {
                        Toast.makeText(getActivity(), t.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                    }
                });

            } else if (resultCode == AutocompleteActivity.RESULT_ERROR) {
                Status status = Autocomplete.getStatusFromIntent(data);
                Toast.makeText(getActivity(), status.getStatusMessage(), Toast.LENGTH_SHORT).show();
            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(getActivity(), "Cancelled !", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initComponents();
    }

    private void fetchUserTrips() {
        swipeRefreshHome.setRefreshing(true);
        tripDaoImpl.getMyTrips();
        tripDaoImpl.setTripActionsListener(new TripDaoImpl.TripActionsListener() {
            @Override
            public void onTripsReceived(List<Trip> myTrips) {
                if (myTrips.isEmpty()) {
                    swipeRefreshHome.setRefreshing(false);
                    tvNoTrip.setVisibility(View.VISIBLE);
                } else {
                    swipeRefreshHome.setRefreshing(false);
                    tvNoTrip.setVisibility(View.GONE);
                    myTripsAdapter.updateData(myTrips);
                }
            }

            @Override
            public void onTripCreated(Trip trip) {

            }

            @Override
            public void onTripUpdated() {

            }

            @Override
            public void onTripDeleted() {

            }
        });
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Toast.makeText(getActivity(), "on activity created", Toast.LENGTH_SHORT).show();
        fetchUserTrips();
    }

    @Override
    public void onRefresh() {
        fetchUserTrips();
    }
}