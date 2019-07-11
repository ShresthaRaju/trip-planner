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

        User user = new User("user1@example.com", "password");

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

        User user = new User("user2@example.com", "password");

        SignInResponse signInResponse = authDaoImpl.signIn(user);
        assertNotNull(signInResponse.getUser());

    }

    //
    @Test
    public void testSignIn_InvalidDetails_ShouldFail() {
        User user = new User("user1@example.com", "passwordssssss");

        SignInResponse signInResponse = authDaoImpl.signIn(user);
        assertNotNull(signInResponse);
    }
}
