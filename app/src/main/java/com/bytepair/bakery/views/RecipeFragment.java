package com.bytepair.bakery.views;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bytepair.bakery.R;
import com.bytepair.bakery.models.Recipe;
import com.bytepair.bakery.presenters.RecipePresenter;
import com.bytepair.bakery.views.interfaces.RecyclerViewInterface;

import static android.content.res.Configuration.ORIENTATION_LANDSCAPE;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class RecipeFragment extends Fragment implements RecyclerViewInterface {

    private static final String TAG = RecipeFragment.class.getSimpleName();

    private OnListFragmentInteractionListener mListener;
    private MyRecipeRecyclerViewAdapter mMyRecipeRecyclerViewAdapter;
    private RecipePresenter mRecipePresenter;
    private int mColumnCount;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public RecipeFragment() {
        mColumnCount = 1;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recipe_list, container, false);

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context mContext = view.getContext();
            mColumnCount = getNumberOfColumns();
            RecyclerView mRecyclerView = (RecyclerView) view;
            mRecyclerView.setLayoutManager(new GridLayoutManager(mContext, mColumnCount));
            mRecipePresenter = new RecipePresenter(this);
            mMyRecipeRecyclerViewAdapter = new MyRecipeRecyclerViewAdapter(mRecipePresenter.getRecipes(), mListener);
            mRecyclerView.setAdapter(mMyRecipeRecyclerViewAdapter);
            mRecipePresenter.initializeRecipes();
        }
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void loadDataSuccess() {
        Log.i(TAG, "load data success");
        mMyRecipeRecyclerViewAdapter.setRecipes(mRecipePresenter.getRecipes());
        mMyRecipeRecyclerViewAdapter.notifyDataSetChanged();
    }

    @Override
    public void loadDataFailure() {
        Log.i(TAG, "load data fail");
    }

    @Override
    public void loadDataInProgress() {
        Log.i(TAG, "load data in progress");
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnListFragmentInteractionListener {
        // TODO: Update argument type and name
        void onListFragmentInteraction(Recipe recipe);
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
