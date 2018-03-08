package com.m68476521.mymexico.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * This helper is used for the content Provider
 */

class TaskDbHelper extends SQLiteOpenHelper {
    // The name of the database
    private static final String DATABASE_NAME = "newsDb.db";

    // If you change the database schema, you must increment the database version
    private static final int VERSION = 1;

    // Constructor
    TaskDbHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create tasks table (careful to follow SQL formatting rules)
        final String CREATE_TABLE = "CREATE TABLE " + TaskContract.TaskEntry.TABLE_NAME + " (" +
                TaskContract.TaskEntry._ID + " INTEGER PRIMARY KEY, " +
                TaskContract.TaskEntry.COLUMN_ID + " TEXT NOT NULL, " +
                TaskContract.TaskEntry.COLUMN_NAME + " TEXT NOT NULL, " +
                TaskContract.TaskEntry.COLUMN_DESCRIPTION + " TEXT NOT NULL, " +
                TaskContract.TaskEntry.COLUMN_SHORT_DESCRIPTION + " TEXT NOT NULL, " +
                TaskContract.TaskEntry.COLUMN_CATEGORY + " TEXT NOT NULL, " +
                TaskContract.TaskEntry.COLUMN_IMAGE + " TEXT NOT NULL);";

        final String CREATE_TABLE_TRICKS = "CREATE TABLE " + TaskContract.TaskEntry.TABLE_NAME_TRICKS + " (" +
                TaskContract.TaskEntry._ID + " INTEGER PRIMARY KEY, " +
                TaskContract.TaskEntry.COLUMN_ID + " TEXT NOT NULL, " +
                TaskContract.TaskEntry.COLUMN_FCM_QUESTION + " TEXT NOT NULL, " +
                TaskContract.TaskEntry.COLUMN_FCM_ANSWER + " TEXT NOT NULL, " +
                TaskContract.TaskEntry.COLUMN_FCM_FAKE_ANS_A + " TEXT NOT NULL, " +
                TaskContract.TaskEntry.COLUMN_FCM_FAKE_ANS_B + " TEXT NOT NULL, " +
                TaskContract.TaskEntry.COLUMN_FCM_HINT + " TEXT NOT NULL);";

        final String CREATE_TABLE_FAVORITES = "CREATE TABLE " + TaskContract.TaskEntry.TABLE_NAME_FAVORITES + " (" +
                TaskContract.TaskEntry._ID + " INTEGER PRIMARY KEY, " +
                TaskContract.TaskEntry.COLUMN_ID + " TEXT NOT NULL, " +
                TaskContract.TaskEntry.COLUMN_NAME + " TEXT NOT NULL, " +
                TaskContract.TaskEntry.COLUMN_DESCRIPTION + " TEXT NOT NULL, " +
                TaskContract.TaskEntry.COLUMN_SHORT_DESCRIPTION + " TEXT NOT NULL, " +
                TaskContract.TaskEntry.COLUMN_CATEGORY + " TEXT NOT NULL, " +
                TaskContract.TaskEntry.COLUMN_IMAGE + " TEXT NOT NULL);";

        db.execSQL(CREATE_TABLE_TRICKS);
        db.execSQL(CREATE_TABLE);
        db.execSQL(CREATE_TABLE_FAVORITES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TaskContract.TaskEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + TaskContract.TaskEntry.TABLE_NAME_TRICKS);
        db.execSQL("DROP TABLE IF EXISTS " + TaskContract.TaskEntry.TABLE_NAME_FAVORITES);
        onCreate(db);
    }
}
