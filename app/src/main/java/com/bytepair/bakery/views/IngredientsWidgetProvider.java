package com.bytepair.bakery.views;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.RemoteViews;

import com.bytepair.bakery.R;
import com.bytepair.bakery.models.Recipe;
import com.google.gson.Gson;

/**
 * Implementation of App Widget functionality.
 */
public class IngredientsWidgetProvider extends AppWidgetProvider {

    public static final String WIDGET_RECIPE = "ingredients_widget_recipe";

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        // Try to get recipe from shared preferences
        SharedPreferences sharedPreferences = context.getSharedPreferences(
                WIDGET_RECIPE, 0);
        Recipe recipe = new Gson().fromJson(sharedPreferences.getString(WIDGET_RECIPE, null), Recipe.class);

        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.ingredients_widget);

        // Launch the app when clicked
        Intent appIntent = new Intent(context, MainActivity.class);
        PendingIntent appPendingIntent = PendingIntent.getActivity(context, 0, appIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        views.setOnClickPendingIntent(R.id.ingredients_widget_relative_layout, appPendingIntent);

        // Otherwise show recipe title and ingredients list
        if (recipe != null) {
            // Set title to name of recipe
            views.setTextViewText(R.id.ingredients_widget_title_text_view, recipe.getName());

            // Set the RecipeWidgetService intent to act as the adapter for the grid view
            Intent intent = new Intent(context, RecipeWidgetService.class);
            views.setRemoteAdapter(R.id.ingredients_widget_grid_view, intent);

            // Set empty view to show when there are no ingredients
            views.setEmptyView(R.id.ingredients_widget_grid_view, R.id.empty_widget_text_view);
        }

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    /**
     * Calls updateAppWidget for every id passed
     * @param context
     * @param appWidgetManager
     * @param appWidgetIds
     */
    public static void updateAppWidgets(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
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
}

