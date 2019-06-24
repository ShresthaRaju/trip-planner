package com.raju.tripplanner.DaoImpl;

import android.app.Activity;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.raju.tripplanner.DAO.UserAPI;
import com.raju.tripplanner.utils.RetrofitClient;
import com.raju.tripplanner.utils.UserSession;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserDaoImpl {

    private Activity activity;
    private UserAPI userAPI;

    public UserDaoImpl(Activity activity) {
        this.activity = activity;
        userAPI = RetrofitClient.getInstance().create(UserAPI.class);
    }

    public void uploadDisplayPicture(String imagePath) {

        File imageFile = new File(imagePath);
        Log.d("imagep", imagePath);
        RequestBody requestBody = RequestBody.create(MediaType.parse("image/*"), imageFile);
        MultipartBody.Part displayPicture = MultipartBody.Part.createFormData("display_picture", imageFile.getName(), requestBody);

        Call<String> uploadDpCall = userAPI.uploadDisplayPicture("Bearer " + new UserSession(activity).getAuthToken(), displayPicture);
        uploadDpCall.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(activity, "ERROR: " + response.code() + " " + response.message(), Toast.LENGTH_LONG).show();
                    Log.e("err", new Gson().toJson(response.errorBody().toString()));
                    return;
                }

                Toast.makeText(activity, "result: " + response.body(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Toast.makeText(activity, "FAILED: " + t.getLocalizedMessage(), Toast.LENGTH_LONG).show();
            }
        });


    }
}
