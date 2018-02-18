package com.m68476521.mymexico.fragmentNewsDetails;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.m68476521.mymexico.data.TaskContract;

/**
 * Created by mike on 2/17/18.
 */

public class NewsPageAdapter extends FragmentStatePagerAdapter {

    private Cursor cursor;
    private Context context;

    NewsPageAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.context = context;

        cursor = context.getContentResolver().query(TaskContract.TaskEntry.CONTENT_URI,
                null,
                null,
                null,
                TaskContract.TaskEntry.COLUMN_ID);
    }

    @Override
    public Fragment getItem(int position) {
        cursor.moveToPosition(position);
        return NewsDetailFragment.newInstance(position, cursor.getString(cursor.getColumnIndex(TaskContract.TaskEntry.COLUMN_NAME)));
    }

    @Override
    public int getCount() {
        return cursor.getCount();
    }
}
