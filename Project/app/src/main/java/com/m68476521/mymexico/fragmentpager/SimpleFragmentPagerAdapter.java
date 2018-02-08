package com.m68476521.mymexico.fragmentpager;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.m68476521.mymexico.R;
import com.m68476521.mymexico.fragmentfav.FragmentFavorite;
import com.m68476521.mymexico.fragmentnews.News;
import com.m68476521.mymexico.fragmenttrick.FragmentTrick;

/**
 * Adapter used to the Fragment FragmentMainSmall
 */

public class SimpleFragmentPagerAdapter extends FragmentPagerAdapter {
    private Context mContext;

    public SimpleFragmentPagerAdapter(Context context, FragmentManager fm) {
        super(fm);
        mContext = context;
    }

    // This determines the fragment for each tab
    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            return new News();
        } else if (position == 1) {
            return new FragmentFavorite();
        } else {
            return new FragmentTrick();
        }
    }

    // This determines the number of tabs
    @Override
    public int getCount() {
        return 3;
    }

    // This determines the title for each tab
    @Override
    public CharSequence getPageTitle(int position) {
        // Generate title based on item position
        switch (position) {
            case 0:
                return mContext.getString(R.string.news);
            case 1:
                return mContext.getString(R.string.favorites);
            case 2:
                return mContext.getString(R.string.tricks);
            default:
                return null;
        }
    }
}
