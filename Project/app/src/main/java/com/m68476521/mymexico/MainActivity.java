package com.m68476521.mymexico;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.m68476521.mymexico.data.TaskContract;
import com.m68476521.mymexico.data.TaskModelLoader;
import com.m68476521.mymexico.fragmentmain.FragmentMainSmall;
import com.m68476521.mymexico.utilities.NetworkUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URL;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();
    private static final int TASK_LOADER_ID = 0;
    private Cursor cursor;
    FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResId(0));
        //getSupportLoaderManager().initLoader(TASK_LOADER_ID, null, this);

        Bundle extras = getIntent().getExtras();
        if (extras != null && extras.containsKey("f_question")) {
            Log.d("MIKE get Intent extra ", extras.getString("f_question"));
        }

        fragmentManager = getSupportFragmentManager();
        Fragment fragment = fragmentManager.findFragmentById(R.id.main_frame);

        if (fragment == null) {
            fragment = createFragment();
            fragmentManager.beginTransaction()
                    .add(R.id.main_frame, fragment)
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
}