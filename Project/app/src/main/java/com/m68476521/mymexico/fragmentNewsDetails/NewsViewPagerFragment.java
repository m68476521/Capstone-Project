package com.m68476521.mymexico.fragmentNewsDetails;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.transition.Explode;
import android.transition.TransitionInflater;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.m68476521.mymexico.R;

/**
 * This class is a fragment that contains the main tabLayout for news, favorites and tricks
 */

public class NewsViewPagerFragment extends Fragment {
    private static final String EXTRA_INITIAL_ITEM_POS = "initial_item_pos";
    private static final String EXTRA_SECTION = "section";
    private String section;

    public NewsViewPagerFragment() {

    }

    public static NewsViewPagerFragment newInstance(String section, int currentItem, Context context) {
        NewsViewPagerFragment newsViewPagerFragment = new NewsViewPagerFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(EXTRA_INITIAL_ITEM_POS, currentItem);
        bundle.putString(EXTRA_SECTION, section);
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

        setSharedElementEnterTransition(TransitionInflater.from(getContext()).inflateTransition(android.R.transition.move));

        setSharedElementReturnTransition(null);
        if (getArguments().containsKey(EXTRA_SECTION)) {
            section = getArguments().getString(EXTRA_SECTION);
        }
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

        NewsPageAdapter newsPageAdapter = new NewsPageAdapter(section, getChildFragmentManager(), getContext());

        ViewPager viewPager = view.findViewById(R.id.news_view_pager);
        viewPager.setAdapter(newsPageAdapter);
        viewPager.setCurrentItem(currentItem);
    }
}
