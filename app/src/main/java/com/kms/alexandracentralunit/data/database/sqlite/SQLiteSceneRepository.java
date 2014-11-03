package com.kms.alexandracentralunit.data.database.sqlite;


import android.content.Context;

import com.kms.alexandracentralunit.data.SceneBuilder;
import com.kms.alexandracentralunit.data.database.SceneRepository;
import com.kms.alexandracentralunit.data.model.Scene;

import java.util.List;
import java.util.UUID;


/**
 * Created by Mateusz Zasoński on 2014-10-31.
 */
public class SQLiteSceneRepository implements SceneRepository {

    // Triggers table columns names
    public static final String KEY_SCENE_ID = "_id";
    public static final String KEY_SCENE_SYSTEM = "system_id";
    public static final String KEY_SCENE_NAME = "name";
    public static final String KEY_SCENE_CREATED = "created_at";
    public static final String KEY_SCENE_UPDATED = "updated_at";
    public static final String KEY_SCENE_CREATED_BY = "created_by";
    // Triggers table column array
    private static final String[] TABLE_COLUMNS_SCENES = {KEY_SCENE_ID, KEY_SCENE_SYSTEM,
                                                          KEY_SCENE_NAME, KEY_SCENE_CREATED,
                                                          KEY_SCENE_UPDATED, KEY_SCENE_CREATED_BY};
    public static final String KEY_SCENE_ID_TYPE = "TEXT";
    // Triggers table columns names
    public static final String KEY_SUBSCENE_SCENE = "scene_id";
    public static final String KEY_SUBSCENE_SUBSCENE = "subscene_id";
    public static final String KEY_SUBSCENE_OFFSET = "offset";
    public static final String KEY_SUBSCENE_CREATED = "created_at";
    // Triggers table column array
    private static final String[] TABLE_COLUMNS_SUBSCENES = {KEY_SUBSCENE_SCENE,
                                                             KEY_SUBSCENE_SUBSCENE,
                                                             KEY_SUBSCENE_OFFSET,
                                                             KEY_SUBSCENE_CREATED};
    // Triggers table columns types
    public static final String KEY_SUBSCENE_SCENE_TYPE = "TEXT";
    /**
     * scenes table
     */
    private static final String TAG = "SQLiteSceneRepository";
    private static final String TABLE_NAME_SCENES = "scenes";
    public static final String SQL_DROP_TABLE_SCENES = "DROP TABLE IF EXISTS "+TABLE_NAME_SCENES;
    // Triggers table columns types
    private static final String COMMA_SEP = ", ";
    private static final String KEY_SCENE_SYSTEM_TYPE = "TEXT";
    private static final String KEY_SCENE_NAME_TYPE = "TEXT ";
    private static final String KEY_TRIGGER_CREATED_TYPE = "DATETIME DEFAULT CURRENT_TIMESTAMP";
    private static final String KEY_SCENE_UPDATED_TYPE = "DATETIME DEFAULT CURRENT_TIMESTAMP";
    private static final String KEY_SCENE_CREATED_BY_TYPE = "INTEGER";
    // Predefined SQL statements
    public static final String SQL_CREATE_TABLE_SCENES = "CREATE TABLE "+TABLE_NAME_SCENES+" ("+
            KEY_SCENE_ID+" "+KEY_SCENE_ID_TYPE+COMMA_SEP+
            KEY_SCENE_SYSTEM+" "+KEY_SCENE_SYSTEM_TYPE+COMMA_SEP+
            KEY_SCENE_NAME+" "+KEY_SCENE_NAME_TYPE+COMMA_SEP+
            KEY_SCENE_CREATED+" "+KEY_TRIGGER_CREATED_TYPE+COMMA_SEP+
            KEY_SCENE_UPDATED+" "+KEY_SCENE_UPDATED_TYPE+COMMA_SEP+
            KEY_SCENE_CREATED_BY+" "+KEY_SCENE_CREATED_BY_TYPE+")";
    /**
     * subscenes table
     */
    private static final String TABLE_NAME_SUBSCENES = "scenes_subscenes";
    public static final String SQL_DROP_TABLE_SUBSCENES = "DROP TABLE IF EXISTS "+TABLE_NAME_SUBSCENES;
    private static final String KEY_SUBSCENE_SUBSCENE_TYPE = "TEXT";
    private static final String KEY_SUBSCENE_OFFSET_TYPE = "INTEGER ";
    private static final String KEY_SUBSCENE_CREATED_TYPE = "DATETIME DEFAULT CURRENT_TIMESTAMP";
    // Predefined SQL statements
    public static final String SQL_CREATE_TABLE_SUBSCENES = "CREATE TABLE "+TABLE_NAME_SUBSCENES+" ("+
            KEY_SUBSCENE_SCENE+" "+KEY_SUBSCENE_SCENE_TYPE+COMMA_SEP+
            KEY_SUBSCENE_SUBSCENE+" "+KEY_SUBSCENE_SUBSCENE_TYPE+COMMA_SEP+
            KEY_SUBSCENE_OFFSET+" "+KEY_SUBSCENE_OFFSET_TYPE+COMMA_SEP+
            KEY_SUBSCENE_CREATED+" "+KEY_SUBSCENE_CREATED_TYPE+")";
    /**
     * class implementation
     */
    private ConfigurationDatabaseHelper databaseHelper;
    private Context context;

    public SQLiteSceneRepository(Context context) {
        this.databaseHelper = ConfigurationDatabaseHelper.getInstance(context);
        this.context = context;
    }

    @Override
    public boolean add(Scene scene) {
        return false;
    }

    @Override
    public boolean delete(Scene scene) {
        return false;
    }

    @Override
    public boolean update(Scene scene) {
        return false;
    }

    @Override
    public Scene find(UUID id) {
        SceneBuilder builder = new SceneBuilder(this.context);


        return null;
    }

    @Override
    public List<Scene> getAll() {
        return null;
    }

    @Override
    public List<Scene> getAllByRoom(UUID roomID) {
        return null;
    }

    @Override
    public List<Scene> getAllSubscenes(UUID id) {
        return null;
    }
}
