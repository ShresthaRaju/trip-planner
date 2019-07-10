package com.raju.tripplanner;

import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.closeSoftKeyboard;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.RootMatchers.isDialog;
import static androidx.test.espresso.matcher.RootMatchers.withDecorView;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.not;

@RunWith(AndroidJUnit4.class)
public class TripUpdateDeleteTests {

    @Rule
    public ActivityTestRule<MainActivity> testRule = new ActivityTestRule<>(MainActivity.class);

    @Before
    public void setupHomeFragment() {
        testRule.getActivity().getSupportFragmentManager().beginTransaction();
    }

    @Test
    public void testUpdateTrip() {
        // verify that the trips recycler view is displayed
        onView(withId(R.id.my_trips_container)).check(matches(isDisplayed()));
        // click the first item in the recycler view
        onView(withId(R.id.my_trips_container))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
        // click the edit trip menu item
        onView(withId(R.id.action_edit)).perform(click());
        // append some text in edit text
        onView(withId(R.id.esp_trip_title)).perform(typeText(", Nepal"));
        // close the keyboard
        closeSoftKeyboard();
        // click the update trip button
        onView(withId(R.id.btn_update_trip)).perform(click());
        // verify that trip update toast is displayed
        onView(withText("Trip updated successfully"))
                .inRoot(withDecorView(not(testRule.getActivity().getWindow().getDecorView()))).check(matches(isDisplayed()));
    }

    @Test
    public void testDeleteTrip() {
        // verify if recycler view is displayed
        onView(withId(R.id.my_trips_container)).check(matches(isDisplayed()));
        // click the first item in the recycler view
        onView(withId(R.id.my_trips_container))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
        // click the delete trip menu item
        onView(withId(R.id.action_delete)).perform(click());
        // verify that delete confirmation dialog is displayed and "SURE" option is clicked
        onView(withText("SURE")).inRoot(isDialog()).check(matches(isDisplayed())).perform(click());
        // verify that trip deleted toast is displayed
        onView(withText("Trip deleted successfully"))
                .inRoot(withDecorView(not(testRule.getActivity().getWindow().getDecorView()))).check(matches(isDisplayed()));
    }

}
