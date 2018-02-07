package com.m68476521.mymexico.data;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

/**
 * Class used to handles the loaders
 */

public class TaskModelLoader extends AsyncTaskLoader<Cursor> {
    private static final String TAG = TaskModelLoader.class.getSimpleName();
    private Cursor cursor;
    private final Context context;
    private final int queryId;
    private final String recipeValue;

    public TaskModelLoader(final Context context, int idQuery, String recipeValue) {
        super(context);
        this.context = context;
        queryId = idQuery;
        this.recipeValue = recipeValue;
    }

    @Override
    public Cursor loadInBackground() {
        try {
            switch (queryId) {
                case 1:
                    Uri uri = TaskContract.TaskEntry.CONTENT_URI;
                    uri = uri.buildUpon().appendPath(recipeValue).build();
                    cursor = context.getContentResolver().query(uri,
                            null,
                            null,
                            null,
                            null);
                    break;
                case 2:
                    cursor = context.getContentResolver().query(TaskContract.TaskEntry.CONTENT_URI_TRICKS,
                            null,
                            null,
                            null,
                            null);
                    break;
                default:
                    Log.d(TAG, " id query not found");
            }
            return cursor;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
