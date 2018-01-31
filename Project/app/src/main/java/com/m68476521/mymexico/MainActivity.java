package com.m68476521.mymexico;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.m68476521.mymexico.utilities.NetworkUtils;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        NetworkUtils networkUtils = new NetworkUtils();
        networkUtils.testConnection();
    }
}
