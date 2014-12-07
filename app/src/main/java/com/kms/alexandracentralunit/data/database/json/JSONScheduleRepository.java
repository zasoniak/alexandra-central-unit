package com.kms.alexandracentralunit.data.database.json;


import com.kms.alexandracentralunit.data.database.ScheduleRepository;
import com.kms.alexandracentralunit.data.model.ScheduledScene;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Mateusz Zasoński on 2014-11-11.
 */
public class JSONScheduleRepository implements ScheduleRepository {

    public static final String ID = "id";
    public static final String SCENE = "scene";
    public static final String TIME = "time";
    public static final String REPEAT_INTERVAL = "repeatInterval";
    public static final String CONDITIONS = "conditions";
    public static final String CONDITION_TYPE = "type";
    public static final String CONDITION_VALUE = "value";

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
        List<ScheduledScene> scheduledScenes = new ArrayList<ScheduledScene>();
        return scheduledScenes;
    }
}
