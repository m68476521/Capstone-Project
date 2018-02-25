package com.m68476521.mymexico.fragmentNewsDetails;

import android.content.Context;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.transition.Explode;
import android.transition.TransitionInflater;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.m68476521.mymexico.R;
import com.m68476521.mymexico.data.TaskContract;
import com.m68476521.mymexico.transitions.DetailsTransition;

/**
 * Created by mike on 2/15/18.
 */

public class NewsViewPagerFragment extends Fragment {
    private static final String EXTRA_INITIAL_ITEM_POS = "initial_item_pos";
    private Cursor cursor;
    private ViewPager viewPager;

    public NewsViewPagerFragment() {

    }

    public static NewsViewPagerFragment newInstance(int currentItem, Context context) {
        NewsViewPagerFragment newsViewPagerFragment = new NewsViewPagerFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(EXTRA_INITIAL_ITEM_POS, currentItem);
        newsViewPagerFragment.setArguments(bundle);
        newsViewPagerFragment.setEnterTransition(new Explode());
        newsViewPagerFragment.setReenterTransition(TransitionInflater.from(context).inflateTransition(R.transition.slide_left));
        newsViewPagerFragment.setExitTransition(TransitionInflater.from(context).inflateTransition(R.transition.slide_bottom));
        return newsViewPagerFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        postponeEnterTransition();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Log.d("MIKE tag", "C");
            setSharedElementEnterTransition(TransitionInflater.from(getContext()).inflateTransition(android.R.transition.move));
        }
        setSharedElementReturnTransition(null);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_news_view_pager, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        int currentItem = getArguments().getInt(EXTRA_INITIAL_ITEM_POS);

        cursor = getContext().getContentResolver().query(TaskContract.TaskEntry.CONTENT_URI,
                null,
                null,
                null,
                TaskContract.TaskEntry.COLUMN_ID);

        NewsPageAdapter newsPageAdapter = new NewsPageAdapter(getChildFragmentManager(), getContext());

        viewPager = (ViewPager) view.findViewById(R.id.news_view_pager);
        viewPager.setAdapter(newsPageAdapter);
        viewPager.setCurrentItem(currentItem);
    }
}
