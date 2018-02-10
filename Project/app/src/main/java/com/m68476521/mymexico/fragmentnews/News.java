package com.m68476521.mymexico.fragmentnews;

import android.content.ContentValues;
import android.database.Cursor;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.m68476521.mymexico.R;
import com.m68476521.mymexico.data.TaskContract;
import com.m68476521.mymexico.databinding.FragmentNewsBinding;
import com.m68476521.mymexico.utilities.NetworkUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URL;

/**
 * Fragment to shows news.
 */

public class News extends Fragment {

    private FragmentNewsBinding fragmentNewsBinding;
    private Cursor cursor;
    private NewsAdapter newsAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        cursor = getContext().getContentResolver().query(TaskContract.TaskEntry.CONTENT_URI,
                null,
                null,
                null,
                TaskContract.TaskEntry.COLUMN_ID);
        if (cursor == null || cursor.getCount() == 0) {
            getApiData();
        }

//        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
//            int descriptionIndex = cursor.getColumnIndex(TaskContract.TaskEntry.COLUMN_NAME);
//            String name = cursor.getString(descriptionIndex);
//            Log.d("MIKE", "data got it: " + name);
//        }


    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        fragmentNewsBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_news, container, false);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(),
                GridLayoutManager.VERTICAL, false);

        fragmentNewsBinding.recyclerView.setLayoutManager(linearLayoutManager);
        newsAdapter = new NewsAdapter(getActivity());
        fragmentNewsBinding.recyclerView.setAdapter(newsAdapter);

        if (cursor != null) {
            Log.d("MIKE", "is not null");
            setupAdapterByCursor(cursor);
        }
        return fragmentNewsBinding.getRoot();
    }

    private void getApiData() {
        URL searchUrl = NetworkUtils.buildUrl();
        new RetrieveInfoAPI().execute(searchUrl);
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

                        Uri uri = getContext().getContentResolver().insert(TaskContract.TaskEntry.CONTENT_URI, contentValues);
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

    private void setupAdapterByCursor(Cursor cursor) {
        newsAdapter.swapCursor(cursor);
    }
}
