package com.m68476521.mymexico.fragmentnews;

import android.content.ContentValues;
import android.content.Context;
import android.content.res.Configuration;
import android.database.Cursor;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

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
    private static final String TAG = News.class.getSimpleName();
    private static Cursor cursor;
    private static NewsAdapter newsAdapter;
    private NewsItemClickListener newsItemClickListener;
    private static Context context;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            newsItemClickListener = (NewsItemClickListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " Catch must implement OnArticleSelectedListener");
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        context = getContext();
        cursor = getContext().getContentResolver().query(TaskContract.TaskEntry.CONTENT_URI,
                null,
                null,
                null,
                TaskContract.TaskEntry.COLUMN_ID);
        if (cursor == null || cursor.getCount() == 0) {
            Log.d(" get API ", "from news");
            getApiData();
        }

        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            int descriptionIndex = cursor.getColumnIndex(TaskContract.TaskEntry.COLUMN_NAME);
            String name = cursor.getString(descriptionIndex);
            Log.d(TAG, "data got it: " + name);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        FragmentNewsBinding fragmentNewsBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_news, container, false);

        int numberColumns;
        boolean isTablet = getResources().getBoolean(R.bool.isTablet);
        if (isTablet) {
            if (getActivity().getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
                numberColumns = 2;
            } else {
                numberColumns = 3;
            }
            GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), numberColumns);
            fragmentNewsBinding.recyclerView.setLayoutManager(gridLayoutManager);
        } else {
            if (getActivity().getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
                numberColumns = 1;
            } else {
                numberColumns = 2;
            }
            GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), numberColumns);
            fragmentNewsBinding.recyclerView.setLayoutManager(gridLayoutManager);
        }

        newsAdapter = new NewsAdapter(getActivity(), new NewsAdapter.OnItemClicked() {
            @Override
            public void onItemClick(View view, int position) {
                setFavoriteNews(position);
            }
        }, new NewsItemClickListener() {
            @Override
            public void onlItemClick(String section, int pos, ImageView shareImageView, View v) {
                newsItemClickListener.onlItemClick(section, pos, shareImageView, v);
            }
        });

        fragmentNewsBinding.recyclerView.setAdapter(newsAdapter);

        if (cursor != null) {
            setupAdapterByCursor(cursor);
        }
        return fragmentNewsBinding.getRoot();
    }

    private void getApiData() {
        URL searchUrl = NetworkUtils.buildUrl();
        new RetrieveInfoAPI().execute(searchUrl);
    }

    private static class RetrieveInfoAPI extends AsyncTask<URL, Void, String> {
        JSONObject result = null;

        @Override
        protected String doInBackground(URL... urls) {
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
                        String category = innerObject.getString(TaskContract.TaskEntry.COLUMN_CATEGORY);
                        String short_desc = innerObject.getString(TaskContract.TaskEntry.COLUMN_SHORT_DESCRIPTION);

                        ContentValues contentValues = new ContentValues();
                        contentValues.put(TaskContract.TaskEntry.COLUMN_ID, id);
                        contentValues.put(TaskContract.TaskEntry.COLUMN_NAME, name);
                        contentValues.put(TaskContract.TaskEntry.COLUMN_DESCRIPTION, description);
                        contentValues.put(TaskContract.TaskEntry.COLUMN_IMAGE, image);
                        contentValues.put(TaskContract.TaskEntry.COLUMN_SHORT_DESCRIPTION, short_desc);
                        contentValues.put(TaskContract.TaskEntry.COLUMN_CATEGORY, category);
                        context.getContentResolver().insert(TaskContract.TaskEntry.CONTENT_URI, contentValues);
                    }
                }

            } catch (Exception e) {
                Log.d("EXCEPTION::", e.toString());
            }
            return "";
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            cursor = context.getContentResolver().query(TaskContract.TaskEntry.CONTENT_URI,
                    null,
                    null,
                    null,
                    TaskContract.TaskEntry.COLUMN_ID);

            if (cursor != null) {
                setupAdapterByCursor(cursor);
            }
        }
    }

    private static void setupAdapterByCursor(Cursor cursor) {
        newsAdapter.swapCursor(cursor);
    }

    private void setFavoriteNews(int position) {
        Cursor cursorResult = null;
        cursor.moveToPosition(position);
        String mId = cursor.getString(cursor.getColumnIndex(TaskContract.TaskEntry.COLUMN_ID));

        try {
            Uri uri2 = TaskContract.TaskEntry.CONTENT_URI_FAVORITES;

            uri2 = uri2.buildUpon().appendPath(mId).build();
            cursorResult = getContext().getContentResolver().query(uri2,
                    null,
                    null,
                    null,
                    null);
        } finally {
            cursorResult.close();
        }
        int numberCount = cursorResult.getCount();
        boolean isFav = numberCount > 0;

        if (isFav) {
            Uri uri = TaskContract.TaskEntry.CONTENT_URI_FAVORITES;
            uri = uri.buildUpon().appendPath(mId).build();
            getContext().getContentResolver().delete(uri, null, null);
        } else {
            ContentValues contentValues = new ContentValues();
            contentValues.put(TaskContract.TaskEntry.COLUMN_ID, mId);
            contentValues.put(TaskContract.TaskEntry.COLUMN_NAME, cursor.getString(cursor.getColumnIndex(TaskContract.TaskEntry.COLUMN_NAME)));
            contentValues.put(TaskContract.TaskEntry.COLUMN_DESCRIPTION, cursor.getString(cursor.getColumnIndex(TaskContract.TaskEntry.COLUMN_DESCRIPTION)));
            contentValues.put(TaskContract.TaskEntry.COLUMN_IMAGE, cursor.getString(cursor.getColumnIndex(TaskContract.TaskEntry.COLUMN_IMAGE)));
            contentValues.put(TaskContract.TaskEntry.COLUMN_SHORT_DESCRIPTION, cursor.getString(cursor.getColumnIndex(TaskContract.TaskEntry.COLUMN_SHORT_DESCRIPTION)));
            contentValues.put(TaskContract.TaskEntry.COLUMN_CATEGORY, cursor.getString(cursor.getColumnIndex(TaskContract.TaskEntry.COLUMN_CATEGORY)));
            Uri uri =
                    getContext().getContentResolver().insert(
                            TaskContract.TaskEntry.CONTENT_URI_FAVORITES, contentValues);

            if (uri != null) {
                Toast.makeText(getContext(), uri.toString(), Toast.LENGTH_LONG).show();
            }
        }
    }
}
