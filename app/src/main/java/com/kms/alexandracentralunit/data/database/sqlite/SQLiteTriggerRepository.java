package com.kms.alexandracentralunit.data.database.sqlite;


import com.kms.alexandracentralunit.data.database.TriggerRepository;


/**
 * Created by Mateusz Zasoński on 2014-11-03.
 */
public class SQLiteTriggerRepository implements TriggerRepository {

    // Triggers table columns names
    public static final String KEY_TRIGGER_SCENE = "scene_id";
    public static final String KEY_TRIGGER_GADGET = "gadget_id";
    public static final String KEY_TRIGGER_ACTION = "action";
    private static final String[] TABLE_COLUMNS_FOR_OBJECT = {KEY_TRIGGER_GADGET,
                                                              KEY_TRIGGER_ACTION};
    public static final String KEY_TRIGGER_CREATED = "created_at";
    // Triggers table column array
    private static final String[] TABLE_COLUMNS = {KEY_TRIGGER_SCENE, KEY_TRIGGER_GADGET,
                                                   KEY_TRIGGER_ACTION, KEY_TRIGGER_CREATED};
    public static final String KEY_TRIGGER_SCENE_TYPE = "TEXT";
    private static final String TAG = "SQLiteTriggerRepository";
    private static final String TABLE_NAME = "scenes_triggers";
    public static final String SQL_DROP_TABLE = "DROP TABLE IF EXISTS "+TABLE_NAME;
    // Triggers table columns types
    private static final String COMMA_SEP = ", ";
    private static final String KEY_TRIGGER_GADGET_TYPE = "TEXT";
    private static final String KEY_TRIGGER_TRIGGER_TYPE = "TEXT ";
    private static final String KEY_TRIGGER_CREATED_TYPE = "DATETIME DEFAULT CURRENT_TIMESTAMP";
    // Predefined SQL statements
    public static final String SQL_CREATE_TABLE = "CREATE TABLE "+TABLE_NAME+" ("+
            KEY_TRIGGER_SCENE+" "+KEY_TRIGGER_SCENE_TYPE+COMMA_SEP+
            KEY_TRIGGER_GADGET+" "+KEY_TRIGGER_GADGET_TYPE+COMMA_SEP+
            KEY_TRIGGER_ACTION+" "+KEY_TRIGGER_TRIGGER_TYPE+COMMA_SEP+
            KEY_TRIGGER_CREATED+" "+KEY_TRIGGER_CREATED_TYPE+")";

}
