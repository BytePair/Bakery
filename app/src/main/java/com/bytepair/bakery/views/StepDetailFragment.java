package com.bytepair.bakery.views;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bytepair.bakery.R;
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
    public static final String STEP_ARGUMENT = "step_argument";

    /**
     * The step this fragment is presenting.
     */
    private Step mStep;

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

        if (getArguments() != null && getArguments().containsKey(STEP_ARGUMENT)) {
            mStep = new Gson().fromJson(getArguments().getString(STEP_ARGUMENT), Step.class);

            Activity activity = this.getActivity();
            CollapsingToolbarLayout appBarLayout = null;
            if (activity != null) {
                appBarLayout = activity.findViewById(R.id.toolbar_layout);
            }
            if (appBarLayout != null) {
                appBarLayout.setTitle("Step #" + mStep.getId());
            }
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.step_detail, container, false);

        // TODO: fill in rest of detailed step view
        if (mStep != null) {
            ((TextView) rootView.findViewById(R.id.step_detail)).setText(mStep.getDescription());

            if (mStep.getVideoURL() != null && mStep.getVideoURL().length() > 0) {
                 bindPlayerView(rootView, mStep.getVideoURL());
            }
            else {
                hideVideoPlayer(rootView);
            }
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
}
