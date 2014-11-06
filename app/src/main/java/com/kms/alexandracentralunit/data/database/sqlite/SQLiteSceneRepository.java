package com.kms.alexandracentralunit.data.database.sqlite;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteQueryBuilder;
import android.util.Log;

import com.kms.alexandracentralunit.data.SceneBuilder;
import com.kms.alexandracentralunit.data.database.ActionRepository;
import com.kms.alexandracentralunit.data.database.SceneRepository;
import com.kms.alexandracentralunit.data.database.TriggerRepository;
import com.kms.alexandracentralunit.data.model.Action;
import com.kms.alexandracentralunit.data.model.Scene;
import com.kms.alexandracentralunit.data.model.SceneComponent;
import com.kms.alexandracentralunit.data.model.Trigger;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


/**
 * Created by Mateusz Zasoński on 2014-10-31.
 */
public class SQLiteSceneRepository implements SceneRepository {


    public static final String KEY_SCENE_ID = "_id";
    public static final String KEY_SCENE_SYSTEM = "system_id";
    public static final String KEY_SCENE_NAME = "name";
    public static final String KEY_SCENE_CREATED = "created_at";
    public static final String KEY_SCENE_UPDATED = "updated_at";
    public static final String KEY_SCENE_CREATED_BY = "created_by";
    private static final String[] TABLE_COLUMNS_SCENES = {KEY_SCENE_ID, KEY_SCENE_SYSTEM,
                                                          KEY_SCENE_NAME, KEY_SCENE_CREATED,
                                                          KEY_SCENE_UPDATED, KEY_SCENE_CREATED_BY};
    public static final String KEY_SCENE_ID_TYPE = "TEXT";
    public static final String KEY_SUBSCENE_SCENE = "scene_id";
    public static final String KEY_SUBSCENE_SUBSCENE = "subscene_id";
    public static final String KEY_SUBSCENE_OFFSET = "offset";
    public static final String KEY_SUBSCENE_CREATED = "created_at";
    private static final String[] TABLE_COLUMNS_SUBSCENES = {KEY_SUBSCENE_SCENE,
                                                             KEY_SUBSCENE_SUBSCENE,
                                                             KEY_SUBSCENE_OFFSET,
                                                             KEY_SUBSCENE_CREATED};
    public static final String KEY_SUBSCENE_SCENE_TYPE = "TEXT";
    private static final String TAG = "SQLiteSceneRepository";
    private static final String TABLE_NAME_SCENES = "scenes";
    public static final String SQL_DROP_TABLE_SCENES = "DROP TABLE IF EXISTS "+TABLE_NAME_SCENES;
    private static final String TABLE_NAME_SUBSCENES = "scenes_subscenes";
    private static final String[] TABLE_COLUMNS_SUBSCENES_SELECTION = {TABLE_NAME_SCENES+"."+KEY_SCENE_ID,
                                                                       TABLE_NAME_SCENES+"."+KEY_SCENE_SYSTEM,
                                                                       TABLE_NAME_SCENES+"."+KEY_SCENE_NAME,
                                                                       TABLE_NAME_SUBSCENES+"."+KEY_SUBSCENE_OFFSET};
    /**
     * subscenes table
     */

    public static final String SQL_DROP_TABLE_SUBSCENES = "DROP TABLE IF EXISTS "+TABLE_NAME_SUBSCENES;
    private static final String COMMA_SEP = ", ";
    private static final String KEY_SCENE_SYSTEM_TYPE = "TEXT";
    private static final String KEY_SCENE_NAME_TYPE = "TEXT ";
    private static final String KEY_TRIGGER_CREATED_TYPE = "DATETIME DEFAULT CURRENT_TIMESTAMP";
    private static final String KEY_SCENE_UPDATED_TYPE = "DATETIME DEFAULT CURRENT_TIMESTAMP";
    private static final String KEY_SCENE_CREATED_BY_TYPE = "INTEGER";
    public static final String SQL_CREATE_TABLE_SCENES = "CREATE TABLE "+TABLE_NAME_SCENES+" ("+
            KEY_SCENE_ID+" "+KEY_SCENE_ID_TYPE+COMMA_SEP+
            KEY_SCENE_SYSTEM+" "+KEY_SCENE_SYSTEM_TYPE+COMMA_SEP+
            KEY_SCENE_NAME+" "+KEY_SCENE_NAME_TYPE+COMMA_SEP+
            KEY_SCENE_CREATED+" "+KEY_TRIGGER_CREATED_TYPE+COMMA_SEP+
            KEY_SCENE_UPDATED+" "+KEY_SCENE_UPDATED_TYPE+COMMA_SEP+
            KEY_SCENE_CREATED_BY+" "+KEY_SCENE_CREATED_BY_TYPE+")";
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
        Log.d("Scene.add", scene.toString());
        TriggerRepository triggerRepository = new SQLiteTriggerRepository(this.context);
        ActionRepository actionRepository = new SQLiteActionRepository(this.context);
        SQLiteDatabase sqLiteDatabase = databaseHelper.openDatabase();

        sqLiteDatabase.beginTransaction();
        try
        {
            String query = "INSERT INTO "+TABLE_NAME_SCENES+" ("+
                    KEY_SCENE_ID+COMMA_SEP+
                    KEY_SCENE_SYSTEM+COMMA_SEP+
                    KEY_SCENE_NAME+COMMA_SEP+
                    KEY_SCENE_CREATED_BY+") "+"values"+" ("+
                    "\'"+scene.getId().toString()+"\'"+COMMA_SEP+
                    "\'"+String.valueOf(scene.getSystem())+"\'"+COMMA_SEP+
                    "\'"+scene.getName()+"\'"+");";
            sqLiteDatabase.execSQL(query);

            List<Trigger> triggers = scene.getTriggers();
            for(Trigger trigger : triggers)
            {
                triggerRepository.add(trigger);
            }

            List<SceneComponent> children = scene.getChildren();
            for(SceneComponent child : children)
            {
                if(child instanceof Scene)
                {
                    this.addSubscene((Scene) child);
                }
                else
                {
                    actionRepository.add((Action) child);
                }
            }
            sqLiteDatabase.setTransactionSuccessful();
            Log.i(TAG, "Inserted new Scene with ID: "+scene.getId().toString());
        }
        catch (SQLiteException ex)
        {
            return false;
        }
        finally
        {
            sqLiteDatabase.endTransaction();
            databaseHelper.closeDatabase();
        }
        return true;
    }

    @Override
    public boolean addSubscene(Scene scene) {
        return false;
    }

    @Override
    public boolean delete(Scene scene) {
        //TODO: implement
        return false;
    }

    @Override
    public boolean deleteSubscene(Scene scene) {
        return false;
    }

    @Override
    public boolean update(Scene scene) {
        return false;
    }

    @Override
    public Scene find(UUID id) {
        // obtain thread-safe database access
        SQLiteDatabase sqLiteDatabase = databaseHelper.openDatabase();

        // build a query
        Cursor cursor = sqLiteDatabase.query(TABLE_NAME_SCENES, // a. table
                TABLE_COLUMNS_SCENES, // b. column names
                KEY_SCENE_ID+" = ?", // c. selections
                new String[] {id.toString()}, // d. selections args
                null, // e. group by
                null, // f. having
                null, // g. order by
                null); // h. limit

        // prepare structured data
        SceneBuilder builder = new SceneBuilder(this.context, this);
        ContentValues values = new ContentValues();
        if(cursor != null)
        {
            cursor.moveToFirst();
            values.put(KEY_SCENE_ID, cursor.getString(0));
            values.put(KEY_SCENE_SYSTEM, cursor.getLong(1));
            values.put(KEY_SCENE_NAME, cursor.getString(2));
        }
        // close database connection and release resources
        databaseHelper.closeDatabase();
        cursor.close();
        builder.buildScene(values);
        builder.addActions();
        builder.addTriggers();
        builder.addSubscenes();
        return builder.getScene();
    }

    @Override
    public List<Scene> getAll() {
        // obtain thread-safe database access
        SQLiteDatabase sqLiteDatabase = databaseHelper.openDatabase();

        // build a query
        String query = "SELECT * FROM "+TABLE_NAME_SCENES+" ORDER BY "+KEY_SCENE_NAME;
        Cursor cursor = sqLiteDatabase.rawQuery(query, null);

        // prepare structured data

        ArrayList<Scene> scenes = new ArrayList<Scene>();
        if(cursor.moveToFirst())
        {
            do
            {
                SceneBuilder builder = new SceneBuilder(this.context, this);
                ContentValues values = new ContentValues();
                values.put(KEY_SCENE_ID, cursor.getString(0));
                values.put(KEY_SCENE_SYSTEM, cursor.getLong(1));
                values.put(KEY_SCENE_NAME, cursor.getString(2));
                values.put(KEY_SCENE_NAME, 0);
                builder.buildScene(values);
                builder.addActions();
                builder.addTriggers();
                builder.addSubscenes();
                scenes.add(builder.getScene());
            } while(cursor.moveToNext());
        }
        // close database connection and release resources
        databaseHelper.closeDatabase();
        cursor.close();
        // return Gadget (new or existing)
        return scenes;
    }

    @Override
    public List<Scene> getAllByRoom(UUID roomID) {

        return null;
    }

    @Override
    public List<Scene> getAllSubscenes(UUID id) {
        // obtain thread-safe database access
        SQLiteDatabase sqLiteDatabase = databaseHelper.openDatabase();

        // build a query

        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
        queryBuilder.setTables(TABLE_NAME_SCENES+" RIGHT INNER JOIN "+TABLE_NAME_SUBSCENES+" ON ("+TABLE_NAME_SCENES+"."+KEY_SCENE_ID+" = "+TABLE_NAME_SUBSCENES+"."+KEY_SUBSCENE_SCENE+")");
        queryBuilder.appendWhere(TABLE_NAME_SUBSCENES+"."+KEY_SUBSCENE_SCENE+" = "+id.toString());

        Cursor cursor = queryBuilder.query(sqLiteDatabase, TABLE_COLUMNS_SUBSCENES_SELECTION, null, null, null, null, TABLE_NAME_SUBSCENES+"."+KEY_SUBSCENE_OFFSET);

        // prepare structured data
        ArrayList<Scene> subscenes = new ArrayList<Scene>();
        if(cursor.moveToFirst())
        {
            do
            {
                SceneBuilder builder = new SceneBuilder(this.context, this);
                ContentValues values = new ContentValues();
                values.put(KEY_SCENE_ID, cursor.getString(0));
                values.put(KEY_SCENE_SYSTEM, cursor.getLong(1));
                values.put(KEY_SCENE_NAME, cursor.getString(2));
                values.put(KEY_SUBSCENE_OFFSET, cursor.getString(3));
                builder.buildScene(values);
                builder.addActions();
                builder.addTriggers();
                builder.addSubscenes();
                subscenes.add(builder.getScene());
            } while(cursor.moveToNext());
        }
        // close database connection and release resources
        databaseHelper.closeDatabase();
        cursor.close();
        // return gadgets list
        return subscenes;
    }
}
