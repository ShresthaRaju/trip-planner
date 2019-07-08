package com.raju.tripplanner;

import com.raju.tripplanner.DAO.AuthAPI;
import com.raju.tripplanner.models.User;
import com.raju.tripplanner.utils.ApiResponse.SignInResponse;
import com.raju.tripplanner.utils.RetrofitClient;
import com.raju.tripplanner.utils.Tools;

import org.hamcrest.core.IsNull;
import org.junit.Test;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Response;

import static org.junit.Assert.assertThat;

public class Tests {

    @Test
    public void testAuthToken() {
        AuthAPI authAPI = RetrofitClient.getInstance(Tools.BASE_URL).create(AuthAPI.class);
        Call<SignInResponse> signInCall = authAPI.signIn(new User("raj@example.com", "password"));

        try {
            Response<SignInResponse> response = signInCall.execute();
            Tools.AUTH_TOKEN = response.body().getAuthToken();
            assertThat(Tools.AUTH_TOKEN, IsNull.notNullValue());
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}
