package com.m68476521.mymexico.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import static com.m68476521.mymexico.data.TaskContract.TaskEntry.CONTENT_URI;
import static com.m68476521.mymexico.data.TaskContract.TaskEntry.CONTENT_URI_FAVORITES;
import static com.m68476521.mymexico.data.TaskContract.TaskEntry.CONTENT_URI_TRICKS;
import static com.m68476521.mymexico.data.TaskContract.TaskEntry.TABLE_NAME;
import static com.m68476521.mymexico.data.TaskContract.TaskEntry.TABLE_NAME_FAVORITES;
import static com.m68476521.mymexico.data.TaskContract.TaskEntry.TABLE_NAME_TRICKS;

/**
 * Content provider manages access to the media database
 */

public class TaskContentProvider extends ContentProvider {
    private static final int NEWS = 100;
    private static final int TRICKS = 200;
    private static final int FAVORITES = 300;
    private static final int FAV_ITEM_WITH_ID = 301;
    private static final UriMatcher uriMatcher = buildUriMatcher();

    private static UriMatcher buildUriMatcher() {
        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(TaskContract.AUTHORITY, TaskContract.PATH_NEWS, NEWS);
        uriMatcher.addURI(TaskContract.AUTHORITY, TaskContract.PATH_TRICKS, TRICKS);
        uriMatcher.addURI(TaskContract.AUTHORITY, TaskContract.PATH_FAVORITES, FAVORITES);
        uriMatcher.addURI(TaskContract.AUTHORITY, TaskContract.PATH_FAVORITES + "/#", FAV_ITEM_WITH_ID);
        return uriMatcher;
    }

    private TaskDbHelper taskDbHelper;

    @Override
    public boolean onCreate() {
        Context context = getContext();
        taskDbHelper = new TaskDbHelper(context);
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        final SQLiteDatabase db = taskDbHelper.getReadableDatabase();
        int match = uriMatcher.match(uri);
        Cursor retCursor;

        switch (match) {
            case NEWS:
                retCursor = db.query(TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            case TRICKS:
                retCursor = db.query(TABLE_NAME_TRICKS,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            case FAVORITES:
                retCursor = db.query(TABLE_NAME_FAVORITES,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            case FAV_ITEM_WITH_ID:
                String id = uri.getPathSegments().get(1);
                retCursor = db.query(TABLE_NAME_FAVORITES,
                        projection,
                        "id= '" + id + "'",
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        retCursor.setNotificationUri(getContext().getContentResolver(), uri);
        return retCursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        final SQLiteDatabase db = taskDbHelper.getWritableDatabase();
        int match = uriMatcher.match(uri);
        Uri returnUri;

        switch (match) {
            case NEWS:
                long id = db.insert(TABLE_NAME, null, values);
                if (id > 0) {
                    returnUri = ContentUris.withAppendedId(CONTENT_URI, id);
                } else {
                    throw new android.database.SQLException("Failed to insert raw " + uri);
                }
                break;
            case TRICKS:
                long id_ing = db.insert(TABLE_NAME_TRICKS, null, values);
                if (id_ing > 0) {
                    returnUri = ContentUris.withAppendedId(CONTENT_URI_TRICKS, id_ing);
                } else {
                    throw new android.database.SQLException("Failed to insert raw " + uri);
                }
                break;
            case FAVORITES:
                long id_fav = db.insert(TABLE_NAME_FAVORITES, null, values);
                if (id_fav > 0) {
                    returnUri = ContentUris.withAppendedId(CONTENT_URI_FAVORITES, id_fav);
                } else {
                    throw new android.database.SQLException("Failed to insert raw " + uri);
                }
                break;
            default:
                throw new UnsupportedOperationException("Unknown URI: " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return returnUri;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        final SQLiteDatabase db = taskDbHelper.getWritableDatabase();
        int match = uriMatcher.match(uri);
        int newsDeleted;
        switch (match) {
            case FAV_ITEM_WITH_ID:
                String id = uri.getPathSegments().get(1);
                newsDeleted = db.delete(TABLE_NAME_FAVORITES,
                        "id=?", new String[]{id});
                break;
            default:
                throw new UnsupportedOperationException("Unknown Uri: " + uri);
        }
        if (newsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return newsDeleted;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }
}
