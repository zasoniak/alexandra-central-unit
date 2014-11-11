package com.kms.alexandracentralunit.data.database;


import com.kms.alexandracentralunit.data.model.ScheduledScene;

import java.util.List;


/**
 * Created by Mateusz Zasoński on 2014-11-05.
 */
public interface ScheduleRepository {

    public boolean add(ScheduledScene scheduledScene);
    public boolean delete(ScheduledScene scheduledScene);
    public boolean update(ScheduledScene scheduledScene);
    public List<ScheduledScene> getAll();
}
