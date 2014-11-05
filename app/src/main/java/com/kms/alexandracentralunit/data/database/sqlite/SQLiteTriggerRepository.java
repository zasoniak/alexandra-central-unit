package com.kms.alexandracentralunit.data.database.sqlite;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.kms.alexandracentralunit.data.TriggerFactory;
import com.kms.alexandracentralunit.data.database.TriggerRepository;
import com.kms.alexandracentralunit.data.model.Trigger;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


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

    /**
     * class implementation
     */
    private ConfigurationDatabaseHelper databaseHelper;
    private Context context;

    public SQLiteTriggerRepository(Context context) {
        this.databaseHelper = ConfigurationDatabaseHelper.getInstance(context);
        this.context = context;
    }

    @Override
    public boolean add(Trigger trigger) {
        Log.d("Gadget.add", trigger.toString());
        SQLiteDatabase sqLiteDatabase = databaseHelper.openDatabase();

        String query = "INSERT INTO "+TABLE_NAME+" ("+
                KEY_TRIGGER_SCENE+COMMA_SEP+
                KEY_TRIGGER_GADGET+COMMA_SEP+
                KEY_TRIGGER_ACTION+") "+"values"+" ("+
                "\'"+trigger.getScene().toString()+"\'"+COMMA_SEP+
                "\'"+trigger.getGadget().getId().toString()+"\'"+COMMA_SEP+
                "\'"+trigger.getAction()+"\'"+");";

        sqLiteDatabase.execSQL(query);
        Log.i(TAG, "Inserted new trigger: "+trigger.toString());

        databaseHelper.closeDatabase();
        return true;
    }

    @Override
    public boolean delete(Trigger trigger) {
        //TODO: to be done (softdelete?)
        return false;
    }

    @Override
    public List<Trigger> getAll() {
        // obtain thread-safe database access
        SQLiteDatabase sqLiteDatabase = databaseHelper.openDatabase();

        // build a query
        String query = "SELECT * FROM "+TABLE_NAME+" ORDER BY "+KEY_TRIGGER_SCENE;
        Cursor cursor = sqLiteDatabase.rawQuery(query, null);

        // prepare structured data
        ArrayList<Trigger> triggers = new ArrayList<Trigger>();
        if(cursor.moveToFirst())
        {
            do
            {
                ContentValues values = new ContentValues();
                values.put(KEY_TRIGGER_SCENE, cursor.getString(0));
                values.put(KEY_TRIGGER_GADGET, cursor.getLong(1));
                values.put(KEY_TRIGGER_ACTION, cursor.getString(2));
                triggers.add(TriggerFactory.create(values));
            } while(cursor.moveToNext());
        }
        // close database connection and release resources
        databaseHelper.closeDatabase();
        cursor.close();
        // return Triggers
        return triggers;
    }

    @Override
    public List<Trigger> getAllByScene(UUID sceneID) {
        // obtain thread-safe database access
        SQLiteDatabase sqLiteDatabase = databaseHelper.openDatabase();

        // build a query
        Cursor cursor = sqLiteDatabase.query(TABLE_NAME, // a. table
                TABLE_COLUMNS, // b. column names
                KEY_TRIGGER_SCENE+" = ?", // c. selections
                new String[] {String.valueOf(sceneID)}, // d. selections args
                null, // e. group by
                null, // f. having
                null, // g. order by
                null); // h. limit

        // prepare structured data
        ArrayList<Trigger> triggers = new ArrayList<Trigger>();
        if(cursor.moveToFirst())
        {
            do
            {
                ContentValues values = new ContentValues();
                values.put(KEY_TRIGGER_SCENE, cursor.getString(0));
                values.put(KEY_TRIGGER_GADGET, cursor.getLong(1));
                values.put(KEY_TRIGGER_ACTION, cursor.getString(2));
                triggers.add(TriggerFactory.create(values));
            } while(cursor.moveToNext());
        }
        // close database connection and release resources
        databaseHelper.closeDatabase();
        cursor.close();
        // return triggers
        return triggers;
    }
}
