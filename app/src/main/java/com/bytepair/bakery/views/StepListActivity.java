package com.bytepair.bakery.views;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bytepair.bakery.R;
import com.bytepair.bakery.models.Ingredient;
import com.bytepair.bakery.models.Recipe;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import static com.bytepair.bakery.views.IngredientsWidgetProvider.WIDGET_RECIPE;
import static com.bytepair.bakery.views.StepDetailFragment.STEP_NUMBER_ARGUMENT;

/**
 * An activity representing a list of Steps. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link StepDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
public class StepListActivity extends AppCompatActivity {

    /**
     * The argument representing the recipe that this activity represents.
     */
    public static final String RECIPE_ARGUMENT = "recipe_argument";

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private boolean mTwoPane;

    /**
     * The recipe that is active for current step list
     */
    private Recipe mRecipe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step_list);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Show the Up button in the action bar.
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        if (findViewById(R.id.step_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/layout-w720dp-land).
            // If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;
        }

        // Get recipe if passed via intent
        if (getIntent() != null && getIntent().getExtras() != null && getIntent().getExtras().containsKey(RECIPE_ARGUMENT)) {
            mRecipe = new Gson().fromJson(getIntent().getExtras().getString(RECIPE_ARGUMENT), Recipe.class);
        }

        // if we have a recipe...
        if (mRecipe != null) {
            // set title...
            if (actionBar != null) {
                actionBar.setTitle(mRecipe.getName());
            }
            toolbar.setTitle(mRecipe.getName());

            // and show ingredients
            RecyclerView ingredientsRecyclerView = findViewById(R.id.ingredients_view);
            assert ingredientsRecyclerView != null;
            ingredientsRecyclerView.setAdapter(new IngredientRecyclerViewAdapter(mRecipe.getIngredients()));
            ingredientsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        }

        RecyclerView recyclerView = findViewById(R.id.step_list);
        assert recyclerView != null;
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        setupRecyclerView(recyclerView);

        final AlertDialog.Builder alertDialogBuilder = getWidgetAlertDialogBuilder();

        FloatingActionButton fab = findViewById(R.id.add_to_widget_fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialogBuilder.show();
            }
        });
    }

    /**
     * Alert dialog that notifies the user that the ingredients will be added to the widget
     *
     * Helpful tutorial for the alert builder found on droid mentor
     * http://droidmentor.com/how-do-i-display-an-alert-dialog-on-android/
     */
    private AlertDialog.Builder getWidgetAlertDialogBuilder() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle("  Add Ingredients");
        alertDialogBuilder.setMessage("Would you like to add the ingredients for " + mRecipe.getName() + " to the ingredients widget?");
        alertDialogBuilder.setCancelable(true);
        alertDialogBuilder.setIcon(R.drawable.ic_launcher);
        alertDialogBuilder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // Save recipe to shared preferences
                SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences(
                        WIDGET_RECIPE, 0);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString(WIDGET_RECIPE, new Gson().toJson(mRecipe, Recipe.class));
                editor.apply();
                // Then notify the widget that there is an update
                AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(getApplicationContext());
                int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(getApplicationContext(), IngredientsWidgetProvider.class));
                appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.ingredients_widget_grid_view);
                IngredientsWidgetProvider.updateAppWidgets(
                        getApplicationContext(),
                        appWidgetManager,
                        appWidgetIds);
                // Show toast to inform the user
                Snackbar snackbar = Snackbar
                        .make(findViewById(R.id.ingredients_view),
                                "Ingredients for " + mRecipe.getName() + " added to widget",
                                Snackbar.LENGTH_LONG);

                snackbar.show();
            }
        });
        alertDialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // do nothing
            }
        });
        return alertDialogBuilder;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            // This ID represents the Home or Up button. In the case of this
            // activity, the Up button is shown. Use NavUtils to allow users
            // to navigate up one level in the application structure. For
            // more details, see the Navigation pattern on Android Design:
            //
            // http://developer.android.com/design/patterns/navigation.html#up-vs-back
            //
            NavUtils.navigateUpFromSameTask(this);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        recyclerView.setAdapter(new SimpleItemRecyclerViewAdapter(this, mRecipe, mTwoPane));
    }

    public static class SimpleItemRecyclerViewAdapter
            extends RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.ViewHolder> {

        // keep track of all the possible views and selected step so we can highlight it
        private List<ViewHolder> mViews = new ArrayList<>();

        private final StepListActivity mParentActivity;
        private final Recipe mRecipe;
        private final boolean mTwoPane;
        private final View.OnClickListener mOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Integer stepPositoin = (Integer) view.getTag();
                if (mTwoPane) {
                    hidePlaceholderTextView();
                    highlightSelectedView(view);
                    Bundle arguments = new Bundle();
                    arguments.putString(RECIPE_ARGUMENT, new Gson().toJson(mRecipe));
                    arguments.putInt(STEP_NUMBER_ARGUMENT, stepPositoin);
                    StepDetailFragment fragment = new StepDetailFragment();
                    fragment.setArguments(arguments);
                    mParentActivity.getSupportFragmentManager().beginTransaction()
                            .replace(R.id.step_detail_container, fragment)
                            .commit();
                } else {
                    Context context = view.getContext();
                    Intent intent = new Intent(context, StepDetailActivity.class);
                    intent.putExtra(RECIPE_ARGUMENT, new Gson().toJson(mRecipe));
                    intent.putExtra(STEP_NUMBER_ARGUMENT, stepPositoin);

                    context.startActivity(intent);
                }
            }

            /**
             * Makes detail container visible and hides the placeholder text
             */
            private void hidePlaceholderTextView() {
                TextView textView = mParentActivity.findViewById(R.id.get_started_text_view);
                if (textView != null) {
                    textView.setVisibility(View.GONE);
                }
                FrameLayout frameLayout = mParentActivity.findViewById(R.id.step_detail_container);
                if (frameLayout != null) {
                    frameLayout.setVisibility(View.VISIBLE);
                }
            }
        };

        private void highlightSelectedView(View clickedView) {
            for (ViewHolder viewHolder : mViews) {
                if (viewHolder.itemView.equals(clickedView)) {
                    viewHolder.mCardView.setCardBackgroundColor(viewHolder.itemView.getResources().getColor(R.color.colorPrimaryLight));
                }
                else {
                    viewHolder.mCardView.setCardBackgroundColor(viewHolder.itemView.getResources().getColor(android.R.color.white));
                }
            }
        }

        SimpleItemRecyclerViewAdapter(StepListActivity parent, Recipe recipe, boolean twoPane) {
            mRecipe = recipe;
            mParentActivity = parent;
            mTwoPane = twoPane;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.step_list_content, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
            holder.mContentView.setText(mRecipe.getSteps().get(position).getDescription());

            holder.itemView.setTag(position);
            holder.itemView.setOnClickListener(mOnClickListener);

            mViews.add(holder);
        }

        @Override
        public int getItemCount() {
            return mRecipe.getSteps().size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            final CardView mCardView;
            final TextView mContentView;

            ViewHolder(View view) {
                super(view);
                mCardView = view.findViewById(R.id.step_card_view);
                mContentView = view.findViewById(R.id.content);
            }
        }
    }

    public class IngredientRecyclerViewAdapter extends RecyclerView.Adapter<IngredientRecyclerViewAdapter.ViewHolder> {

        private List<Ingredient> mIngredients;

        IngredientRecyclerViewAdapter(List<Ingredient> ingredients) {
            this.mIngredients = ingredients;
        }

        @NonNull
        @Override
        public IngredientRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            Context context = parent.getContext();
            LayoutInflater inflater = LayoutInflater.from(context);

            // Inflate the custom layout
            View ingredientsView = inflater.inflate(R.layout.ingredient_list_item, parent, false);

            // Return a new holder instance
            return new ViewHolder(ingredientsView);
        }

        @Override
        public void onBindViewHolder(@NonNull IngredientRecyclerViewAdapter.ViewHolder holder, int position) {
            // Get ingredient at that position
            Ingredient ingredient = mIngredients.get(position);

            // Set views
            holder.ingredientTextView.setText(ingredient.getIngredient());
            holder.quantityTextView.setText(String.valueOf(ingredient.getQuantity() + "  " + ingredient.getMeasure()));
        }

        @Override
        public int getItemCount() {
            return (mIngredients == null) ? 0 : mIngredients.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {

            public TextView ingredientTextView;
            public TextView quantityTextView;

            public ViewHolder(View itemView) {
                super(itemView);
                ingredientTextView = itemView.findViewById(R.id.ingredient_name_view);
                quantityTextView = itemView.findViewById(R.id.ingredient_quantity_view);
            }
        }
    }
}
