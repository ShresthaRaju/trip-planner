package com.raju.tripplanner.DaoImpl;

import android.app.Activity;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.raju.tripplanner.DAO.UserAPI;
import com.raju.tripplanner.models.User;
import com.raju.tripplanner.utils.APIError;
import com.raju.tripplanner.utils.ApiResponse.UserResponse;
import com.raju.tripplanner.utils.Error;
import com.raju.tripplanner.utils.RetrofitClient;
import com.raju.tripplanner.utils.UserSession;

import java.io.File;
import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserDaoImpl {

    private Activity activity;
    private UserAPI userAPI;
    private UserProfileListener userProfileListener;
    private UserSession userSession;
    private Gson gson;
    private APIError apiError;

    public UserDaoImpl(Activity activity) {
        this.activity = activity;
        userAPI = RetrofitClient.getInstance().create(UserAPI.class);
        userSession = new UserSession(activity);
        gson = new GsonBuilder().create();
        apiError = new APIError();
    }

    public void uploadDisplayPicture(String imagePath) {

        File imageFile = new File(imagePath);
        RequestBody imageBody = RequestBody.create(MediaType.parse("image/*"), imageFile);
        MultipartBody.Part displayPicture = MultipartBody.Part.createFormData("display_picture", imageFile.getName(), imageBody);
        RequestBody userId = RequestBody.create(MediaType.parse("text/plain"), userSession.getUser().getId());

        Call<UserResponse> uploadDpCall = userAPI.uploadDisplayPicture("Bearer " + userSession.getAuthToken(), displayPicture, userId);
        uploadDpCall.enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(activity, "ERROR: " + response.code() + " " + response.message(), Toast.LENGTH_LONG).show();
                    return;
                }

                userProfileListener.onDpUploaded(response.body().getUser());
            }

            @Override
            public void onFailure(Call<UserResponse> call, Throwable t) {
                Toast.makeText(activity, "FAILED: " + t.getLocalizedMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    public void updateDetails(String firstName, String familyName) {

        Call<UserResponse> updateDetailsCall = userAPI.updateDetails("Bearer " + userSession.getAuthToken(), firstName,
                familyName, userSession.getUser().getId());
        updateDetailsCall.enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                if (!response.isSuccessful()) {
                    try {
                        apiError = gson.fromJson(response.errorBody().string(), APIError.class);
                        userProfileListener.onError(apiError.getError());
                    } catch (IOException e) {
                        Log.e("IOException", e.getMessage());
                    }
                    return;
                }

                userProfileListener.onDetailsUpdated(response.body().getUser());
            }

            @Override
            public void onFailure(Call<UserResponse> call, Throwable t) {
                Toast.makeText(activity, "FAILED: " + t.getLocalizedMessage(), Toast.LENGTH_LONG).show();
            }
        });

    }

    public void changePassword(String oldPassword, String newPassword) {
        Call<Void> changePasswordCall = userAPI.changePassword("Bearer " + userSession.getAuthToken(), oldPassword,
                newPassword, userSession.getUser().getId());

        changePasswordCall.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (!response.isSuccessful()) {
                    try {
                        apiError = gson.fromJson(response.errorBody().string(), APIError.class);
                        userProfileListener.onError(apiError.getError());
                    } catch (IOException e) {
                        Log.e("IOException", e.getMessage());
                    }
                    return;
                }

                userProfileListener.onPasswordChanged();
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {

            }
        });
    }

    public interface UserProfileListener {
        void onDpUploaded(User updatedUser);

        void onDetailsUpdated(User updatedUser);

        void onPasswordChanged();

        void onError(Error error);
    }

    public void setUserProfileListener(UserProfileListener userProfileListener) {
        this.userProfileListener = userProfileListener;
    }
}
