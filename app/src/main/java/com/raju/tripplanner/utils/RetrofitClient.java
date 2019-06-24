package com.raju.tripplanner.utils;

import android.util.Log;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {

    /*android emulator does not allow localhost
      that is why localhost is replaced with 10.0.2.2*/

    //    private static String BASE_URL = "http:10.0.2.2:7000/api/";
//    private static String BASE_URL = "http:172.26.0.72:7000/api/";
    private static String BASE_URL = "http:192.168.0.104:7000/api/";
    private static Retrofit retrofit;

    public static Retrofit getInstance() {

        HttpLoggingInterceptor logging = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
            @Override
            public void log(String message) {
                Log.d("okkkkkk", message);
            }
        });
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(logging)
                .build();

        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(client)
                    .build();
        }

        return retrofit;
    }
}
