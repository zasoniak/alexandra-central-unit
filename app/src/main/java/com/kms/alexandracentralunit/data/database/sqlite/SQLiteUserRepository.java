package com.kms.alexandracentralunit.data.database.sqlite;


import android.content.Context;

import com.kms.alexandracentralunit.data.database.UserRepository;


/**
 * Created by Mateusz Zasoński on 2014-10-31.
 */
public class SQLiteUserRepository implements UserRepository {

    private ConfigurationDatabaseHelper databaseHelper;

    public SQLiteUserRepository(Context context) {
        databaseHelper = ConfigurationDatabaseHelper.getInstance(context);
    }
}
