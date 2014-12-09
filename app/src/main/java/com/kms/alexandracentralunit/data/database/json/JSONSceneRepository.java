package com.kms.alexandracentralunit.data.database.json;


import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.kms.alexandracentralunit.data.SceneBuilder;
import com.kms.alexandracentralunit.data.database.SceneRepository;
import com.kms.alexandracentralunit.data.model.ActionMessage;
import com.kms.alexandracentralunit.data.model.BaseAction;
import com.kms.alexandracentralunit.data.model.Home;
import com.kms.alexandracentralunit.data.model.Scene;
import com.kms.alexandracentralunit.data.model.SceneComponent;
import com.kms.alexandracentralunit.data.model.Trigger;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


/**
 * JSON implementation of scene repository
 *
 * provides add / delete / update / find / getAll methods
 *
 * @author Mateusz Zasoński
 * @version 0.1
 */
public class JSONSceneRepository implements SceneRepository {

    public static final String KEY_SCENE_ID = "_id";
    public static final String KEY_SCENE_OBJECT = "object";
    public static final String KEY_SCENE_CREATION_TIMESTAMP = "created_at";
    private static final String[] TABLE_COLUMNS = {KEY_SCENE_ID, KEY_SCENE_OBJECT,
                                                   KEY_SCENE_CREATION_TIMESTAMP};
    private static final String TABLE_NAME = "scenes";
    public static final String SQL_DROP_TABLE = "DROP TABLE IF EXISTS "+TABLE_NAME;
    private static final String COMMA_SEP = ", ";
    private static final String KEY_GADGET_ID_TYPE = "TEXT";
    private static final String KEY_GADGET_OBJECT_TYPE = "TEXT";
    private static final String KEY_SCENE_CREATION_TIMESTAMP_TYPE = "TEXT";
    public static final String SQL_CREATE_TABLE = "CREATE TABLE "+TABLE_NAME+" ("+
            KEY_SCENE_ID+" "+KEY_GADGET_ID_TYPE+COMMA_SEP+
            KEY_SCENE_OBJECT+" "+KEY_GADGET_OBJECT_TYPE+COMMA_SEP+
            KEY_SCENE_CREATION_TIMESTAMP+" "+KEY_SCENE_CREATION_TIMESTAMP_TYPE+")";

    private JSONConfigurationDatabaseHelper databaseHelper;
    private Home home;

    public JSONSceneRepository(Context context) {
        databaseHelper = JSONConfigurationDatabaseHelper.getInstance(context);
    }

    @Override
    public boolean add(Scene scene) {
        JSONObject object = toJSONObject(scene);
        SQLiteDatabase sqLiteDatabase = databaseHelper.openDatabase();

        String query = "INSERT INTO "+TABLE_NAME+" ("+
                KEY_SCENE_ID+COMMA_SEP+
                KEY_SCENE_OBJECT+") "+"values"+" ("+
                "\'"+scene.getId()+"\'"+COMMA_SEP+
                "\'"+object.toString()+"\'"+");";
        sqLiteDatabase.execSQL(query);

        databaseHelper.closeDatabase();
        return true;
    }

    @Override
    public boolean delete(Scene scene) {
        SQLiteDatabase sqLiteDatabase = databaseHelper.openDatabase();

        String query = "DELETE FROM "+TABLE_NAME+
                " WHERE "+KEY_SCENE_ID+" = "+"\'"+scene.getId()+"\'"+");";

        sqLiteDatabase.execSQL(query);
        databaseHelper.closeDatabase();
        return true;
    }

    @Override
    public boolean update(Scene scene) {
        JSONObject object = toJSONObject(scene);
        SQLiteDatabase sqLiteDatabase = databaseHelper.openDatabase();

        String query = "UPDATE "+TABLE_NAME+" SET "+
                KEY_SCENE_OBJECT+" = "+"\'"+object.toString()+"\'"+
                " WHERE "+KEY_SCENE_ID+" = "+"\'"+scene.getId()+"\'"+";";

        sqLiteDatabase.execSQL(query);
        databaseHelper.closeDatabase();
        return true;
    }

    @Override
    public Scene find(String id) {
        // obtain thread-safe database access
        SQLiteDatabase sqLiteDatabase = databaseHelper.openDatabase();

        // build a query
        Cursor cursor = sqLiteDatabase.query(TABLE_NAME, // a. table
                TABLE_COLUMNS, // b. column names
                KEY_SCENE_ID+" = ?", // c. selections
                new String[] {id}, // d. selections args
                null, // e. group by
                null, // f. having
                null, // g. order by
                null); // h. limit

        // prepare structured data
        Scene scene;
        if(cursor != null)
        {
            cursor.moveToFirst();
            try
            {
                scene = fromJSONObject(new JSONObject(cursor.getString(1)));
                databaseHelper.closeDatabase();
                cursor.close();
                return scene;
            }
            catch (JSONException e)
            {
                e.printStackTrace();
                return null;
            }
        }
        return null;
    }

    @Override
    public List<Scene> getAll() {
        // obtain thread-safe database access
        SQLiteDatabase sqLiteDatabase = databaseHelper.openDatabase();

        // build a query
        String query = "SELECT * FROM "+TABLE_NAME+" ORDER BY "+KEY_SCENE_CREATION_TIMESTAMP;
        Cursor cursor = sqLiteDatabase.rawQuery(query, null);

        // prepare structured data
        ArrayList<Scene> scenes = new ArrayList<Scene>();
        if(cursor.moveToFirst())
        {
            do
            {
                try
                {
                    scenes.add(fromJSONObject(new JSONObject(cursor.getString(1))));
                }
                catch (JSONException e)
                {
                    e.printStackTrace();
                    return scenes;
                }
            } while(cursor.moveToNext());
        }
        // close database connection and release resources
        databaseHelper.closeDatabase();
        cursor.close();
        // return rooms
        return scenes;
    }

    @Override
    public List<Scene> getAllByRoom(String roomID) {
        // obtain thread-safe database access
        SQLiteDatabase sqLiteDatabase = databaseHelper.openDatabase();

        // build a query
        String query = "SELECT * FROM "+TABLE_NAME+" ORDER BY "+KEY_SCENE_CREATION_TIMESTAMP;
        Cursor cursor = sqLiteDatabase.rawQuery(query, null);

        // prepare structured data
        ArrayList<Scene> scenes = new ArrayList<Scene>();
        if(cursor.moveToFirst())
        {
            do
            {
                try
                {
                    //    Scene newSCene = fromJSONObject(new JSONObject(cursor.getString(1)));
                    scenes.add(fromJSONObject(new JSONObject(cursor.getString(1))));
                }
                catch (JSONException e)
                {
                    e.printStackTrace();
                    return scenes;
                }
            } while(cursor.moveToNext());
        }
        // close database connection and release resources
        databaseHelper.closeDatabase();
        cursor.close();
        // return rooms
        return scenes;
    }

    public void setHome(Home home) {
        this.home = home;
    }

    private JSONObject toJSONObject(Scene scene) {
        JSONObject result = new JSONObject();
        try
        {
            result.put(Scene.ID, scene.getId());
            result.put(Scene.NAME, scene.getName());
            JSONArray triggersArray = new JSONArray();
            for(Trigger trigger : scene.getTriggers())
            {
                triggersArray.put(trigger.toJSONObject());
            }
            result.put(Scene.TRIGGERS, triggersArray);
            JSONArray actionsArray = new JSONArray();
            JSONArray subscenesArray = new JSONArray();
            for(SceneComponent sceneComponent : scene.getChildren())
            {
                if(sceneComponent instanceof Scene)
                {
                    subscenesArray.put(((Scene) sceneComponent).getId());
                }
                else
                {
                    actionsArray.put(((BaseAction) sceneComponent).toJSONObject());
                }
            }
            result.put(Scene.ACTIONS, actionsArray);
            result.put(Scene.SUBSCENES, subscenesArray);

            return result;
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }

        return result;
    }

    private Scene fromJSONObject(JSONObject object) {
        SceneBuilder builder = new SceneBuilder(home);
        try
        {
            String id = object.getString(Scene.ID);
            String name = object.getString(Scene.NAME);
            builder.create(id, name);

            /**
             * essential action data encapsulation
             * and passing it to scene builder
             */
            List<ActionMessage> actions = new ArrayList<ActionMessage>();
            JSONArray actionsList = object.getJSONArray(Scene.ACTIONS);

            for(int i = 0; i < actionsList.length(); i++)
            {
                JSONObject actionObject = actionsList.getJSONObject(i);
                UUID gadgetId = UUID.fromString(actionObject.getString(BaseAction.GADGET_ID));
                String action = actionObject.getString(BaseAction.ACTION);
                String parameter = actionObject.getString(BaseAction.PARAMETER);
                long delay = actionObject.getLong(BaseAction.DELAY);
                actions.add(new ActionMessage(gadgetId, action, parameter, delay));
            }
            builder.addActions(actions);

            /**
             * getting subscenes' ID list
             * and passing it to scene builder
             */
            List<String> subscenes = new ArrayList<String>();
            JSONArray subscenesList = object.getJSONArray(Scene.SUBSCENES);
            for(int i = 0; i < subscenesList.length(); i++)
            {
                subscenes.add(subscenesList.getString(i));
            }
            builder.addSubscenes(subscenes);

            /**
             * first step of trigger creation
             * for next step passing triggers list to scene builder
             */
            List<Trigger> triggers = new ArrayList<Trigger>();
            JSONArray triggersList = object.getJSONArray(Scene.TRIGGERS);
            for(int i = 0; i < triggersList.length(); i++)
            {
                Trigger trigger = new Trigger(id);
                JSONArray conditionsList = triggersList.getJSONObject(i).getJSONArray(Trigger.CONDITIONS);
                for(int j = 0; j < conditionsList.length(); j++)
                {
                    JSONObject triggerObject = conditionsList.getJSONObject(j);
                    UUID gadgetID = UUID.fromString(triggerObject.getString(Trigger.CONDITION_GADGET));
                    String parameter = triggerObject.getString(Trigger.CONDITION_PARAMETER);
                    String value = triggerObject.getString(Trigger.CONDITION_VALUE);
                    trigger.addObserver(gadgetID, parameter, value);
                }
                triggers.add(trigger);
            }
            builder.addTriggers(triggers);
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
        catch (IllegalArgumentException e)
        {
            e.printStackTrace();
        }
        return builder.getScene();
    }
}
