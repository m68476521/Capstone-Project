package com.m68476521.mymexico;

import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;

import com.m68476521.mymexico.fragmentNewsDetails.NewsViewPagerFragment;
import com.m68476521.mymexico.fragmentmain.FragmentMainSmall;
import com.m68476521.mymexico.fragmentnews.NewsItemClickListener;

public class MainActivity extends AppCompatActivity implements NewsItemClickListener {
    private static final String TAG = MainActivity.class.getSimpleName();
    private static final int TASK_LOADER_ID = 0;
    private Cursor cursor;
    FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResId(0));
        //getSupportLoaderManager().initLoader(TASK_LOADER_ID, null, this);

        fragmentManager = getSupportFragmentManager();

        if (savedInstanceState == null) {
            fragmentManager
                    .beginTransaction()
                    .add(R.id.main_frame, createFragment())
                    .commit();
        }
    }

    @LayoutRes
    private int getLayoutResId(int layout) {
        if (layout == 0) {
            return R.layout.activity_main;
        } else {
//            return R.layout.fragment_master_container;
            return 0;
        }
    }

    protected Fragment createFragment() {
        return new FragmentMainSmall();
    }

    @Override
    public void onlItemClick(int pos, ImageView shareImageView) {
        Log.d("MIKE MAINACTIVITY", Integer.toString(pos) + " " + shareImageView.toString());

        Fragment newsViewPagerFragment = NewsViewPagerFragment.newInstance(pos);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Log.d("MIKE", "add transtision");
//                    newsViewPagerFragment.setSharedElementEnterTransition(new DetailsTransition());
//                    newsViewPagerFragment.setEnterTransition(new Fade());
//                    getWindow().setExitTransition(new Fade());
//                    newsViewPagerFragment.setSharedElementReturnTransition(new DetailsTransition());
        }

        fragmentManager
                .beginTransaction()
                .addSharedElement(shareImageView, ViewCompat.getTransitionName(shareImageView))
//                        .re
                .replace(R.id.main_frame, newsViewPagerFragment)
                .addToBackStack("MIKE")
                .commit();
    }
}