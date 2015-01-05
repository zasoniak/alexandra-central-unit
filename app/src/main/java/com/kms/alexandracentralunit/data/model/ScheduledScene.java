package com.kms.alexandracentralunit.data.model;


import java.util.HashMap;


/**
 * Created by Mateusz Zasoński on 2014-11-05.
 *
 * @author Mateusz Zasoński
 * @version 0.1
 */
public class ScheduledScene {

    public static final String ID = "id";
    public static final String SCENE = "scene";
    public static final String TIME = "time";
    public static final String REPEAT_INTERVAL = "repeatInterval";
    public static final String CONDITIONS = "conditions";
    public static final String CONDITION_TYPE = "type";
    public static final String CONDITION_VALUE = "value";

    public static final String EXTRA_ID = "com.kms.alexandracentralunit.EXTRA_ID";
    public static final String EXTRA_SCENE_ID = "com.kms.alexandracentralunit.EXTRA_SCENE_ID";
    public static final String EXTRA_TIME = "com.kms.alexandracentralunit.EXTRA_TIME";
    public static final String EXTRA_REPEAT_INTERVAL = "com.kms.alexandracentralunit.EXTRA_REPEAT_INTERVAL";
    public static final String EXTRA_CONDITIONS = "com.kms.alexandracentralunit.EXTRA_CONDITIONS";

    public String id;
    public String sceneId;
    public long time;
    public long repeatInterval;
    public HashMap<String, String> conditions = new HashMap<String, String>();

    public ScheduledScene(String id, String sceneId, long time, long repeatInterval, HashMap<String, String> conditions) {
        this.id = id;
        this.sceneId = sceneId;
        this.time = time;
        this.repeatInterval = repeatInterval;
        this.conditions = conditions;
    }

    public ScheduledScene(String sceneId, long time, long repeatInterval, HashMap<String, String> conditions) {
        this.sceneId = sceneId;
        this.time = time;
        this.repeatInterval = repeatInterval;
        this.conditions = conditions;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getScene() {
        return this.sceneId;
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
