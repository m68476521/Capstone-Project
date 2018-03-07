package com.m68476521.mymexico.fragmentnews;

import android.view.View;
import android.widget.ImageView;

/**
 * This is an interface that is principal method is an onClick from the listener
 */

public interface NewsItemClickListener {
    void onlItemClick(String section, int pos, ImageView shareImageView, View view
    );
}
