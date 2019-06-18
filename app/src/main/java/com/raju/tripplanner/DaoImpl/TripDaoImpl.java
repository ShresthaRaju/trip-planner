package com.raju.tripplanner.DaoImpl;

import android.app.Activity;
import android.content.Intent;
import android.widget.Toast;

import com.raju.tripplanner.ApiCalls.TripAPI;
import com.raju.tripplanner.activities.ViewTripActivity;
import com.raju.tripplanner.models.Trip;
import com.raju.tripplanner.utils.ApiResponse.TripResponse;
import com.raju.tripplanner.utils.RetrofitClient;
import com.raju.tripplanner.utils.UserSession;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TripDaoImpl {
    private TripAPI tripAPI;
    private Activity activity;
    private GetTripsListener getTripsListener;

    public TripDaoImpl(Activity activity) {
        this.activity = activity;
        tripAPI = RetrofitClient.getInstance().create(TripAPI.class);
    }

    public void createTrip(Trip trip) {
        Call<TripResponse> createTripCall = tripAPI.createTrip("Bearer " + new UserSession(activity).getAuthToken(), trip);
        createTripCall.enqueue(new Callback<TripResponse>() {
            @Override
            public void onResponse(Call<TripResponse> call, Response<TripResponse> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(activity, "ERROR: " + response.code() + " " + response.message(), Toast.LENGTH_LONG).show();
                    return;
                }
                TripResponse tripResponse = response.body();
                Intent viewTrip = new Intent(activity, ViewTripActivity.class);
                viewTrip.putExtra("TRIP", tripResponse.getTrip());
                activity.startActivity(viewTrip);
                activity.finish();
            }

            @Override
            public void onFailure(Call<TripResponse> call, Throwable t) {
                Toast.makeText(activity, "FAILED: " + t.getLocalizedMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    public List<Trip> getMyTrips() {
        List<Trip> myTrips = new ArrayList<>();
        Call<TripResponse> userTripsCall = tripAPI.getUserTrips("Bearer " + new UserSession(activity).getAuthToken());
        userTripsCall.enqueue(new Callback<TripResponse>() {
            @Override
            public void onResponse(Call<TripResponse> call, Response<TripResponse> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(activity, "ERROR: " + response.code() + " " + response.message(), Toast.LENGTH_LONG).show();
                    return;
                }
                getTripsListener.onTripsReceived(response.body().getMyTrips());
            }

            @Override
            public void onFailure(Call<TripResponse> call, Throwable t) {
                Toast.makeText(activity, "FAILED: " + t.getLocalizedMessage(), Toast.LENGTH_LONG).show();
            }
        });
        return myTrips;
    }

    public void deleteTrip(String tripId) {
        Call<Void> deleteTripCall = tripAPI.deleteTrip("Bearer " + new UserSession(activity).getAuthToken(), tripId);
        deleteTripCall.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(activity, "ERROR: " + response.code() + " " + response.message(), Toast.LENGTH_LONG).show();
                    return;
                }

                Toast.makeText(activity, "Trip deleted successfully", Toast.LENGTH_LONG).show();
                activity.finish();
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(activity, "FAILED: " + t.getLocalizedMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    public interface GetTripsListener {
        void onTripsReceived(List<Trip> myTrips);
    }

    public void setGetTripsListener(GetTripsListener getTripsListener) {
        this.getTripsListener = getTripsListener;
    }
}
