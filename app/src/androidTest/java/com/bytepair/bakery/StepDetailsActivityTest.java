package com.bytepair.bakery;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.util.DisplayMetrics;

import com.bytepair.bakery.views.MainActivity;

import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withParent;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.junit.Assume.assumeFalse;

@RunWith(AndroidJUnit4.class)
public class StepDetailsActivityTest {

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    @Before
    public void launchStepDetailsActivity() {
        // click the first item in the recycler view
        onView(withId(R.id.recipe_recycler_view)).perform(actionOnItemAtPosition(0, click()));

        // click the first step to launch step details activity
        onView(withText("Recipe Introduction")).perform(click());

        // check toolbar to verify we are on the right starting point
        onView(withId(R.id.toolbar)).check(matches(isDisplayed()));
        onView(withText("Introduction")).check(matches(withParent(withId(R.id.toolbar))));
    }

    /**
     * Click on forward FAB to navigate to next step
     */
    @Test
    public void fabButtonTest() {
        // does not exist on tablet
        assumeFalse(isScreenSw720dp());

        // verify FAB is displayed
        onView(withId(R.id.forward_fab)).check(matches(isDisplayed()));

        // click on FAB
        onView(withId(R.id.forward_fab)).perform(click());

        // verify toolbar changes
        onView(withId(R.id.toolbar)).check(matches(isDisplayed()));
        onView(withText("Step #1")).check(matches(withParent(withId(R.id.toolbar))));

        // verify text changes
        onView(withId(R.id.step_detail)).check(matches(withText(Matchers.startsWith("Preheat the oven to 350"))));
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
