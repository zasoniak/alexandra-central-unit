package com.kms.alexandracentralunit.data.model;


import android.text.format.Time;


/**
 * Created by Mateusz Zasoński on 2014-11-05.
 */
public class ScheduledScene {

    public static final String EXTRA_ID = "com.kms.alexandracentralunit.EXTRA_ID";
    public static final String EXTRA_SCENE_ID = "com.kms.alexandracentralunit.EXTRA_SCENE_ID";
    public static final String EXTRA_TIME = "com.kms.alexandracentralunit.EXTRA_TIME";
    public static final String EXTRA_REPEAT_INTERVAL = "com.kms.alexandracentralunit.EXTRA_REPEAT_INTERVAL";
    public static final String EXTRA_CONDITIONS = "com.kms.alexandracentralunit.EXTRA_CONDITIONS";

    public long id;
    public long scene;
    public long system;
    public Time time;
    public String repeatInterval;
    public String conditions;

    public ScheduledScene(long id, long scene, long system, Time time, String repeatInterval, String conditions) {
        this.id = id;
        this.scene = scene;
        this.system = system;
        this.time = time;
        this.repeatInterval = repeatInterval;
        this.conditions = conditions;
    }

    public long getId() {
        return id;
    }

    public long getScene() {
        return scene;
    }

    public long getSystem() {
        return system;
    }

    public Time getTime() {
        return time;
    }

    public String getRepeatInterval() {
        return repeatInterval;
    }

    public String getConditions() {
        return conditions;
    }
}
