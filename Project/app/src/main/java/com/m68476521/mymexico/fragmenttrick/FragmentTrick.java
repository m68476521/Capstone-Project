package com.m68476521.mymexico.fragmenttrick;

import android.content.Context;
import android.content.res.Configuration;
import android.database.ContentObserver;
import android.database.Cursor;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.m68476521.mymexico.R;
import com.m68476521.mymexico.data.TaskContract;
import com.m68476521.mymexico.databinding.FragmentTrickBinding;

public class FragmentTrick extends Fragment {
    private static final String EXTRA_BODY_QUESTION = "bodyQuestion";
    private static final String EXTRA_BODY_ANSWER = "bodyAnswer";
    private static final String EXTRA_BODY_FAKE_A = "bodyFakeA";
    private static final String EXTRA_BODY_FAKE_B = "bodyFakeB";

    private Cursor cursor;
    private TrickAdapter trickAdapter;
    private Context context;

    public FragmentTrick() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getActivity();
        cursor = getContext().getContentResolver().query(TaskContract.TaskEntry.CONTENT_URI_TRICKS,
                null,
                null,
                null,
                TaskContract.TaskEntry.COLUMN_ID);

        MyObserver myObserver = new MyObserver(new Handler());
        getContext().getContentResolver().registerContentObserver(
                TaskContract.TaskEntry.CONTENT_URI_TRICKS,
                true, myObserver
        );
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        FragmentTrickBinding fragmentTrickBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_trick, container, false);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());

        fragmentTrickBinding.recyclerViewTrick.setHasFixedSize(true);

        trickAdapter = new TrickAdapter(getActivity(), new TrickAdapter.OnItemClicked() {
            @Override
            public void onItemClick(View view, int position) {
                cursor.moveToPosition(position);
                BottomSheetDialogFragment bottomSheetDialogFragment = new CustomBottomSheet();
                Bundle args = new Bundle();
                args.putString(EXTRA_BODY_QUESTION, cursor.getString(cursor.getColumnIndex(TaskContract.TaskEntry.COLUMN_FCM_QUESTION)));
                args.putString(EXTRA_BODY_FAKE_A, cursor.getString(cursor.getColumnIndex(TaskContract.TaskEntry.COLUMN_FCM_FAKE_ANS_A)));
                args.putString(EXTRA_BODY_FAKE_B, cursor.getString(cursor.getColumnIndex(TaskContract.TaskEntry.COLUMN_FCM_FAKE_ANS_B)));
                args.putString(EXTRA_BODY_ANSWER, cursor.getString(cursor.getColumnIndex(TaskContract.TaskEntry.COLUMN_FCM_ANSWER)));

                bottomSheetDialogFragment.setArguments(args);
                bottomSheetDialogFragment.show(getActivity().getSupportFragmentManager(), bottomSheetDialogFragment.getTag());
            }
        });

        fragmentTrickBinding.recyclerViewTrick.setAdapter(trickAdapter);

        int numberColumns;
        boolean isTablet = getResources().getBoolean(R.bool.isTablet);
        if (isTablet) {
            if (getActivity().getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
                numberColumns = 2;
            } else {
                numberColumns = 3;
            }
            GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), numberColumns);
            fragmentTrickBinding.recyclerViewTrick.setLayoutManager(gridLayoutManager);
        } else {
            fragmentTrickBinding.recyclerViewTrick.setLayoutManager(linearLayoutManager);
        }

        if (cursor != null && cursor.getCount() > 0) {
            setupAdapterByCursor(cursor);
        }

        return fragmentTrickBinding.getRoot();
    }

    private void setupAdapterByCursor(Cursor cursor) {
        trickAdapter.swapCursor(cursor);
    }

    class MyObserver extends ContentObserver {
        public MyObserver(Handler handler) {
            super(handler);
        }

        @Override
        public void onChange(boolean selfChange, Uri uri) {
            super.onChange(selfChange, uri);
            cursor = context.getContentResolver().query(TaskContract.TaskEntry.CONTENT_URI_TRICKS,
                    null,
                    null,
                    null,
                    TaskContract.TaskEntry.COLUMN_ID);
            setupAdapterByCursor(cursor);
        }
    }
}
