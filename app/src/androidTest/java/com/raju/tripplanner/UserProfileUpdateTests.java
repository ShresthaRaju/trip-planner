package com.raju.tripplanner;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

import com.raju.tripplanner.activities.UpdateProfileActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.closeSoftKeyboard;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.action.ViewActions.swipeUp;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.RootMatchers.withDecorView;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.not;

@RunWith(AndroidJUnit4.class)
public class UserProfileUpdateTests {

    @Rule
    public ActivityTestRule<UpdateProfileActivity> testRule = new ActivityTestRule<>(UpdateProfileActivity.class);

    @Test
    public void testUpdateUserProfile() {
        String updateTo = "New test name";
        // type new first name in the first name field
        onView(withId(R.id.esp_first_name)).perform(replaceText(updateTo));
        // close the keyboard
        closeSoftKeyboard();
        // click update profile button
        onView(withId(R.id.btn_update_profile)).perform(click());
        // verify that the name has updated
        onView(withId(R.id.esp_first_name)).check(matches(withText(updateTo)));
    }

    @Test
    public void testChangePassword() {
        // scroll down to find the change password view
        onView(withId(R.id.profile_update_scroll)).perform(swipeUp());
        // type old password in the old password field
        onView(withId(R.id.esp_old_password)).perform(typeText("password"));
        // close the keyboard
        closeSoftKeyboard();
        // type new password in the new password field
        onView(withId(R.id.esp_new_password)).perform(typeText("newpassword"));
        // close the keyboard
        closeSoftKeyboard();
        // click change password button
        onView(withId(R.id.btn_change_password)).perform(click());
        // verify that password changed toast is displayed
        onView(withText("Password changed successfully..."))
                .inRoot(withDecorView(not(testRule.getActivity().getWindow().getDecorView()))).check(matches(isDisplayed()));
    }

}
