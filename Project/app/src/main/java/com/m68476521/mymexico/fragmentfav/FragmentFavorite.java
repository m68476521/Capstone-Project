package com.m68476521.mymexico.fragmentfav;

import android.content.ContentValues;
import android.database.ContentObserver;
import android.database.Cursor;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.m68476521.mymexico.R;
import com.m68476521.mymexico.data.TaskContract;
import com.m68476521.mymexico.databinding.FragmentFavoriteBinding;
import com.m68476521.mymexico.fragmentnews.NewsAdapter;

/**
 * This fragment will be handling favorites for small devices
 */

public class FragmentFavorite extends Fragment{
    private FragmentFavoriteBinding fragmentFavoriteBinding;
    private Cursor cursor;
    private NewsAdapter newsAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("MIKE on create", "favo");
        cursor = getContext().getContentResolver().query(TaskContract.TaskEntry.CONTENT_URI_FAVORITES,
                null,
                null,
                null,
                TaskContract.TaskEntry.COLUMN_ID);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        fragmentFavoriteBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_favorite, container, false);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());

        fragmentFavoriteBinding.recyclerViewFav.setHasFixedSize(true);

        newsAdapter = new NewsAdapter(getActivity(), new NewsAdapter.OnItemClicked() {
            @Override
            public void onItemClick(View view, int position) {
                Log.d("MIKE", " CLICKED: " + Integer.toString(position));
                setFavoriteNews(position);
            }
        });

        fragmentFavoriteBinding.recyclerViewFav.setAdapter(newsAdapter);

        fragmentFavoriteBinding.recyclerViewFav.setLayoutManager(linearLayoutManager);
        if (cursor != null && cursor.getCount() > 0) {
            Log.d("MIKE", "is not null");
            setupAdapterByCursor(cursor);
        }

        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            int descriptionIndex = cursor.getColumnIndex(TaskContract.TaskEntry.COLUMN_NAME);
            String name = cursor.getString(descriptionIndex);
            Log.d("MIKE FAV", "data got it: " + name);
        }
        return fragmentFavoriteBinding.getRoot();
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
