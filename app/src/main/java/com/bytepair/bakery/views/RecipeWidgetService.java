package com.bytepair.bakery.views;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.bytepair.bakery.R;
import com.bytepair.bakery.models.Ingredient;
import com.bytepair.bakery.models.Recipe;
import com.google.gson.Gson;

import java.util.List;

import static com.bytepair.bakery.views.IngredientsWidgetProvider.WIDGET_RECIPE;

public class RecipeWidgetService extends RemoteViewsService {

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new RecipeRemoteViewsFactory(this.getApplicationContext());
    }
}

class RecipeRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {

    private Context mContext;
    private List<Ingredient> mIngredients;

    public RecipeRemoteViewsFactory(Context context) {
        mContext = context;
    }

    @Override
    public void onCreate() {
    }

    @Override
    public void onDataSetChanged() {
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(
                WIDGET_RECIPE, 0);
        Recipe recipe = new Gson().fromJson(sharedPreferences.getString(WIDGET_RECIPE, null), Recipe.class);
        if (recipe != null) {
            mIngredients = recipe.getIngredients();
        }
    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        if (mIngredients == null) {
            return 0;
        }
        return mIngredients.size();
    }

    @Override
    public RemoteViews getViewAt(int position) {

        Ingredient ingredient = mIngredients.get(position);

        RemoteViews views = new RemoteViews(mContext.getPackageName(), R.layout.ingredient_list_item);
        views.setTextViewText(R.id.ingredient_name_view, ingredient.getIngredient());
        views.setTextViewText(R.id.ingredient_quantity_view, ingredient.getQuantity() + "  " + ingredient.getMeasure());

        return views;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }
}
