package com.m68476521.mymexico;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.m68476521.mymexico.data.TaskContract;
import com.m68476521.mymexico.data.TaskModelLoader;
import com.m68476521.mymexico.utilities.NetworkUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URL;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {
    private static final String TAG = MainActivity.class.getSimpleName();
    private static final int TASK_LOADER_ID = 0;
    private Cursor cursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //getSupportLoaderManager().initLoader(TASK_LOADER_ID, null, this);

        cursor = getContentResolver().query(TaskContract.TaskEntry.CONTENT_URI,
                null,
                null,
                null,
                TaskContract.TaskEntry.COLUMN_ID);
        if (cursor == null || cursor.getCount() == 0) {
            getApiData();
        }

        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            int descriptionIndex = cursor.getColumnIndex(TaskContract.TaskEntry.COLUMN_NAME);
            String description = cursor.getString(descriptionIndex);
            Log.d("MIKE", "swap cursorCD: " + description);
        }

        Bundle extras = getIntent().getExtras();
        if (extras != null && extras.containsKey("f_question")) {
            Log.d("MIKE get Intent extra ", extras.getString("f_question"));
        }

        Cursor cursor2 = getContentResolver().query(TaskContract.TaskEntry.CONTENT_URI_TRICKS,
                null,
                null,
                null,
                null);

        for (cursor2.moveToFirst(); !cursor2.isAfterLast(); cursor2.moveToNext()) {
            int descriptionIndex = cursor2.getColumnIndex(TaskContract.TaskEntry.COLUMN_FCM_QUESTION);
            String description = cursor2.getString(descriptionIndex);
            Log.d("MIKE", "swap cursorCD: " + description);
        }
    }

    private void getApiData() {
        URL searchUrl = NetworkUtils.buildUrl();
        new RetrieveInfoAPI().execute(searchUrl);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Log.d("MIKE ", "onCreateLoader");
        return new TaskModelLoader(this, 1, "");
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        Log.d("MIKE ", "onLoadFinished");
        cursor = data;
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    private class RetrieveInfoAPI extends AsyncTask<URL, Void, String> {
        JSONObject result = null;

        @Override
        protected String doInBackground(URL... urls) {

            Log.d("MIKE ", "doInBackground");
            try {
                NetworkUtils networkUtils = new NetworkUtils();
                result = networkUtils.getDataFromApi();

                JSONArray jsonArray = result.getJSONArray(TaskContract.TaskEntry.JSON_ROOT);

                if (jsonArray.length() > 0) {
                    for (int index = 0; index < jsonArray.length(); index++) {

                        JSONObject innerObject = jsonArray.getJSONObject(index);
                        String name = innerObject.getString(TaskContract.TaskEntry.COLUMN_NAME);
                        String id = innerObject.getString(TaskContract.TaskEntry.COLUMN_ID);
                        String description = innerObject.getString(TaskContract.TaskEntry.COLUMN_DESCRIPTION);
                        String image = innerObject.getString(TaskContract.TaskEntry.COLUMN_IMAGE);
                        String lastModified = innerObject.getString(TaskContract.TaskEntry.COLUMN_LAST_MODIFIED);
                        Log.d("MIKE:: ", name + id + description + image + lastModified);


                        ContentValues contentValues = new ContentValues();
                        contentValues.put(TaskContract.TaskEntry.COLUMN_ID, id);
                        contentValues.put(TaskContract.TaskEntry.COLUMN_NAME, name);
                        contentValues.put(TaskContract.TaskEntry.COLUMN_DESCRIPTION, description);
                        contentValues.put(TaskContract.TaskEntry.COLUMN_IMAGE, image);
                        contentValues.put(TaskContract.TaskEntry.COLUMN_LAST_MODIFIED, lastModified);

                        Uri uri = getContentResolver().insert(TaskContract.TaskEntry.CONTENT_URI, contentValues);
                    }
                }

            } catch (Exception e) {
                Log.d("MIKE EXCEPTION", e.toString());
            }
            return "";
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
        }
    }
}