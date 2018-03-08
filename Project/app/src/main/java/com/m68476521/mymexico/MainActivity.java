package com.m68476521.mymexico;

import android.content.ContentValues;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import com.m68476521.mymexico.data.TaskContract;
import com.m68476521.mymexico.fragmentNewsDetails.NewsViewPagerFragment;
import com.m68476521.mymexico.fragmentmain.FragmentMainSmall;
import com.m68476521.mymexico.fragmentnews.NewsItemClickListener;

// This is the main activity that handles all the fragments
public class MainActivity extends AppCompatActivity implements NewsItemClickListener {
    private FragmentManager fragmentManager;

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

        if (getIntent().getExtras() != null) {
            String f_hint = getIntent().getExtras().get("f_hint").toString();
            String id = getIntent().getExtras().get("id").toString();
            String question = getIntent().getExtras().get("f_question").toString();
            String fake_a = getIntent().getExtras().get("f_fake_answer_a").toString();
            String fake_b = getIntent().getExtras().get("f_fake_answer_b").toString();
            String answer = getIntent().getExtras().get("f_valid_answer").toString();
            String image = getIntent().getExtras().get("f_url").toString();

            ContentValues newMessage = new ContentValues();
            newMessage.put(TaskContract.TaskEntry.COLUMN_ID, id);
            newMessage.put(TaskContract.TaskEntry.COLUMN_FCM_QUESTION, question);
            newMessage.put(TaskContract.TaskEntry.COLUMN_FCM_ANSWER, answer);
            newMessage.put(TaskContract.TaskEntry.COLUMN_FCM_FAKE_ANS_A, fake_a);
            newMessage.put(TaskContract.TaskEntry.COLUMN_FCM_FAKE_ANS_B, fake_b);
            newMessage.put(TaskContract.TaskEntry.COLUMN_FCM_HINT, f_hint);
            newMessage.put(TaskContract.TaskEntry.COLUMN_FCM_IMAGE, image);
            getContentResolver().insert(TaskContract.TaskEntry.CONTENT_URI_TRICKS, newMessage);
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

    private Fragment createFragment() {
        return new FragmentMainSmall();
    }

    @Override
    public void onlItemClick(String section, int pos, ImageView shareImageView, View view) {
        Fragment newsViewPagerFragment = NewsViewPagerFragment.newInstance(section, pos, this);

        fragmentManager
                .beginTransaction()
                .addSharedElement(shareImageView, ViewCompat.getTransitionName(shareImageView))
                .replace(R.id.main_frame, newsViewPagerFragment)
                .addToBackStack(null)
                .commit();
    }
}