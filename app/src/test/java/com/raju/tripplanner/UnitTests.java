package com.raju.tripplanner;

import com.raju.tripplanner.DaoImpl.AuthDaoImpl;
import com.raju.tripplanner.models.User;
import com.raju.tripplanner.utils.ApiResponse.SignInResponse;
import com.raju.tripplanner.utils.Tools;

import org.hamcrest.core.IsNull;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import static junit.framework.Assert.assertNotNull;
import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)

public class UnitTests {

    private AuthDaoImpl authDaoImpl;

    @Before
    public void setup() {
        authDaoImpl = new AuthDaoImpl();
    }

    @Test
    public void testAuthToken() {

        User user = new User("raj@example.com", "password");

        SignInResponse signInResponse = authDaoImpl.signIn(user);
        Tools.AUTH_TOKEN = signInResponse.getAuthToken();
        assertThat(Tools.AUTH_TOKEN, IsNull.notNullValue());

    }

    @Test
    public void testSignUp_ValidDetails_ShouldRegisterANewUser() {
        String firstName = "test";
        String familyName = "name";
        String email = "unit@test.com";
        String username = "UnitTest";
        String password = "TestPassword";

        User newUser = new User(firstName, familyName, email, username, password);
        boolean signedUp = authDaoImpl.signUp(newUser);
        assertTrue(signedUp);

    }

    @Test
    public void testSignUp_InvalidDetails_ShouldNotRegister() {
        String firstName = " ";
        String familyName = "demo";
        String email = "demo@test.com";
        String username = "demo";
        String password = "demo";

        User newUser = new User(firstName, familyName, email, username, password);
        boolean signedUp = authDaoImpl.signUp(newUser);
        assertFalse(signedUp);

    }

    @Test
    public void testSignIn_ValidDetails_ShouldReturnAuthenticatedUser() {

        User user = new User("raj@example.com", "password");

        SignInResponse signInResponse = authDaoImpl.signIn(user);
        assertNotNull(signInResponse.getUser());

    }

    //
    @Test
    public void testSignIn_InvalidDetails_ShouldFail() {
        User user = new User("raj@example.com", "passwords");

        SignInResponse signInResponse = authDaoImpl.signIn(user);
        assertNotNull(signInResponse);
    }

//    @Test
//    public void testCreateTrip_ShouldAddANewTrip() {
//        String placeName = "Pokhara Lakeside";
//        String startDate = "2019-07-15";
//        String endDate = "2019-07-22";
//        double lat = 28.2099793;
//        double lng = 83.955765;
//        String photoUrl = "https://maps.googleapis.com/maps/api/place/photo?maxwidth=800&photoreference=CmRaAAAAqSbEHCpTOwkklhELghZdeAwnE2q__3pPEadO64A6kB9nnI2k7jYy3JGgLlu7utyNEVS4w2xGyFw0CIa_S8MLa_MbC6d_Rw4_GyaipoqE3SiSLExsvklnSTz0XGgbeYQTEhASibwXjHgaI3E0ZfLqockUGhTSl-FG8jSjTFydv_CT84qMuhodjw&key=AIzaSyBBp7x9kEmqRi-x0LkFzsewTzcnShvAbQE";
//
//        Destination destination = new Destination(lat, lng, photoUrl);
//
//        Call<SignInResponse> signInCall = authAPI.signIn(new User("raj@example.com", "passwords"));
//        try {
//            Response<SignInResponse> signInResponse = signInCall.execute();
//            Trip trip = new Trip("Trip to " + placeName, startDate, endDate, destination, signInResponse.body().getUser().getId());
//            Call<TripResponse> addTripCall = tripAPI.createTrip("Bearer " + signInResponse.body().getAuthToken(), trip);
//            Response<TripResponse> tripResponse = addTripCall.execute();
//            assertTrue(tripResponse.isSuccessful() && tripResponse.body().getTrip().getName().equals("Trip to " + placeName));
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }

}
