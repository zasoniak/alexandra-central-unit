package com.kms.alexandracentralunit.data.model;


import java.util.UUID;


/**
 * Created by Mateusz Zasoński on 2014-11-05.
 */
public class ScheduledScene {

    public static final String EXTRA_ID = "com.kms.alexandracentralunit.EXTRA_ID";
    public static final String EXTRA_SCENE_ID = "com.kms.alexandracentralunit.EXTRA_SCENE_ID";
    public static final String EXTRA_TIME = "com.kms.alexandracentralunit.EXTRA_TIME";
    public static final String EXTRA_REPEAT_INTERVAL = "com.kms.alexandracentralunit.EXTRA_REPEAT_INTERVAL";
    public static final String EXTRA_CONDITIONS = "com.kms.alexandracentralunit.EXTRA_CONDITIONS";

    public UUID id;
    public UUID scene;
    public int system;
    public long time;
    public long repeatInterval;
    public String conditions;

    public ScheduledScene(UUID id, UUID scene, int system, long time, long repeatInterval, String conditions) {
        this.id = id;
        this.scene = scene;
        this.system = system;
        this.time = time;
        this.repeatInterval = repeatInterval;
        this.conditions = conditions;
    }

    public UUID getId() {
        return id;
    }

    public UUID getScene() {
        return scene;
    }

    public int getSystem() {
        return system;
    }

    public long getTime() {
        return time;
    }

    public long getRepeatInterval() {
        return repeatInterval;
    }

    public String getConditions() {
        return conditions;
    }
}
