package com.m68476521.mymexico.fragmentNewsDetails;

import android.content.Intent;
import android.database.Cursor;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.ShareCompat;
import android.support.v7.app.AppCompatActivity;
import android.transition.TransitionInflater;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.m68476521.mymexico.R;
import com.m68476521.mymexico.data.TaskContract;
import com.m68476521.mymexico.databinding.FragmentNewsDetailBinding;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

/**
 * This is the synopsis page for the news
 */

public class NewsDetailFragment extends Fragment {
    private static final String EXTRA_NEWS_POSITION = "news_position";
    private static final String EXTRA_TRANSITION_NAME = "transition_name";
    private static final String EXTRA_SECTION = "section";
    private static final String EXTRA_SECTION_NEWS = "NEWS";
    private Cursor cursor;
    private String section;

    private FragmentNewsDetailBinding binding;

    public NewsDetailFragment() {
        // Required empty public constructor
    }

    public static NewsDetailFragment newInstance(String section, int newsPosition, String transitionName) {
        NewsDetailFragment newsDetailFragment = new NewsDetailFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(EXTRA_NEWS_POSITION, newsPosition);
        bundle.putString(EXTRA_TRANSITION_NAME, transitionName);
        bundle.putString(EXTRA_SECTION, section);
        newsDetailFragment.setArguments(bundle);
        return newsDetailFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        postponeEnterTransition();

        setSharedElementEnterTransition(TransitionInflater.from(getContext()).inflateTransition(android.R.transition.move));

        if (getArguments().containsKey(EXTRA_SECTION)) {
            section = getArguments().getString(EXTRA_SECTION);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_news_detail, container, false);

        if (section.equals(EXTRA_SECTION_NEWS)) {
            cursor = getContext().getContentResolver().query(TaskContract.TaskEntry.CONTENT_URI,
                    null,
                    null,
                    null,
                    TaskContract.TaskEntry.COLUMN_ID);
        } else {
            cursor = getContext().getContentResolver().query(TaskContract.TaskEntry.CONTENT_URI_FAVORITES,
                    null,
                    null,
                    null,
                    TaskContract.TaskEntry.COLUMN_ID);
        }

        Integer position = getArguments().getInt(EXTRA_NEWS_POSITION);
        String transitionName = getArguments().getString(EXTRA_TRANSITION_NAME);

        cursor.moveToPosition(position);

        final String title = cursor.getString(cursor.getColumnIndex(TaskContract.TaskEntry.COLUMN_NAME));

        assert binding.toolbar != null;
        binding.toolbar.setNavigationIcon(R.drawable.ic_arrow_back);

        ((AppCompatActivity) getActivity()).setSupportActionBar(binding.toolbar);
        binding.toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });
        assert binding.collapsingToolbar != null;
        binding.collapsingToolbar.setTitle(title);
        binding.collapsingToolbar.setCollapsedTitleTextColor(Color.WHITE);
        binding.collapsingToolbar.setExpandedTitleColor(Color.WHITE);
        assert binding.appbar != null;
        binding.appbar.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = false;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (scrollRange + verticalOffset == 0) {
                    binding.collapsingToolbar.setTitle(title);
                    isShow = true;
                } else if (isShow) {
                    binding.collapsingToolbar.setTitle(title);
                    isShow = false;
                }
            }
        });

        binding.newsDetailText.setText(cursor.getString(cursor.getColumnIndex(TaskContract.TaskEntry.COLUMN_DESCRIPTION)));

        binding.newsDetailImageView.setTransitionName(transitionName);

        binding.categoryTextView.setText(cursor.getString(cursor.getColumnIndex(TaskContract.TaskEntry.COLUMN_CATEGORY)));

        Picasso.with(getContext())
                .load(cursor.getString(cursor.getColumnIndex(TaskContract.TaskEntry.COLUMN_IMAGE)))
                .noFade()
                .into(binding.newsDetailImageView, new Callback() {
                    @Override
                    public void onSuccess() {
                        startPostponedEnterTransition();
                    }

                    @Override
                    public void onError() {
                        startPostponedEnterTransition();
                    }
                });

        binding.shareFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(Intent.createChooser(ShareCompat.IntentBuilder.from(getActivity())
                        .setType("text/plain")
                        .setText(cursor.getString(cursor.getColumnIndex(TaskContract.TaskEntry.COLUMN_DESCRIPTION)))
                        .getIntent(), getString(R.string.action_share)));
            }
        });
        if (section.equals(EXTRA_SECTION_NEWS)) {
            binding.favImageView.setVisibility(View.GONE);
        } else {
            binding.favImageView.setVisibility(View.VISIBLE);
        }

        return binding.getRoot();
    }
}
