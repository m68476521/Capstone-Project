package com.m68476521.mymexico.data;

import android.provider.BaseColumns;

/**
 * Created by mike on 1/31/18.
 */

public class TaskContract {
    public static final class TaskEntry implements BaseColumns {

        public static final String JSON_ROOT = "questions";

        public static final String COLUMN_ID = "id";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_DESCRIPTION = "description";
        public static final String COLUMN_IMAGE = "url";
        public static final String COLUMN_LAST_MODIFIED ="lastModified";
    }
}
