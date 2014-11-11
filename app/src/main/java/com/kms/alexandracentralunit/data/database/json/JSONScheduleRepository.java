package com.kms.alexandracentralunit.data.database.json;


import com.kms.alexandracentralunit.data.database.ScheduleRepository;
import com.kms.alexandracentralunit.data.model.ScheduledScene;

import java.util.List;


/**
 * Created by Mateusz Zasoński on 2014-11-11.
 */
public class JSONScheduleRepository implements ScheduleRepository {

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
