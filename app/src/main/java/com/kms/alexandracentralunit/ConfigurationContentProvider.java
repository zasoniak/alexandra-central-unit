package com.kms.alexandracentralunit;


import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;


/**
 *
 */
public class ConfigurationContentProvider extends ContentProvider {

    private static final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    private ConfigurationDatabaseHelper databaseHelper;
    private SQLiteDatabase database;

    public ConfigurationContentProvider() {
    }

    @Override
    public boolean onCreate() {
        databaseHelper = ConfigurationDatabaseHelper.getInstance(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
        database = databaseHelper.openDatabase();

        switch(ConfigurationContract.uriMatcher.match(uri))
        {
            case ConfigurationContract.USERS:
                queryBuilder.setTables(ConfigurationContract.USERS_TABLE);
                break;
            case ConfigurationContract.SYSTEM_USERS:
                queryBuilder.setTables(ConfigurationContract.SYSTEM_USERS_TABLE);
                break;
            case ConfigurationContract.SYSTEMS:
                queryBuilder.setTables(ConfigurationContract.SYSTEMS_TABLE);
                break;
            case ConfigurationContract.ROOMS:
                queryBuilder.setTables(ConfigurationContract.ROOMS_TABLE);
                break;
            case ConfigurationContract.GADGETS:
                queryBuilder.setTables(ConfigurationContract.GADGETS_TABLE);
                break;
            case ConfigurationContract.SCENES:
                queryBuilder.setTables(ConfigurationContract.SCENES_TABLE);
                break;
            case ConfigurationContract.SUBSCENES:
                queryBuilder.setTables(ConfigurationContract.SUBSCENES_TABLE);
                break;
            case ConfigurationContract.ACTIONS:
                queryBuilder.setTables(ConfigurationContract.ACTIONS_TABLE);
                break;
            case ConfigurationContract.TRIGGERS:
                queryBuilder.setTables(ConfigurationContract.TRIGGERS_TABLE);
                break;
        }

        Cursor cursor = queryBuilder.query(database, projection, selection, selectionArgs, null, null, sortOrder);
        /**
         * register to watch a content URI for changes
         */
        cursor.setNotificationUri(getContext().getContentResolver(), uri);

        return cursor;

    }

    @Override
    public String getType(Uri uri) {
        // TODO: Implement this to handle requests for the MIME type of the data
        // at the given URI.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        //TODO: switch for different tables
        long rowID;
        Uri _uri;
        switch(ConfigurationContract.uriMatcher.match(uri))
        {
            case ConfigurationContract.USERS:
                database = databaseHelper.openDatabase();
                rowID = database.insert(ConfigurationContract.USERS_TABLE, "", values);
                _uri = ContentUris.withAppendedId(ConfigurationContract.URI, rowID);
                getContext().getContentResolver().notifyChange(_uri, null);
                break;
            case ConfigurationContract.SYSTEM_USERS:
                database = databaseHelper.openDatabase();
                rowID = database.insert(ConfigurationContract.SYSTEM_USERS_TABLE, "", values);
                _uri = ContentUris.withAppendedId(ConfigurationContract.URI, rowID);
                getContext().getContentResolver().notifyChange(_uri, null);
                break;

        }
        database = databaseHelper.openDatabase();
        rowID = database.insert(uri.getPathSegments().get(0), "", values);
        _uri = ContentUris.withAppendedId(ConfigurationContract.URI, rowID);
        getContext().getContentResolver().notifyChange(_uri, null);
        return _uri;

    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        // Implement this to handle requests to delete one or more rows.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        // TODO: Implement this to handle requests to update one or more rows.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
