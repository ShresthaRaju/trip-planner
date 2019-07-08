package com.raju.tripplanner;

import com.raju.tripplanner.DAO.AuthAPI;
import com.raju.tripplanner.DAO.TripAPI;
import com.raju.tripplanner.models.Destination;
import com.raju.tripplanner.models.Trip;
import com.raju.tripplanner.models.User;
import com.raju.tripplanner.utils.ApiResponse.SignInResponse;
import com.raju.tripplanner.utils.ApiResponse.TripResponse;
import com.raju.tripplanner.utils.ApiResponse.UserResponse;
import com.raju.tripplanner.utils.RetrofitClient;
import com.raju.tripplanner.utils.Tools;

import org.hamcrest.core.IsNull;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Response;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)

public class UnitTests {

    private AuthAPI authAPI;
    private TripAPI tripAPI;

    @Before
    public void setup() {
        authAPI = RetrofitClient.getInstance(Tools.BASE_URL).create(AuthAPI.class);
        tripAPI = RetrofitClient.getInstance(Tools.BASE_URL).create(TripAPI.class);
    }

    @Test
    public void testAuthToken_ShouldStoreReceivedToken() {
        Call<SignInResponse> testAuthTokenCall = authAPI.signIn(new User("raj@example.com", "passwords"));
        try {
            Response<SignInResponse> response = testAuthTokenCall.execute();
            Tools.AUTH_TOKEN = response.body().getAuthToken();
            assertThat(Tools.AUTH_TOKEN, IsNull.notNullValue());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testSignUp_ShouldRegisterANewUser() {
        String firstName = "test";
        String familyName = "name";
        String email = "unit@test.com";
        String username = "testname";
        String password = "testpassword";

        User newUser = new User(firstName, familyName, email, username, password);

        Call<UserResponse> signUpCall = authAPI.registerUser(newUser);

        try {
            Response<UserResponse> response = signUpCall.execute();
            assertEquals(email, response.body().getUser().getEmail());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testSignIn_ValidDetails_ShouldReturnAuthenticatedUser() {
        Call<SignInResponse> signInCall = authAPI.signIn(new User("sameer@example.com", "password"));

        try {
            Response<SignInResponse> response = signInCall.execute();
            assertNotNull(response.body().getUser());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testSignIn_InvalidDetails_ShouldFail() {
        Call<SignInResponse> signInCall = authAPI.signIn(new User("sameer@example.com", " "));

        try {
            Response<SignInResponse> response = signInCall.execute();
            assertNotNull(response.body().getUser());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testCreateTrip_ShouldAddANewTrip() {
        String placeName = "Pokhara Lakeside";
        String startDate = "2019-07-15";
        String endDate = "2019-07-22";
        double lat = 28.2099793;
        double lng = 83.955765;
        String photoUrl = "https://maps.googleapis.com/maps/api/place/photo?maxwidth=800&photoreference=CmRaAAAAqSbEHCpTOwkklhELghZdeAwnE2q__3pPEadO64A6kB9nnI2k7jYy3JGgLlu7utyNEVS4w2xGyFw0CIa_S8MLa_MbC6d_Rw4_GyaipoqE3SiSLExsvklnSTz0XGgbeYQTEhASibwXjHgaI3E0ZfLqockUGhTSl-FG8jSjTFydv_CT84qMuhodjw&key=AIzaSyBBp7x9kEmqRi-x0LkFzsewTzcnShvAbQE";

        Destination destination = new Destination(lat, lng, photoUrl);

        Call<SignInResponse> signInCall = authAPI.signIn(new User("raj@example.com", "passwords"));
        try {
            Response<SignInResponse> signInResponse = signInCall.execute();
            Trip trip = new Trip("Trip to " + placeName, startDate, endDate, destination, signInResponse.body().getUser().getId());
            Call<TripResponse> addTripCall = tripAPI.createTrip("Bearer " + signInResponse.body().getAuthToken(), trip);
            Response<TripResponse> tripResponse = addTripCall.execute();
            assertTrue(tripResponse.isSuccessful() && tripResponse.body().getTrip().getName().equals("Trip to " + placeName));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
