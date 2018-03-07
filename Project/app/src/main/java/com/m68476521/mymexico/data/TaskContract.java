package com.m68476521.mymexico.data;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Contract base used for Content Provider
 */

public class TaskContract {

    public static final String AUTHORITY = "com.m68476521.mymexico";
    //base content URI
    private static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);

    public static final String PATH_NEWS = "news";
    public static final String PATH_TRICKS = "tricks";
    public static final String PATH_FAVORITES = "favorites";


    public static final class TaskEntry implements BaseColumns {

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_NEWS).build();

        public static final Uri CONTENT_URI_TRICKS =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_TRICKS).build();

        public static final Uri CONTENT_URI_FAVORITES =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_FAVORITES).build();

        // Task table and column names
        public static final String TABLE_NAME = "news";

        public static final String JSON_ROOT = "questions";

        public static final String COLUMN_ID = "id";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_DESCRIPTION = "description";
        public static final String COLUMN_SHORT_DESCRIPTION = "short_description";
        public static final String COLUMN_IMAGE = "url";
        public static final String COLUMN_LAST_MODIFIED = "lastModified";
        public static final String COLUMN_CATEGORY = "category";

        // this is for handling FCM
        public static final String TABLE_NAME_TRICKS = "tricks";

        public static final String COLUMN_FCM_QUESTION = "f_question";
        public static final String COLUMN_FCM_ANSWER = "f_valid_answer";
        public static final String COLUMN_FCM_FAKE_ANS_A = "f_fake_answer_a";
        public static final String COLUMN_FCM_FAKE_ANS_B = "f_fake_answer_b";
        public static final String COLUMN_FCM_HINT = "f_hint";

        public static final String TABLE_NAME_FAVORITES = "favorites";
    }
}
