package com.m68476521.mymexico.utilities;

import android.net.Uri;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * This class will handling request from the API
 */

public class NetworkUtils {
    //TODO: add the api URL into URL
    private final static String BASE_URL =
            "";


    private final static String TAG = "NetworkUtils";

    public static java.net.URL buildUrl() {
        Uri builtUri = null;
        builtUri = Uri.parse(BASE_URL).buildUpon()
                .build();

        URL url = null;
        try {
            url = new URL(builtUri != null ? builtUri.toString() : null);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }

    public JSONObject getDataFromApi() {
        JSONObject Jobject = null;
        Log.d("MIKE exception ", "MIKE");
        try {
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url(BASE_URL)
                    .build();
            Response responses = null;
            try {
                responses = client.newCall(request).execute();
                return new JSONObject(responses.body().string());
            } catch (IOException e) {
                e.printStackTrace();
                Log.d("MIKE EXCEPTION@#", e.toString());
            }
        } catch (JSONException e) {
            Log.d("MIKE exception lol", e.toString());
        }
        return Jobject;
    }
}


