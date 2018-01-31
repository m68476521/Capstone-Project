package com.m68476521.mymexico.utilities;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * This class will handling request from the API
 */

public class NetworkUtils {
    //TODO: add the api URL into URL
    private static final String URL = "";

    public void testConnection() {
        Log.d("MIKE exception ", "MIKE");
        try {
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url(URL)
                    .build();
            Response responses = null;
            String jsonData = "";
            try {
                responses = client.newCall(request).execute();
                jsonData = responses.body().string();
            } catch (IOException e) {
                e.printStackTrace();
                Log.d("MIKE EXCEPTION@#", e.toString());
            }

            Log.d("MIKE exception ", jsonData);
            JSONObject Jobject = new JSONObject(jsonData);
            JSONArray Jarray = Jobject.getJSONArray("employees");

            for (int i = 0; i < Jarray.length(); i++) {
                JSONObject object = Jarray.getJSONObject(i);
            }


        } catch (JSONException e) {
            Log.d("MIKE exception ", e.toString());
        }
    }
}


