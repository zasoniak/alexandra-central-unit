package com.kms.alexandracentralunit.data.database.sqlite;


import android.content.Context;

import com.kms.alexandracentralunit.data.database.ActionRepository;
import com.kms.alexandracentralunit.data.model.Action;

import java.util.List;
import java.util.UUID;


/**
 * Created by Mateusz Zasoński on 2014-10-31.
 */
public class SQLiteActionRepository implements ActionRepository {

    // Actions table columns names
    public static final String KEY_ACTION_SCENE = "scene_id";
    public static final String KEY_ACTION_GADGET = "gadget_id";
    public static final String KEY_ACTION_CODE = "action";
    public static final String KEY_ACTION_OFFSET = "offset";
    public static final String KEY_ACTION_CREATED = "created_at";
    public static final String KEY_ACTION_UPDATED = "updated_at";
    // Actions table column array
    private static final String[] TABLE_COLUMNS = {KEY_ACTION_SCENE, KEY_ACTION_GADGET,
                                                   KEY_ACTION_CODE, KEY_ACTION_OFFSET,
                                                   KEY_ACTION_CREATED, KEY_ACTION_UPDATED};
    public static final String KEY_ACTION_SCENE_TYPE = "TEXT PRIMARY KEY";
    private static final String TAG = "SQLiteActionRepository";
    private static final String TABLE_NAME = "actions_scenes";
    // Predefined SQL statements
    public static final String SQL_DROP_TABLE = "DROP TABLE IF EXISTS "+TABLE_NAME;
    // Actions table columns types
    private static final String COMMA_SEP = ", ";
    private static final String KEY_ACTION_GADGET_TYPE = "TEXT PRIMARY KEY";
    private static final String KEY_ACTION_CODE_TYPE = "INTEGER PRIMARY KEY";
    private static final String KEY_ACTION_OFFSET_TYPE = "INTEGER";
    private static final String KEY_ACTION_CREATED_TYPE = "TEXT";
    private static final String KEY_ACTION_UPDATED_TYPE = "TEXT";
    public static final String SQL_CREATE_TABLE = "CREATE TABLE "+TABLE_NAME+" ("+
            KEY_ACTION_SCENE+" "+KEY_ACTION_SCENE_TYPE+COMMA_SEP+
            KEY_ACTION_GADGET+" "+KEY_ACTION_GADGET_TYPE+COMMA_SEP+
            KEY_ACTION_CODE+" "+KEY_ACTION_CODE_TYPE+COMMA_SEP+
            KEY_ACTION_OFFSET+" "+KEY_ACTION_OFFSET_TYPE+COMMA_SEP+
            KEY_ACTION_CREATED+" "+KEY_ACTION_CREATED_TYPE+COMMA_SEP+
            KEY_ACTION_UPDATED+" "+KEY_ACTION_UPDATED_TYPE+COMMA_SEP+")";

    // class implementation
    private ConfigurationDatabaseHelper databaseHelper;

    public SQLiteActionRepository(Context context) {
        databaseHelper = ConfigurationDatabaseHelper.getInstance(context);
    }

    @Override
    public boolean add(Action action) {
        return false;
    }

    @Override
    public boolean delete(Action action) {
        return false;
    }

    @Override
    public boolean update(Action action) {
        return false;
    }

    @Override
    public List<Action> getAllByScene(UUID sceneID) {
        return null;
    }
}
