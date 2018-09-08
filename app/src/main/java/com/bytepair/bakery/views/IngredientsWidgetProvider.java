package com.bytepair.bakery.views;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
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

        // Try to get recipe from shared preferences
        SharedPreferences sharedPreferences = context.getSharedPreferences(
                WIDGET_RECIPE, 0);
        Recipe recipe = new Gson().fromJson(sharedPreferences.getString(WIDGET_RECIPE, null), Recipe.class);

        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.ingredients_widget);

        // If recipe is null, do not try to fill in ingredients list
        if (recipe == null) {
            showEmptyWidget(views);
            // Launch the app when clicked
            Intent appIntent = new Intent(context, MainActivity.class);
            PendingIntent appPendingIntent = PendingIntent.getActivity(context, 0, appIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            views.setOnClickPendingIntent(R.id.empty_widget_text_view, appPendingIntent);
        }
        // Otherwise show recipe title and ingredients list
        else {
            showPopulatedWidget(views);
            CharSequence widgetText = recipe.getName();
            views.setTextViewText(R.id.ingredients_widget_title_text_view, widgetText);

            // Set the RecipeWidgetService intent to act as the adapter for the grid view
            Intent intent = new Intent(context, RecipeWidgetService.class);
            views.setRemoteAdapter(R.id.ingredients_widget_grid_view, intent);
        }

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    /**
     * Show only title and grid views
     * @param remoteViews
     */
    private static void showPopulatedWidget(RemoteViews remoteViews) {
        remoteViews.setViewVisibility(R.id.ingredients_widget_title_text_view, View.VISIBLE);
        remoteViews.setViewVisibility(R.id.ingredients_widget_grid_view, View.VISIBLE);
        remoteViews.setViewVisibility(R.id.empty_widget_text_view, View.GONE);
    }

    /**
     * Show only instructional text view when recipe is not set
     * @param remoteViews
     */
    private static void showEmptyWidget(RemoteViews remoteViews) {
        remoteViews.setViewVisibility(R.id.empty_widget_text_view, View.VISIBLE);
        remoteViews.setViewVisibility(R.id.ingredients_widget_title_text_view, View.GONE);
        remoteViews.setViewVisibility(R.id.ingredients_widget_grid_view, View.GONE);
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

