package com.m68476521.mymexico.fragmentnews;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.SharedElementCallback;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.transition.TransitionInflater;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.m68476521.mymexico.MainActivity;
import com.m68476521.mymexico.R;
import com.m68476521.mymexico.data.TaskContract;
import com.m68476521.mymexico.databinding.FragmentNewsBinding;
import com.m68476521.mymexico.fragmentNewsDetails.NewsViewPagerFragment;
import com.m68476521.mymexico.utilities.NetworkUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URL;
import java.util.List;
import java.util.Map;

/**
 * Fragment to shows news.
 */

public class News extends Fragment {
    public static final String TAG = News.class.getSimpleName();
    private FragmentNewsBinding fragmentNewsBinding;
    private Cursor cursor;
    private NewsAdapter newsAdapter;
    NewsItemClickListener newsItemClickListener;

    public static News newInstance(NewsItemClickListener newsItemClickListener) {
        Bundle args = new Bundle();
//        args.putSerializable(ARG_MOVIE_ID, id);
        News fragment = new News();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            newsItemClickListener = (NewsItemClickListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " MIKE must implement OnArticleSelectedListener");
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        cursor = getContext().getContentResolver().query(TaskContract.TaskEntry.CONTENT_URI,
                null,
                null,
                null,
                TaskContract.TaskEntry.COLUMN_ID);
        if (cursor == null || cursor.getCount() == 0) {
            Log.d("MIKE get API ", "from news");
            getApiData();
        }

        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            int descriptionIndex = cursor.getColumnIndex(TaskContract.TaskEntry.COLUMN_NAME);
            String name = cursor.getString(descriptionIndex);
            Log.d("MIKE", "data got it: " + name);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d("MIKE", " onREsume");
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d("MIKE", " onStart");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d("MIKE", " onPasue");
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("MIKE", " onDestroy");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        fragmentNewsBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_news, container, false);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(),
                GridLayoutManager.VERTICAL, false);

        fragmentNewsBinding.recyclerView.setLayoutManager(linearLayoutManager);

        newsAdapter = new NewsAdapter(getActivity(), new NewsAdapter.OnItemClicked() {
            @Override
            public void onItemClick(View view, int position) {
                Log.d("MIKE", " CLICKED: " + Integer.toString(position));
                setFavoriteNews(position);
            }
        }, new NewsItemClickListener() {
            @Override
            public void onlItemClick(String section,int pos, ImageView shareImageView, View v) {
                newsItemClickListener.onlItemClick(section,pos, shareImageView, v);
            }
        });

        fragmentNewsBinding.recyclerView.setAdapter(newsAdapter);

        if (cursor != null) {
            Log.d("MIKE", "is not null from news");
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
            Log.d("MIKE ", "doInBackground from News");
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
                        Log.d("MIKE:: ", name + "|" + id + "|" + description + "|" + image + "|" + lastModified);

                        ContentValues contentValues = new ContentValues();
                        contentValues.put(TaskContract.TaskEntry.COLUMN_ID, id);
                        contentValues.put(TaskContract.TaskEntry.COLUMN_NAME, name);
                        contentValues.put(TaskContract.TaskEntry.COLUMN_DESCRIPTION, description);
                        contentValues.put(TaskContract.TaskEntry.COLUMN_IMAGE, image);
                        contentValues.put(TaskContract.TaskEntry.COLUMN_LAST_MODIFIED, lastModified);

                        Uri uri = getContext().getContentResolver().insert(TaskContract.TaskEntry.CONTENT_URI, contentValues);
                    }

                    Log.d("MIKE done call", " the cursor");
                    cursor = getContext().getContentResolver().query(TaskContract.TaskEntry.CONTENT_URI,
                            null,
                            null,
                            null,
                            TaskContract.TaskEntry.COLUMN_ID);

                    if (cursor != null) {
                        Log.d("MIKE", "is not null from news");
                        setupAdapterByCursor(cursor);
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

    private void setFavoriteNews(int position) {
        Cursor cursorResult = null;
        cursor.moveToPosition(position);
        String mId = cursor.getString(cursor.getColumnIndex(TaskContract.TaskEntry.COLUMN_ID));

        Log.d("MIKE", "MIKE " + mId);
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
            Log.d("MIKE", "MIKE is fav" + mId);
            Uri uri = TaskContract.TaskEntry.CONTENT_URI_FAVORITES;
            uri = uri.buildUpon().appendPath(mId).build();
            getContext().getContentResolver().delete(uri, null, null);
        } else {
            Log.d("MIKE", "MIKE is not" + mId);
            ContentValues contentValues = new ContentValues();
            contentValues.put(TaskContract.TaskEntry.COLUMN_ID, mId);
            contentValues.put(TaskContract.TaskEntry.COLUMN_NAME, cursor.getString(cursor.getColumnIndex(TaskContract.TaskEntry.COLUMN_NAME)));
            contentValues.put(TaskContract.TaskEntry.COLUMN_DESCRIPTION, cursor.getString(cursor.getColumnIndex(TaskContract.TaskEntry.COLUMN_DESCRIPTION)));
            contentValues.put(TaskContract.TaskEntry.COLUMN_IMAGE, cursor.getString(cursor.getColumnIndex(TaskContract.TaskEntry.COLUMN_IMAGE)));
            contentValues.put(TaskContract.TaskEntry.COLUMN_LAST_MODIFIED, cursor.getString(cursor.getColumnIndex(TaskContract.TaskEntry.COLUMN_LAST_MODIFIED)));
            Uri uri =
                    getContext().getContentResolver().insert(
                            TaskContract.TaskEntry.CONTENT_URI_FAVORITES, contentValues);

            if (uri != null) {
                Log.d("MIKE URL", uri.toString());
                Toast.makeText(getContext(), uri.toString(), Toast.LENGTH_LONG).show();
            }
        }
    }
}
