package com.m68476521.mymexico.data;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Contract base used for Content Provider
 */

public class TaskContract {

    public static final String AUTHORITY = "com.ackila.mymexico";
    //base content URI
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);

    public static final String PATH_NEWS = "news";

    public static final class TaskEntry implements BaseColumns {

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_NEWS).build();

        // Task table and column names
        public static final String TABLE_NAME = "news";

        public static final String JSON_ROOT = "questions";

        public static final String COLUMN_ID = "id";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_DESCRIPTION = "description";
        public static final String COLUMN_IMAGE = "url";
        public static final String COLUMN_LAST_MODIFIED = "lastModified";
    }
}
