package com.kms.alexandracentralunit.data.database.sqlite;


import android.content.Context;


/**
 * Created by Mateusz Zasoński on 2014-10-31.
 */
public class SQLiteActionRepository {

    private ConfigurationDatabaseHelper databaseHelper;

    public SQLiteActionRepository(Context context) {
        databaseHelper = ConfigurationDatabaseHelper.getInstance(context);
    }
}
