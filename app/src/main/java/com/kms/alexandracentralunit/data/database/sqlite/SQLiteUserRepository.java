package com.kms.alexandracentralunit.data.database.sqlite;


import android.content.Context;

import com.kms.alexandracentralunit.data.database.UserRepository;
import com.kms.alexandracentralunit.data.model.User;

import java.util.List;


/**
 * Created by Mateusz Zasoński on 2014-10-31.
 */
public class SQLiteUserRepository implements UserRepository {

    private ConfigurationDatabaseHelper databaseHelper;

    public SQLiteUserRepository(Context context) {
        databaseHelper = ConfigurationDatabaseHelper.getInstance(context);
    }

    @Override
    public boolean add(User user) {
        return false;
    }

    @Override
    public boolean delete(User user) {
        return false;
    }

    @Override
    public boolean update(User user) {
        return false;
    }

    @Override
    public User find(long id) {
        return null;
    }

    @Override
    public List<User> getAll() {
        return null;
    }
}
