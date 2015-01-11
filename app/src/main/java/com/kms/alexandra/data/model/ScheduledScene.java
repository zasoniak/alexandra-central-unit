package com.kms.alexandra.data.model;


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
    public static final String HOUR = "hour";
    public static final String MINUTES = "minutes";
    public static final String DAYS_OF_WEEK = "daysOfWeek";
    public static final String CONDITIONS = "conditions";
    public static final String CONDITION_TYPE = "type";
    public static final String CONDITION_VALUE = "value";

    public static final String EXTRA_ID = "com.kms.alexandra.EXTRA_ID";
    public static final String EXTRA_SCENE_ID = "com.kms.alexandra.EXTRA_SCENE_ID";
    public static final String EXTRA_MINUTES = "com.kms.alexandra.EXTRA_MINUTES";
    public static final String EXTRA_HOUR = "com.kms.alexandra.EXTRA_HOUR";
    public static final String EXTRA_DAYS_OF_WEEK = "com.kms.alexandra.EXTRA_DAYS_OF_WEEK";
    public static final String EXTRA_CONDITIONS = "com.kms.alexandra.EXTRA_CONDITIONS";

    public String id;
    public String sceneId;
    public int hour;
    public int minutes;
    public boolean[] daysOfWeek;
    public HashMap<String, String> conditions = new HashMap<String, String>();

    public ScheduledScene(String id, String sceneId, int hour, int minutes, boolean[] daysOfWeek, HashMap<String, String> conditions) {
        this.id = id;
        this.sceneId = sceneId;
        this.hour = hour;
        this.minutes = minutes;
        this.daysOfWeek = daysOfWeek;
        this.conditions = conditions;
    }

    public ScheduledScene(String sceneId, int hour, int minutes, boolean[] daysOfWeek, HashMap<String, String> conditions) {
        this.sceneId = sceneId;
        this.hour = hour;
        this.minutes = minutes;
        this.daysOfWeek = daysOfWeek;
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

    public HashMap<String, String> getConditions() {
        return conditions;
    }

    public int getHour() {
        return hour;
    }

    public int getMinutes() {
        return minutes;
    }

    public boolean[] getDaysOfWeek() {
        return daysOfWeek;
    }

}
