package com.raju.tripplanner.DaoImpl;

import android.app.Activity;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.raju.tripplanner.DAO.UserAPI;
import com.raju.tripplanner.models.User;
import com.raju.tripplanner.utils.APIError;
import com.raju.tripplanner.utils.ApiResponse.AllUsersResponse;
import com.raju.tripplanner.utils.ApiResponse.UserResponse;
import com.raju.tripplanner.utils.Error;
import com.raju.tripplanner.utils.RetrofitClient;
import com.raju.tripplanner.utils.Tools;
import com.raju.tripplanner.utils.UserSession;

import java.io.File;
import java.io.IOException;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserDaoImpl {

    private Activity activity;
    private UserAPI userAPI;
    private UserActionsListener userActionsListener;
    private UserSession userSession;
    private Gson gson;
    private APIError apiError;

    public UserDaoImpl(Activity activity) {
        this.activity = activity;
        userAPI = RetrofitClient.getInstance(Tools.BASE_URL).create(UserAPI.class);
        userSession = new UserSession(activity);
        gson = new GsonBuilder().create();
        apiError = new APIError();
    }

    public void uploadDisplayPicture(String imagePath) {

        File imageFile = new File(imagePath);
        RequestBody imageBody = RequestBody.create(MediaType.parse("image/*"), imageFile);
        MultipartBody.Part displayPicture = MultipartBody.Part.createFormData("display_picture", imageFile.getName(), imageBody);
//        RequestBody userId = RequestBody.create(MediaType.parse("text/plain"), userSession.getUser().getId());

        Call<UserResponse> uploadDpCall = userAPI.uploadDisplayPicture("Bearer " + userSession.getAuthToken(), displayPicture);
        uploadDpCall.enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(activity, "ERROR: " + response.code() + " " + response.message(), Toast.LENGTH_LONG).show();
                    return;
                }

                userActionsListener.onDpUploaded(response.body().getUser());
            }

            @Override
            public void onFailure(Call<UserResponse> call, Throwable t) {
                Toast.makeText(activity, "FAILED: " + t.getLocalizedMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    public void updateDetails(String firstName, String familyName, String email, String username) {

        Call<UserResponse> updateDetailsCall = userAPI.updateDetails("Bearer " + userSession.getAuthToken(), firstName,
                familyName, email, username);
        updateDetailsCall.enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                if (!response.isSuccessful()) {
                    try {
                        apiError = gson.fromJson(response.errorBody().string(), APIError.class);
                        userActionsListener.onError(apiError.getError());
                    } catch (IOException e) {
                        Log.e("IOException", e.getMessage());
                    }
                    return;
                }

                userActionsListener.onDetailsUpdated(response.body().getUser());
            }

            @Override
            public void onFailure(Call<UserResponse> call, Throwable t) {
                Toast.makeText(activity, "FAILED: " + t.getLocalizedMessage(), Toast.LENGTH_LONG).show();
            }
        });

    }

    public void changePassword(String oldPassword, String newPassword) {
        Call<Void> changePasswordCall = userAPI.changePassword("Bearer " + userSession.getAuthToken(), oldPassword,
                newPassword);

        changePasswordCall.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (!response.isSuccessful()) {
                    try {
                        apiError = gson.fromJson(response.errorBody().string(), APIError.class);
                        userActionsListener.onError(apiError.getError());
                    } catch (IOException e) {
                        Log.e("IOException", e.getMessage());
                    }
                    return;
                }

                userActionsListener.onPasswordChanged();
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {

            }
        });
    }

    public void getAllUsers() {
        Call<AllUsersResponse> allUsersCall = userAPI.getAllUsers("Bearer " + userSession.getAuthToken());
        allUsersCall.enqueue(new Callback<AllUsersResponse>() {
            @Override
            public void onResponse(Call<AllUsersResponse> call, Response<AllUsersResponse> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(activity, "ERROR: " + response.code() + " " + response.message(), Toast.LENGTH_LONG).show();
                    return;
                }

                userActionsListener.onFetchedAllUsers(response.body().getUsers());
            }

            @Override
            public void onFailure(Call<AllUsersResponse> call, Throwable t) {
                Toast.makeText(activity, "FAILED: " + t.getLocalizedMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    public void signOut() {
        Call<Void> signOutCall = userAPI.signOut("Bearer " + userSession.getAuthToken());
        signOutCall.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(activity, "ERROR: " + response.message(), Toast.LENGTH_LONG).show();
                    return;
                }

                userActionsListener.onSignedOut();
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(activity, "FAILED: " + t.getLocalizedMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    public interface UserActionsListener {
        void onDpUploaded(User updatedUser);

        void onDetailsUpdated(User updatedUser);

        void onPasswordChanged();

        void onSignedOut();

        void onError(Error error);

        void onFetchedAllUsers(List<User> allUsers);
    }

    public void setUserActionsListener(UserActionsListener userActionsListener) {
        this.userActionsListener = userActionsListener;
    }
}
