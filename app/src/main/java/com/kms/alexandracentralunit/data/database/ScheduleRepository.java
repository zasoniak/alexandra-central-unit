package com.kms.alexandracentralunit.data.database;


import com.kms.alexandracentralunit.data.model.ScheduledScene;

import java.util.List;


/**
 * Created by Mateusz Zasoński on 2014-11-05.
 */
public interface ScheduleRepository {

    public boolean add();
    public boolean delete();
    public boolean update();
    public List<ScheduledScene> getAll();
}
