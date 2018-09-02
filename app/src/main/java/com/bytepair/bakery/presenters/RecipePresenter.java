package com.bytepair.bakery.presenters;

import android.support.annotation.NonNull;

import com.bytepair.bakery.models.Recipe;
import com.bytepair.bakery.ui.interfaces.RecyclerViewInterface;
import com.bytepair.bakery.utils.services.RecipeService;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RecipePresenter {

    private List<Recipe> mRecipes;
    private RecyclerViewInterface mRecyclerViewInterface;

    public RecipePresenter(RecyclerViewInterface recyclerViewInterface) {
        this.mRecyclerViewInterface = recyclerViewInterface;
        this.mRecipes = new ArrayList<>();
    }

    public List<Recipe> getRecipes() {
        return mRecipes;
    }

    public void initializeRecipes() {
        mRecyclerViewInterface.loadDataInProgress();
        mRecipes = new ArrayList<>();
        loadRecipes(RecipeService.getAPI().getRecipes());
    }

    private void loadRecipes(Call<List<Recipe>> recipeResults) {
        recipeResults.enqueue(new Callback<List<Recipe>>() {
            @Override
            public void onResponse(@NonNull Call<List<Recipe>> call, @NonNull Response<List<Recipe>> response) {
                if (response.isSuccessful()) {
                    mRecipes.addAll(Objects.requireNonNull(response.body()));
                    mRecyclerViewInterface.loadDataSuccess();
                } else {
                    onFailure(call, new Throwable("Fail response code: " +  response.code()));
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Recipe>> call, @NonNull Throwable t) {
                t.printStackTrace();
                mRecyclerViewInterface.loadDataFailure();
            }
        });
    }
}