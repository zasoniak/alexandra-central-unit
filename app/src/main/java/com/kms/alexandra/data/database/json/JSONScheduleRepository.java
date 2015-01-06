package com.kms.alexandra.data.database.json;


import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kms.alexandra.data.database.ScheduleRepository;
import com.kms.alexandra.data.model.ScheduledScene;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * JSON implementation of schedule repository
 * <p/>
 * provides add / delete / update / getAll methods
 *
 * @author Mateusz Zasoński
 * @version 0.1
 */
public class JSONScheduleRepository implements ScheduleRepository {

    public static final String KEY_SCHEDULE_ID = "_id";
    public static final String KEY_SCHEDULE_OBJECT = "object";
    private static final String TABLE_NAME = "schedule";
    public static final String SQL_DROP_TABLE = "DROP TABLE IF EXISTS "+TABLE_NAME;
    private static final String COMMA_SEP = ", ";
    private static final String KEY_SCHEDULE_ID_TYPE = "TEXT";
    private static final String KEY_SCHEDULE_OBJECT_TYPE = "TEXT";
    public static final String SQL_CREATE_TABLE = "CREATE TABLE "+TABLE_NAME+" ("+
            KEY_SCHEDULE_ID+" "+KEY_SCHEDULE_ID_TYPE+COMMA_SEP+
            KEY_SCHEDULE_OBJECT+" "+KEY_SCHEDULE_OBJECT_TYPE+")";

    private JSONConfigurationDatabaseHelper databaseHelper;

    public JSONScheduleRepository(Context context) {
        databaseHelper = JSONConfigurationDatabaseHelper.getInstance(context);
    }

    @Override
    public boolean add(ScheduledScene scheduledScene) {
        JSONObject object = toJSONObject(scheduledScene);
        SQLiteDatabase sqLiteDatabase = databaseHelper.openDatabase();

        String query = "INSERT INTO "+TABLE_NAME+" ("+
                KEY_SCHEDULE_ID+COMMA_SEP+
                KEY_SCHEDULE_OBJECT+") "+"values"+" ("+
                "\'"+scheduledScene.getId()+"\'"+COMMA_SEP+
                "\'"+object.toString()+"\'"+");";
        sqLiteDatabase.execSQL(query);

        databaseHelper.closeDatabase();
        return true;
    }

    @Override
    public boolean delete(ScheduledScene scheduledScene) {
        SQLiteDatabase sqLiteDatabase = databaseHelper.openDatabase();

        String query = "DELETE FROM "+TABLE_NAME+
                " WHERE "+KEY_SCHEDULE_ID+" = "+"\'"+scheduledScene.getId()+"\'"+";";

        sqLiteDatabase.execSQL(query);
        databaseHelper.closeDatabase();
        return true;
    }

    @Override
    public boolean update(ScheduledScene scheduledScene) {
        JSONObject object = toJSONObject(scheduledScene);
        SQLiteDatabase sqLiteDatabase = databaseHelper.openDatabase();

        String query = "UPDATE "+TABLE_NAME+" SET "+
                KEY_SCHEDULE_OBJECT+" = "+"\'"+object.toString()+"\'"+
                " WHERE "+KEY_SCHEDULE_OBJECT+" = "+"\'"+scheduledScene.getId()+"\'"+";";

        sqLiteDatabase.execSQL(query);
        databaseHelper.closeDatabase();
        return true;
    }

    @Override
    public List<ScheduledScene> getAll() {
        // obtain thread-safe database access
        SQLiteDatabase sqLiteDatabase = databaseHelper.openDatabase();

        // build a query
        String query = "SELECT * FROM "+TABLE_NAME+" ORDER BY "+KEY_SCHEDULE_ID;
        Cursor cursor = sqLiteDatabase.rawQuery(query, null);

        // prepare structured data
        ArrayList<ScheduledScene> scheduledScenes = new ArrayList<ScheduledScene>();
        if(cursor.moveToFirst())
        {
            do
            {
                try
                {
                    scheduledScenes.add(fromJSONObject(new JSONObject(cursor.getString(1))));
                }
                catch (JSONException e)
                {
                    e.printStackTrace();
                    return scheduledScenes;
                }
            } while(cursor.moveToNext());
        }
        // close database connection and release resources
        databaseHelper.closeDatabase();
        cursor.close();
        // return rooms
        return scheduledScenes;
    }

    private ScheduledScene fromJSONObject(JSONObject object) {
        try
        {
            String id = object.getString(ScheduledScene.ID);
            String sceneId = object.getString(ScheduledScene.SCENE);
            long time = object.getLong(ScheduledScene.TIME);
            long repeatInterval = object.getLong(ScheduledScene.REPEAT_INTERVAL);
            HashMap<String, String> conditions = new ObjectMapper().readValue(object.getJSONObject(ScheduledScene.CONDITIONS).toString(), new TypeReference<Map<String, String>>() {
            });
            return new ScheduledScene(id, sceneId, time, repeatInterval, conditions);
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return null;
    }

    private JSONObject toJSONObject(ScheduledScene scheduledScene) {
        JSONObject result = new JSONObject();
        try
        {
            result.put(ScheduledScene.ID, scheduledScene.getId());
            result.put(ScheduledScene.SCENE, scheduledScene.getScene());
            result.put(ScheduledScene.TIME, scheduledScene.getTime());
            result.put(ScheduledScene.REPEAT_INTERVAL, scheduledScene.getRepeatInterval());

            JSONObject conditions = new JSONObject();
            for(Map.Entry<String, String> entry : scheduledScene.getConditions().entrySet())
            {
                conditions.put(entry.getKey(), entry.getValue());
            }
            result.put(ScheduledScene.CONDITIONS, conditions);
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
        return result;
    }
}
