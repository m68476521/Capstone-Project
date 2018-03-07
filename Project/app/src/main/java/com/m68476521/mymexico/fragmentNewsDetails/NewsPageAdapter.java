package com.m68476521.mymexico.fragmentNewsDetails;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.m68476521.mymexico.data.TaskContract;

/**
 * This class is an adapter for the ViewPager
 */

public class NewsPageAdapter extends FragmentStatePagerAdapter {
    private static final String EXTRA_SECTION_NEWS = "NEWS";
    private final Cursor cursor;
    private final String section;

    NewsPageAdapter(String section, FragmentManager fm, Context context) {
        super(fm);
        Context context1 = context;
        this.section = section;

        if (section.equals(EXTRA_SECTION_NEWS)) {
            cursor = context.getContentResolver().query(TaskContract.TaskEntry.CONTENT_URI,
                    null,
                    null,
                    null,
                    TaskContract.TaskEntry.COLUMN_ID);
        } else {
            cursor = context.getContentResolver().query(TaskContract.TaskEntry.CONTENT_URI_FAVORITES,
                    null,
                    null,
                    null,
                    TaskContract.TaskEntry.COLUMN_ID);

        }
    }

    @Override
    public Fragment getItem(int position) {
        cursor.moveToPosition(position);
        return NewsDetailFragment.newInstance(section, position, cursor.getString(cursor.getColumnIndex(TaskContract.TaskEntry.COLUMN_NAME)));
    }

    @Override
    public int getCount() {
        return cursor.getCount();
    }
}
