package com.m68476521.mymexico.data;

import android.app.IntentService;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;

import com.m68476521.mymexico.MexicoWidgetProvider;

/**
 * IntentService used to handle the provider update in the Widget
 */

public class TrickService extends IntentService {

    private static final String ACTION_TRICK_lIST =
            "com.m68476521.mike.myMexico.data.question";

    public TrickService() {
        super("TrickService");
    }

    /**
     * Starts this service to perform question action with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */

    private static String question = "";

    /**
     * Starts this service to perform UpdateListIngredientWidgets action with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    public static void startActionUpdateTrickWidgets(Context context, String newQuestion) {
        Intent intent = new Intent(context, TrickService.class);
        intent.setAction(ACTION_TRICK_lIST);
        question = newQuestion;
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_TRICK_lIST.equals(action)) {
                handleActionUpdateListWidgets();
            }
        }
    }

    private void handleActionCustomList() {
        //TODO: maybe add a new functionality
    }

    private void handleActionUpdateListWidgets() {
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(this, MexicoWidgetProvider.class));
        //Now update all widgets
        MexicoWidgetProvider.updateTricksWidgets(this, appWidgetManager, question, appWidgetIds);
    }
}
