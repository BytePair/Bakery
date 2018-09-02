package com.bytepair.bakery.views;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.view.MenuItem;

import com.bytepair.bakery.R;

import com.bytepair.bakery.models.Recipe;
import com.bytepair.bakery.models.Step;
import com.bytepair.bakery.views.dummy.DummyContent;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

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
        toolbar.setTitle(getTitle());

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
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

        RecyclerView recyclerView = findViewById(R.id.step_list);
        assert recyclerView != null;
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        setupRecyclerView(recyclerView);
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
        recyclerView.setAdapter(new SimpleItemRecyclerViewAdapter(this, mRecipe.getSteps(), mTwoPane));
    }

    public static class SimpleItemRecyclerViewAdapter
            extends RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.ViewHolder> {

        // keep track of all the possible views and selected step so we can highlight it
        private List<ViewHolder> mViews = new ArrayList<>();

        private final StepListActivity mParentActivity;
        private final List<Step> mSteps;
        private final boolean mTwoPane;
        private final View.OnClickListener mOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Step step = (Step) view.getTag();
                if (mTwoPane) {
                    highlightSelectedView(view);
                    Bundle arguments = new Bundle();
                    arguments.putString(StepDetailFragment.STEP_ARGUMENT, new Gson().toJson(step));
                    StepDetailFragment fragment = new StepDetailFragment();
                    fragment.setArguments(arguments);
                    mParentActivity.getSupportFragmentManager().beginTransaction()
                            .replace(R.id.step_detail_container, fragment)
                            .commit();
                } else {
                    Context context = view.getContext();
                    Intent intent = new Intent(context, StepDetailActivity.class);
                    intent.putExtra(StepDetailFragment.STEP_ARGUMENT, new Gson().toJson(step));

                    context.startActivity(intent);
                }
            }
        };

        private void highlightSelectedView(View clickedView) {
            for (ViewHolder viewHolder : mViews) {
                Log.d("color", viewHolder.toString());
                if (viewHolder.itemView.equals(clickedView)) {
                    viewHolder.itemView.setBackgroundColor(viewHolder.itemView.getResources().getColor(R.color.colorAccent));
                }
                else {
                    viewHolder.itemView.setBackgroundColor(viewHolder.itemView.getResources().getColor(android.R.color.white));
                }
            }
        }

        SimpleItemRecyclerViewAdapter(StepListActivity parent,
                                      List<Step> steps,
                                      boolean twoPane) {
            mSteps = steps;
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
            holder.mContentView.setText(mSteps.get(position).getDescription());

            holder.itemView.setTag(mSteps.get(position));
            holder.itemView.setOnClickListener(mOnClickListener);

            mViews.add(holder);
        }

        @Override
        public int getItemCount() {
            return mSteps.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            final TextView mContentView;

            ViewHolder(View view) {
                super(view);
                mContentView = view.findViewById(R.id.content);
            }
        }
    }
}
