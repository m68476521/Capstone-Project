package com.m68476521.mymexico.fragmentfav;

import android.content.ContentValues;
import android.content.Context;
import android.content.res.Configuration;
import android.database.ContentObserver;
import android.database.Cursor;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.m68476521.mymexico.R;
import com.m68476521.mymexico.data.TaskContract;
import com.m68476521.mymexico.databinding.FragmentFavoriteBinding;
import com.m68476521.mymexico.fragmentnews.NewsAdapter;
import com.m68476521.mymexico.fragmentnews.NewsItemClickListener;

/**
 * This fragment will be handling favorites for small devices
 */

public class FragmentFavorite extends Fragment {
    private FragmentFavoriteBinding fragmentFavoriteBinding;
    private Cursor cursor;
    private NewsAdapter newsAdapter;
    NewsItemClickListener newsItemClickListener;

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
        cursor = getContext().getContentResolver().query(TaskContract.TaskEntry.CONTENT_URI_FAVORITES,
                null,
                null,
                null,
                TaskContract.TaskEntry.COLUMN_ID);

        MyObserver myObserver = new MyObserver(new Handler());
        getContext().getContentResolver().registerContentObserver(
                TaskContract.TaskEntry.CONTENT_URI_FAVORITES,
                true, myObserver
        );
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
                setFavoriteNews(position);
            }
        }, new NewsItemClickListener() {
            @Override
            public void onlItemClick(String section, int pos, ImageView shareImageView, View view) {
                newsItemClickListener.onlItemClick("FAVORITES", pos, shareImageView, view);
            }
        });

        fragmentFavoriteBinding.recyclerViewFav.setAdapter(newsAdapter);

        int numberColumns;
        boolean isTablet = getResources().getBoolean(R.bool.isTablet);
        if (isTablet) {
            if (getActivity().getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
                numberColumns = 2;
            } else {
                numberColumns = 3;
            }
            GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(),numberColumns);
            fragmentFavoriteBinding.recyclerViewFav.setLayoutManager(gridLayoutManager);
        } else {
            fragmentFavoriteBinding.recyclerViewFav.setLayoutManager(linearLayoutManager);
        }

        if (cursor != null && cursor.getCount() > 0) {
            setupAdapterByCursor(cursor);
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
            contentValues.put(TaskContract.TaskEntry.COLUMN_LAST_MODIFIED, cursor.getString(cursor.getColumnIndex(TaskContract.TaskEntry.COLUMN_LAST_MODIFIED)));
            Uri uri =
                    getContext().getContentResolver().insert(
                            TaskContract.TaskEntry.CONTENT_URI_FAVORITES, contentValues);

            if (uri != null) {
                Toast.makeText(getContext(), uri.toString(), Toast.LENGTH_LONG).show();
            }
        }
    }

    class MyObserver extends ContentObserver {
        public MyObserver(Handler handler) {
            super(handler);
        }

        @Override
        public void onChange(boolean selfChange) {
            super.onChange(selfChange);
        }


        @Override
        public void onChange(boolean selfChange, Uri uri) {
            super.onChange(selfChange, uri);
            cursor = getContext().getContentResolver().query(TaskContract.TaskEntry.CONTENT_URI_FAVORITES,
                    null,
                    null,
                    null,
                    TaskContract.TaskEntry.COLUMN_ID);
            setupAdapterByCursor(cursor);
        }
    }
}
