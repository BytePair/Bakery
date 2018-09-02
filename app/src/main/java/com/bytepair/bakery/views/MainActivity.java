package com.bytepair.bakery.views;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.FrameLayout;

import com.bytepair.bakery.R;
import com.bytepair.bakery.models.Recipe;

public class MainActivity extends AppCompatActivity implements RecipeFragment.OnListFragmentInteractionListener {

    private static final String TAG = MainActivity.class.getSimpleName();
    private RecipeFragment mRecipeFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Check that the activity is using the layout version with
        // the fragment_container FrameLayout
        FrameLayout frameLayout = findViewById(R.id.recipe_fragment_container);
        if (frameLayout != null) {

            // However, if we're being restored from a previous state,
            // return early to avoid making duplicate fragments
            if (savedInstanceState != null) {
                return;
            }

            // Create a new Fragment to be placed in the activity layout
            mRecipeFragment = new RecipeFragment();

            // In case this activity was started with special instructions from an
            // Intent, pass the Intent's extras to the fragment as arguments
            mRecipeFragment.setArguments(getIntent().getExtras());

            // Add the fragment to the 'fragment_container' FrameLayout
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.recipe_fragment_container, mRecipeFragment).commit();
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public void onListFragmentInteraction(Recipe recipe) {
        // TODO: Launch recipe when clicking
        Log.i(TAG, recipe.toString());
    }
}
