package com.raju.tripplanner;

import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.rule.ActivityTestRule;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static androidx.test.espresso.Espresso.closeSoftKeyboard;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.RootMatchers.withDecorView;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;

public class SendInvitationTest {

    @Rule
    public ActivityTestRule<MainActivity> testRule = new ActivityTestRule<>(MainActivity.class);

    @Before
    public void setupHomeFragment() {
        testRule.getActivity().getSupportFragmentManager().beginTransaction();
    }

    @Test
    public void testSendInvitation() {
        // verify that the trips recycler view is displayed
        onView(withId(R.id.my_trips_container)).check(matches(isDisplayed()));
        // click the first item in the recycler view
        onView(withId(R.id.my_trips_container))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
        // click the invite friends fab
        onView(withId(R.id.fab_invite_friends)).perform(click());
        // type username of the friend
        onView(withId(R.id.friends_autocomplete)).perform(typeText("raj"));
        // close the keyboard
        closeSoftKeyboard();
        // choose the first suggestion
        onView(withText("raj01")).inRoot(withDecorView(not(is(testRule.getActivity().getWindow().getDecorView()))))
                .perform(click());
        // verify that invitation sent toast is displayed
//        onView(withText("Invitation Sent"))
//                .inRoot(withDecorView(not(testRule.getActivity().getWindow().getDecorView()))).check(matches(isDisplayed()));
    }

}
