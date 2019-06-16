package com.raju.tripplanner.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

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
import com.raju.tripplanner.DAO.PlacePhoto;
import com.raju.tripplanner.MainActivity;
import com.raju.tripplanner.R;
import com.raju.tripplanner.activities.CreateTripActivity;
import com.raju.tripplanner.authentication.SignInActivity;
import com.raju.tripplanner.models.Destination;
import com.raju.tripplanner.utils.ApiResponse.PhotoResponse;
import com.raju.tripplanner.utils.UserSession;

import java.util.Arrays;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

public class HomeFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private String toolbarTitle;
    public static final String MAP_API = BuildConfig.MapApi;
    private static final int AUTOCOMPLETE_REQUEST_CODE = 001;
    private PlacesClient placesClient;
    private ImageView imageView;
    private PlacePhoto placePhoto;

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
        initComponents(homeView);
        return homeView;
    }

    private void initComponents(View view) {
        FloatingActionButton createTrip = view.findViewById(R.id.fab_create_trip);
        createTrip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchPlaceAutocomplete();
            }
        });

        Button btnLogout = view.findViewById(R.id.btn_logout);
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new UserSession(getActivity()).endSession();
                Intent signInActivity = new Intent(getActivity(), SignInActivity.class);
                signInActivity.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(signInActivity);
                getActivity().finish();
            }
        });

        // Initialize Places.
        Places.initialize(getContext(), MAP_API);

        // Create a new Places client instance.
        placesClient = Places.createClient(getActivity());

        imageView = view.findViewById(R.id.imgView);

        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BASIC);
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(logging)
                .build();

        Retrofit retrofit = new Retrofit.Builder().baseUrl("https://maps.googleapis.com/maps/api/place/details/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();

        placePhoto = retrofit.create(PlacePhoto.class);
    }

    private void initToolbar(View view) {
        Toolbar homeToolbar = view.findViewById(R.id.home_toolbar);
        ((MainActivity) getActivity()).setSupportActionBar(homeToolbar);
        if (getArguments() != null) {
            toolbarTitle = getArguments().getString(ARG_PARAM1);
            ((MainActivity) getActivity()).getSupportActionBar().setTitle(toolbarTitle);
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
                String placeName = selectedPlace.getName();
                LatLng latLng = selectedPlace.getLatLng();
                double lat = latLng.latitude;
                double lng = latLng.longitude;

                Call<PhotoResponse> photoCall = placePhoto.fetchPhoto(placeId, "photo", MAP_API);
                photoCall.enqueue(new Callback<PhotoResponse>() {
                    @Override
                    public void onResponse(Call<PhotoResponse> call, Response<PhotoResponse> response) {
                        if (!response.isSuccessful()) {
                            Toast.makeText(getActivity(), response.code() + "", Toast.LENGTH_LONG).show();
                            return;
                        }
                        PhotoResponse photoResponse = response.body();
                        String photoReference = photoResponse.getResult().getPhotos().get(0).getPhoto_reference();
                        String photoUrl = "https://maps.googleapis.com/maps/api/destination/photo?maxwidth=800&photoreference=" + photoReference + "&key=" + MAP_API;
                        Log.i("PHOTO_URL", photoUrl);
                        Destination destination = new Destination(lat, lng, photoUrl);
                        Intent createTrip = new Intent(getActivity(), CreateTripActivity.class);
                        createTrip.putExtra("Place_Name", placeName);
                        createTrip.putExtra("Destination", destination);
                        startActivity(createTrip);
                    }

                    @Override
                    public void onFailure(Call<PhotoResponse> call, Throwable t) {
                        Toast.makeText(getActivity(), t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
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
}
