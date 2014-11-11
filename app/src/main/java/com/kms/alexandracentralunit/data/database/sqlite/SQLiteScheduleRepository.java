package com.kms.alexandracentralunit.data.database.sqlite;


import android.content.Context;

import com.kms.alexandracentralunit.CoreService;
import com.kms.alexandracentralunit.data.database.ScheduleRepository;
import com.kms.alexandracentralunit.data.model.ScheduledScene;

import java.util.List;


/**
 * Created by Mateusz Zasoński on 2014-11-05.
 */
public class SQLiteScheduleRepository implements ScheduleRepository {

    private ConfigurationDatabaseHelper databaseHelper;

    public SQLiteScheduleRepository(Context context) {
        databaseHelper = ConfigurationDatabaseHelper.getInstance(CoreService.getContext());

    }

    @Override
    public boolean add(ScheduledScene scheduledScene) {
        return false;
    }

    @Override
    public boolean delete(ScheduledScene scheduledScene) {
        return false;
    }

    @Override
    public boolean update(ScheduledScene scheduledScene) {
        return false;
    }

    @Override
    public List<ScheduledScene> getAll() {
        return null;
    }
}
