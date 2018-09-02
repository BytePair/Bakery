package com.bytepair.bakery.views;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bytepair.bakery.R;
import com.bytepair.bakery.models.Step;
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
                appBarLayout.setTitle(mStep.getDescription());
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
        }

        return rootView;
    }
}
