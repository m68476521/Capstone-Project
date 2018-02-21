package com.m68476521.mymexico.fragmentNewsDetails;

import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.transition.TransitionInflater;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.m68476521.mymexico.R;
import com.m68476521.mymexico.data.TaskContract;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

/**
 * Created by mike on 2/17/18.
 */

public class NewsDetailFragment extends Fragment {
    private static final String EXTRA_NEWS_POSITION = "news_position";
    private static final String EXTRA_TRANSITION_NAME = "transition_name";
    private Cursor cursor;

    public NewsDetailFragment() {
        // Required empty public constructor
    }

    public static NewsDetailFragment newInstance(int newsPosition, String transitionName) {
        NewsDetailFragment newsDetailFragment = new NewsDetailFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(EXTRA_NEWS_POSITION, newsPosition);
        bundle.putString(EXTRA_TRANSITION_NAME, transitionName);
        newsDetailFragment.setArguments(bundle);
        return newsDetailFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        postponeEnterTransition();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Log.d("MIKE tag", "A");
            setSharedElementEnterTransition(TransitionInflater.from(getContext()).inflateTransition(android.R.transition.move));
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_news_detail, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        cursor = getContext().getContentResolver().query(TaskContract.TaskEntry.CONTENT_URI,
                null,
                null,
                null,
                TaskContract.TaskEntry.COLUMN_ID);


        Integer position = getArguments().getInt(EXTRA_NEWS_POSITION);
        String transitionName = getArguments().getString(EXTRA_TRANSITION_NAME);

        cursor.moveToPosition(position);

        TextView nameTextView = (TextView) view.findViewById(R.id.news_name_text);

        String title = cursor.getString(cursor.getColumnIndex(TaskContract.TaskEntry.COLUMN_NAME));

        nameTextView.setText(title);

        TextView detailTextView = view.findViewById(R.id.news_detail_text);
        detailTextView.setText(cursor.getString(cursor.getColumnIndex(TaskContract.TaskEntry.COLUMN_DESCRIPTION)));

        ImageView imageView = (ImageView) view.findViewById(R.id.news_detail_image_view);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Log.d("MIKE tag", "B");
            imageView.setTransitionName(transitionName);
        }

        Picasso.with(getContext())
                .load(cursor.getString(cursor.getColumnIndex(TaskContract.TaskEntry.COLUMN_IMAGE)))
                .noFade()
                .into(imageView, new Callback() {
                    @Override
                    public void onSuccess() {
                        startPostponedEnterTransition();
                    }

                    @Override
                    public void onError() {
                        startPostponedEnterTransition();
                    }
                });
    }
}
