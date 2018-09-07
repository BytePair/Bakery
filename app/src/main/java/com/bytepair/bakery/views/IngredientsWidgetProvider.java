package com.bytepair.bakery.views;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RemoteViews;
import android.widget.TextView;

import com.bytepair.bakery.R;
import com.bytepair.bakery.models.Ingredient;
import com.bytepair.bakery.models.Recipe;
import com.google.gson.Gson;

import java.util.List;

/**
 * Implementation of App Widget functionality.
 */
public class IngredientsWidgetProvider extends AppWidgetProvider {

    public static final String WIDGET_RECIPE = "ingredients_widget_recipe";

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        SharedPreferences sharedPreferences = context.getSharedPreferences(
                WIDGET_RECIPE, 0);
        Recipe recipe = new Gson().fromJson(sharedPreferences.getString(WIDGET_RECIPE, null), Recipe.class);

        CharSequence widgetText = context.getString(R.string.appwidget_text);
        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.ingredients_widget);
        views.setTextViewText(R.id.ingredients_widget_title_text_view, widgetText);

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    private class IngredientArrayAdapter extends ArrayAdapter<Ingredient> {

        private List<Ingredient> mIngredients;

        IngredientArrayAdapter(Context context, List<Ingredient> ingredients) {
            super(context, 0, ingredients);
            mIngredients = ingredients;
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            Ingredient ingredient = mIngredients.get(position);
            // inflate view if it doesn't exist yet
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.ingredient_list_item, parent, false);
            }
            // set text in the view
            TextView ingredientTextView = convertView.findViewById(R.id.ingredient_name_view);
            TextView quantityTextView = convertView.findViewById(R.id.ingredient_quantity_view);
            ingredientTextView.setText(ingredient.getIngredient());
            quantityTextView.setText(String.valueOf(ingredient.getMeasure() + "  " + ingredient.getQuantity()));
            return convertView;
        }
    }
}

