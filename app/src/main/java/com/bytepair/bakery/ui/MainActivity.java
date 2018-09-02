package com.bytepair.bakery.ui;

import android.content.res.Configuration;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.FrameLayout;

import com.bytepair.bakery.R;
import com.bytepair.bakery.ui.dummy.DummyContent;

import static android.content.res.Configuration.ORIENTATION_LANDSCAPE;

public class MainActivity extends AppCompatActivity implements RecipeFragment.OnListFragmentInteractionListener {

    private static final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Check that the activity is using the layout version with
        // the fragment_container FrameLayout
        FrameLayout frameLayout = findViewById(R.id.recipe_fragment_container);
        if (frameLayout != null) {

            // However, if we're being restored from a previous state,
            // remove existing views so we don't get overlapping fragments
            if (savedInstanceState != null) {
                frameLayout.removeAllViews();
            }

            // Create a new Fragment to be placed in the activity layout
            RecipeFragment recipeFragment = RecipeFragment.newInstance(getNumberOfColumns());

            // In case this activity was started with special instructions from an
            // Intent, pass the Intent's extras to the fragment as arguments
            recipeFragment.setArguments(getIntent().getExtras());

            // Add the fragment to the 'fragment_container' FrameLayout
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.recipe_fragment_container, recipeFragment).commit();
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public void onListFragmentInteraction(DummyContent.DummyItem item) {
        // TODO: Launch recipe when clicking
        Log.i(TAG, item.toString());
    }

    /**
     * Finds the number of columns of recipes to be displayed
     *
     * @return  3 for tablets in landscape and 1 for everything else
     */
    private int getNumberOfColumns() {
        if (getResources().getConfiguration().smallestScreenWidthDp >= 600
                && getResources().getConfiguration().orientation == ORIENTATION_LANDSCAPE) {
            return 3;
        }
        return 1;
    }
}
