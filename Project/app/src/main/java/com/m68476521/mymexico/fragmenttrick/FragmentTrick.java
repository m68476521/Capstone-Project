package com.m68476521.mymexico.fragmenttrick;

import android.database.ContentObserver;
import android.database.Cursor;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.m68476521.mymexico.R;
import com.m68476521.mymexico.data.TaskContract;
import com.m68476521.mymexico.databinding.FragmentTrickBinding;
import com.m68476521.mymexico.fragmentfav.FragmentFavorite;

public class FragmentTrick extends Fragment {

    private FragmentTrickBinding fragmentTrickBinding;
    private Cursor cursor;
    private TrickAdapter trickAdapter;

    public FragmentTrick() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("MIKE on create", "TRicks");
        cursor = getContext().getContentResolver().query(TaskContract.TaskEntry.CONTENT_URI_TRICKS,
                null,
                null,
                null,
                TaskContract.TaskEntry.COLUMN_ID);

        if (cursor != null || cursor.getCount() != 0 ) {
            Log.d("Mike", "cursor in ont null at Tricks");
            for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                int descriptionIndex = cursor.getColumnIndex(TaskContract.TaskEntry.COLUMN_FCM_QUESTION);
                String name = cursor.getString(descriptionIndex);
                Log.d("MIKE", "data got it from tricks " + name);
            }
        }
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
        fragmentTrickBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_trick, container, false);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());

        fragmentTrickBinding.recyclerViewTrick.setHasFixedSize(true);

        trickAdapter = new TrickAdapter(getActivity(), new TrickAdapter.OnItemClicked() {
            @Override
            public void onItemClick(View view, int position) {
                Log.d("MIKE clickec on ima", Integer.toString(position));
                BottomSheetDialogFragment bottomSheetDialogFragment = new CustomBottomSheet();
                Bundle args = new Bundle();
                args.putString("bodyTextCompleted", "mike text");
                bottomSheetDialogFragment.setArguments(args);
                bottomSheetDialogFragment.show(getActivity().getSupportFragmentManager(), bottomSheetDialogFragment.getTag());
            }
        });

        fragmentTrickBinding.recyclerViewTrick.setAdapter(trickAdapter);

        fragmentTrickBinding.recyclerViewTrick.setLayoutManager(linearLayoutManager);

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
            Log.d("MIKE", "on CreateObserveTrickr");
        }

        @Override
        public void onChange(boolean selfChange) {
            super.onChange(selfChange);
            Log.d("MIKE", "onChangeAtrick");
        }


        @Override
        public void onChange(boolean selfChange, Uri uri) {
            super.onChange(selfChange, uri);
            Log.d("MIKE", "onChangeTrick");
            cursor = getContext().getContentResolver().query(TaskContract.TaskEntry.CONTENT_URI_TRICKS,
                    null,
                    null,
                    null,
                    TaskContract.TaskEntry.COLUMN_ID);
            setupAdapterByCursor(cursor);
        }
    }
}
