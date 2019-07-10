package com.raju.tripplanner.DaoImpl;

import android.app.Activity;
import android.widget.Toast;

import com.raju.tripplanner.DAO.TripAPI;
import com.raju.tripplanner.models.Trip;
import com.raju.tripplanner.utils.ApiResponse.TripResponse;
import com.raju.tripplanner.utils.RetrofitClient;
import com.raju.tripplanner.utils.Tools;
import com.raju.tripplanner.utils.UserSession;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TripDaoImpl {
    private TripAPI tripAPI;
    private Activity activity;
    private TripActionsListener tripActionsListener;
    private UserSession userSession;

    public TripDaoImpl(Activity activity) {
        this.activity = activity;
        tripAPI = RetrofitClient.getInstance(Tools.BASE_URL).create(TripAPI.class);
        userSession = new UserSession(activity);
    }

    public Trip createTrip(Trip trip) {
        Trip createdTrip = null;
        Call<TripResponse> createTripCall = tripAPI.createTrip("Bearer " + userSession.getAuthToken(), trip);
        try {
            Response<TripResponse> tripResponse = createTripCall.execute();
            if (!tripResponse.isSuccessful()) {
                Toast.makeText(activity, "ERROR: " + tripResponse.code() + " " + tripResponse.message(), Toast.LENGTH_LONG).show();
                return createdTrip;
            }
            if (tripResponse.body().getTrip() != null) {
                createdTrip = tripResponse.body().getTrip();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return createdTrip;
    }

    public List<Trip> getMyTrips() {
        List<Trip> myTrips = new ArrayList<>();
        Call<TripResponse> userTripsCall = tripAPI.getUserTrips("Bearer " + userSession.getAuthToken());
        userTripsCall.enqueue(new Callback<TripResponse>() {
            @Override
            public void onResponse(Call<TripResponse> call, Response<TripResponse> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(activity, "ERROR: " + response.code() + " " + response.message(), Toast.LENGTH_LONG).show();
                    return;
                }
                tripActionsListener.onTripsReceived(response.body().getMyTrips());
            }

            @Override
            public void onFailure(Call<TripResponse> call, Throwable t) {
                Toast.makeText(activity, "FAILED: " + t.getLocalizedMessage(), Toast.LENGTH_LONG).show();
            }
        });
        return myTrips;
    }

    public void updateTrip(String tripId, Trip trip) {
        Call<Void> updateTripCall = tripAPI.updateTrip("Bearer " + userSession.getAuthToken(), tripId, trip);
        updateTripCall.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(activity, "ERROR: " + response.code() + " " + response.message(), Toast.LENGTH_LONG).show();
                    return;
                }

                tripActionsListener.onTripUpdated();
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(activity, "FAILED: " + t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void deleteTrip(String tripId) {
        Call<Void> deleteTripCall = tripAPI.deleteTrip("Bearer " + userSession.getAuthToken(), tripId);
        deleteTripCall.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(activity, "ERROR: " + response.code() + " " + response.message(), Toast.LENGTH_LONG).show();
                    return;
                }

                tripActionsListener.onTripDeleted();
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(activity, "FAILED: " + t.getLocalizedMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    public interface TripActionsListener {
        void onTripsReceived(List<Trip> myTrips);

        void onTripUpdated();

        void onTripDeleted();
    }

    public void setTripActionsListener(TripActionsListener tripActionsListener) {
        this.tripActionsListener = tripActionsListener;
    }
}
