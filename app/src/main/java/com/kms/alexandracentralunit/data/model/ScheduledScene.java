package com.kms.alexandracentralunit.data.model;


import java.util.HashMap;


/**
 * Created by Mateusz Zasoński on 2014-11-05.
 */
public class ScheduledScene {

    public static final String EXTRA_ID = "com.kms.alexandracentralunit.EXTRA_ID";
    public static final String EXTRA_SCENE_ID = "com.kms.alexandracentralunit.EXTRA_SCENE_ID";
    public static final String EXTRA_TIME = "com.kms.alexandracentralunit.EXTRA_TIME";
    public static final String EXTRA_REPEAT_INTERVAL = "com.kms.alexandracentralunit.EXTRA_REPEAT_INTERVAL";
    public static final String EXTRA_CONDITIONS = "com.kms.alexandracentralunit.EXTRA_CONDITIONS";

    public String id;
    public String scene;
    public long time;
    public long repeatInterval;
    public HashMap<String, String> conditions = new HashMap<String, String>();

    public ScheduledScene(String id, String scene, long time, long repeatInterval, HashMap<String, String> conditions) {
        this.id = id;
        this.scene = scene;
        this.time = time;
        this.repeatInterval = repeatInterval;
        this.conditions = conditions;
    }

    public String getId() {
        return id;
    }

    public String getScene() {
        return scene;
    }

    public long getTime() {
        return time;
    }

    public long getRepeatInterval() {
        return repeatInterval;
    }

    public HashMap<String, String> getConditions() {
        return conditions;
    }
}
