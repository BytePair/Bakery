package com.bytepair.bakery;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.util.DisplayMetrics;

import com.bytepair.bakery.views.MainActivity;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition;
import static android.support.test.espresso.matcher.RootMatchers.isDialog;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withParent;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.junit.Assume.assumeFalse;

@RunWith(AndroidJUnit4.class)
public class StepListActivityPhoneTest {

    private static final String ADD_INGREDIENTS = "  Add Ingredients";

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    @Before
    public void launchStepListActivity() {
        // these tests are only to be ran on a phone
        assumeFalse(isScreenSw720dp());

        // click the first item in the recycler view
        onView(withId(R.id.recipe_recycler_view)).perform(actionOnItemAtPosition(0, click()));
    }

    /**
     * Click on FAB and verify user is presented with add to widget alert dialog
     */
    @Test
    public void fabButtonTest() {
        // verify FAB is displayed
        onView(withId(R.id.add_to_widget_fab)).check(matches(isDisplayed()));

        // click on FAB
        onView(withId(R.id.add_to_widget_fab)).perform(click());

        // verify dialog is shown
        onView(withText(ADD_INGREDIENTS)).inRoot(isDialog()).check(matches(isDisplayed()));
    }

    /**
     * Click on a step and verify that the step details activity is launched
     */
    @Test
    public void launchStepTest() {
        // verify first step is shown
        onView(withText("Recipe Introduction")).check(matches(isDisplayed()));

        // click the first step
        onView(withText("Recipe Introduction")).perform(click());

        // verify step details are displayed (check toolbar changed)
        onView(withId(R.id.toolbar)).check(matches(isDisplayed()));
        onView(withText("Introduction")).check(matches(withParent(withId(R.id.toolbar))));

        // verify step details are displayed (check video is shown)
        onView(withId(R.id.step_video_view)).check(matches(isDisplayed()));
    }

    private boolean isScreenSw720dp() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        mActivityTestRule.getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        float widthDp = displayMetrics.widthPixels / displayMetrics.density;
        float heightDp = displayMetrics.heightPixels / displayMetrics.density;
        float screenSw = Math.min(widthDp, heightDp);
        return screenSw >= 720;
    }
}
