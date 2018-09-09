package com.bytepair.bakery;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.bytepair.bakery.views.MainActivity;

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

@RunWith(AndroidJUnit4.class)
public class MainActivityTest {

    private static final String RECIPE_NAME = "Nutella Pie";

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    /**
     * Click on first recipe in the list and verify that it opens
     * StepListActivity with the correct recipe
     */
    @Test
    public void clickRecipeOpensStepListActivity() {

        // click the first item in the recycler view
        onView(withId(R.id.recipe_recycler_view)).perform(actionOnItemAtPosition(0, click()));

        // verify the step list activity launched and shows correct recipe in toolbar
        onView(withId(R.id.toolbar)).check(matches(isDisplayed()));
        onView(withText(RECIPE_NAME)).check(matches(withParent(withId(R.id.toolbar))));
    }
}
