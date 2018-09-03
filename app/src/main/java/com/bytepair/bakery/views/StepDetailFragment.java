package com.bytepair.bakery.views;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bytepair.bakery.R;
import com.bytepair.bakery.models.Recipe;
import com.bytepair.bakery.models.Step;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.google.gson.Gson;

import static com.bytepair.bakery.views.StepListActivity.RECIPE_ARGUMENT;

/**
 * A fragment representing a single Step detail screen.
 * This fragment is either contained in a {@link StepListActivity}
 * in two-pane mode (on tablets) or a {@link StepDetailActivity}
 * on handsets.
 */
public class StepDetailFragment extends Fragment {
    /**
     * The fragment argument representing the recipe step that this fragment represents.
     */
    public static final String STEP_NUMBER_ARGUMENT = "step_number_argument";

    /**
     * Recipe and step number for this fragment
     */
    private Recipe mRecipe;
    private Integer mStepNumber;

    /**
     * Used to play the step video if available
     */
    private SimpleExoPlayer mExoPlayer;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public StepDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null && getArguments().containsKey(STEP_NUMBER_ARGUMENT) && getArguments().containsKey(RECIPE_ARGUMENT)) {

            mRecipe = new Gson().fromJson(getArguments().getString(RECIPE_ARGUMENT), Recipe.class);
            mStepNumber = getArguments().getInt(STEP_NUMBER_ARGUMENT);

            Activity activity = this.getActivity();
            CollapsingToolbarLayout appBarLayout = null;
            if (activity != null) {
                appBarLayout = activity.findViewById(R.id.toolbar_layout);
            }
            if (appBarLayout != null) {
                if (mStepNumber == null || mStepNumber < 1) {
                    appBarLayout.setTitle("Introduction");
                }
                else {
                    appBarLayout.setTitle("Step #" + mStepNumber);
                }
            }
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.step_detail, container, false);

        // TODO: fill in rest of detailed step view
        if (mStepNumber != null && mRecipe != null) {
            // Ensure description exists before setting it
            Step step = mRecipe.getSteps().get(mStepNumber);
            String description = step.getDescription();
            if (description != null && description.length() > 3) {
                // if it is not step 1, remove the step number from description before showing
                if (mStepNumber > 0) {
                    description = description.substring(3);
                }
                ((TextView) rootView.findViewById(R.id.step_detail)).setText(description);
            }

            if (step.getVideoURL() != null && step.getVideoURL().length() > 0) {
                 bindPlayerView(rootView, step.getVideoURL());
            }
            else {
                hideVideoPlayer(rootView);
            }

            // Set up the FAB buttons to navigate to previous and next steps
            setUpFABs();
        }

        return rootView;
    }

    private void hideVideoPlayer(View rootView) {
        PlayerView playerView = rootView.findViewById(R.id.step_video_view);
        playerView.setVisibility(View.GONE);
    }

    private void bindPlayerView(View view, String videoUrl) {
        // Get video uri
        Uri mp4VideoUri = Uri.parse(videoUrl);
        // Create simple exo player and bind to the view
        if (mExoPlayer == null) {
            mExoPlayer = createExoPlayer();
        }
        // Produces DataSource instances through which media data is loaded.
        DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(view.getContext(),
                Util.getUserAgent(view.getContext(), "Bakery"), null);
        // This is the MediaSource representing the media to be played.
        MediaSource videoSource = new ExtractorMediaSource.Factory(dataSourceFactory)
                .createMediaSource(mp4VideoUri);
        // Prepare the player with the source.
        mExoPlayer.prepare(videoSource);
        // Find the player view and attach the player
        PlayerView playerView = view.findViewById(R.id.step_video_view);
        playerView.setPlayer(mExoPlayer);
    }

    private SimpleExoPlayer createExoPlayer() {
        // 1. Create a default TrackSelector
        Handler mainHandler = new Handler();
        BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
        TrackSelection.Factory videoTrackSelectionFactory = new AdaptiveTrackSelection.Factory(bandwidthMeter);
        DefaultTrackSelector trackSelector = new DefaultTrackSelector(videoTrackSelectionFactory);

        // 2. Create and return the player
        return ExoPlayerFactory.newSimpleInstance(getContext(), trackSelector);
    }

    @Override
    public void onDestroyView() {
        if (mExoPlayer != null) {
            mExoPlayer.release();
            mExoPlayer = null;
        }
        super.onDestroyView();
    }



    /**
     * Tries to set up the previous and next step buttons
     *  - both are hidden for tablet view
     *  - previous is hidden if on first step
     *  - next is hidden if on last step
     */
    private void setUpFABs() {
        // set up button to go to next step
        FloatingActionButton backFab = getActivity().findViewById(R.id.back_fab);
        if (backFab != null) {
            backFab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Snackbar.make(view, "Back FAB", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
            });
        }

        // set up button to go to next step
        FloatingActionButton forwardFab = getActivity().findViewById(R.id.forward_fab);
        if (forwardFab != null) {
            forwardFab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Snackbar.make(view, "Forward FAB", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
            });
        }
    }

}
