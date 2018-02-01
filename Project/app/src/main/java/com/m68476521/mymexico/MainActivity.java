package com.m68476521.mymexico;


import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.m68476521.mymexico.data.TaskContract;
import com.m68476521.mymexico.utilities.NetworkUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URL;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getApiData();
    }

    private void getApiData() {
        URL searchUrl = NetworkUtils.buildUrl();
        new RetrieveInfoAPI().execute(searchUrl);
    }

    private class RetrieveInfoAPI extends AsyncTask<URL, Void, String> {

        JSONObject result = null;

        @Override
        protected String doInBackground(URL... urls) {
            try {
                NetworkUtils networkUtils = new NetworkUtils();
                result = networkUtils.getDataFromApi();

                JSONArray jsonArray = result.getJSONArray(TaskContract.TaskEntry.JSON_ROOT);

                if (jsonArray.length() > 0) {
                    for (int jIndex = 0; jIndex < jsonArray.length(); jIndex++) {

                        JSONObject innerObject = jsonArray.getJSONObject(jIndex);
                        String name = innerObject.getString(TaskContract.TaskEntry.COLUMN_NAME);
                        String id = innerObject.getString(TaskContract.TaskEntry.COLUMN_ID);
                        String description = innerObject.getString(TaskContract.TaskEntry.COLUMN_DESCRIPTION);
                        String image = innerObject.getString(TaskContract.TaskEntry.COLUMN_IMAGE);
                        String lastModified = innerObject.getString(TaskContract.TaskEntry.COLUMN_LAST_MODIFIED);
                        Log.d("MIKE:: ", name + id + description + image + lastModified);
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
