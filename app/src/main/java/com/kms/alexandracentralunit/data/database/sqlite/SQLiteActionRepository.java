package com.kms.alexandracentralunit.data.database.sqlite;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.kms.alexandracentralunit.data.ActionFactory;
import com.kms.alexandracentralunit.data.database.ActionRepository;
import com.kms.alexandracentralunit.data.model.Action;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


/**
 * Created by Mateusz Zasoński on 2014-10-31.
 */
public class SQLiteActionRepository implements ActionRepository {

    // Actions table columns names
    public static final String KEY_ACTION_SCENE = "scene_id";
    public static final String KEY_ACTION_GADGET = "gadget_id";
    public static final String KEY_ACTION_ACTION = "action";
    public static final String KEY_ACTION_OFFSET = "offset";
    public static final String KEY_ACTION_CREATED = "created_at";
    public static final String KEY_ACTION_UPDATED = "updated_at";
    // Actions table column array
    private static final String[] TABLE_COLUMNS = {KEY_ACTION_SCENE, KEY_ACTION_GADGET,
                                                   KEY_ACTION_ACTION, KEY_ACTION_OFFSET,
                                                   KEY_ACTION_CREATED, KEY_ACTION_UPDATED};
    public static final String KEY_ACTION_SCENE_TYPE = "TEXT";
    private static final String TAG = "SQLiteActionRepository";
    private static final String TABLE_NAME = "actions_scenes";
    // Predefined SQL statements
    public static final String SQL_DROP_TABLE = "DROP TABLE IF EXISTS "+TABLE_NAME;
    // Actions table columns types
    private static final String COMMA_SEP = ", ";
    private static final String KEY_ACTION_GADGET_TYPE = "TEXT";
    private static final String KEY_ACTION_ACTION_TYPE = "TEXT ";
    private static final String KEY_ACTION_OFFSET_TYPE = "INTEGER";
    private static final String KEY_ACTION_CREATED_TYPE = "TEXT";
    private static final String KEY_ACTION_UPDATED_TYPE = "DATETIME DEFAULT CURRENT_TIMESTAMP";
    public static final String SQL_CREATE_TABLE = "CREATE TABLE "+TABLE_NAME+" ("+
            KEY_ACTION_SCENE+" "+KEY_ACTION_SCENE_TYPE+COMMA_SEP+
            KEY_ACTION_GADGET+" "+KEY_ACTION_GADGET_TYPE+COMMA_SEP+
            KEY_ACTION_ACTION+" "+KEY_ACTION_ACTION_TYPE+COMMA_SEP+
            KEY_ACTION_OFFSET+" "+KEY_ACTION_OFFSET_TYPE+COMMA_SEP+
            KEY_ACTION_CREATED+" "+KEY_ACTION_CREATED_TYPE+COMMA_SEP+
            KEY_ACTION_UPDATED+" "+KEY_ACTION_UPDATED_TYPE+")";

    // class implementation
    private ConfigurationDatabaseHelper databaseHelper;

    public SQLiteActionRepository(Context context) {
        databaseHelper = ConfigurationDatabaseHelper.getInstance(context);
    }

    @Override
    public boolean add(Action action) {
        Log.d("Action.add", action.toString());
        SQLiteDatabase sqLiteDatabase = databaseHelper.openDatabase();

        String query = "INSERT INTO "+TABLE_NAME+" ("+
                KEY_ACTION_SCENE+COMMA_SEP+
                KEY_ACTION_GADGET+COMMA_SEP+
                KEY_ACTION_ACTION+COMMA_SEP+
                KEY_ACTION_OFFSET+") "+"values"+" ("+
                "\'"+action.getScene().toString()+"\'"+COMMA_SEP+
                "\'"+action.getGadget().getId().toString()+"\'"+COMMA_SEP+
                "\'"+action.getParameters()+"\'"+COMMA_SEP+
                "\'"+String.valueOf(action.getOffset())+"\'"+");";
        sqLiteDatabase.execSQL(query);
        Log.i(TAG, "Inserted new action: "+action.toString());

        databaseHelper.closeDatabase();
        return true;
    }

    @Override
    public boolean delete(Action action) {
        //TODO: uzupelnic
        return false;
    }

    @Override
    public boolean update(Action action) {
        Log.d("Action.update", action.toString());
        SQLiteDatabase sqLiteDatabase = databaseHelper.openDatabase();

        String query = "UPDATE "+TABLE_NAME+" SET "+
                KEY_ACTION_ACTION+" = "+action.getParameters()+COMMA_SEP+
                KEY_ACTION_OFFSET+" = "+String.valueOf(action.getOffset())+COMMA_SEP+
                KEY_ACTION_UPDATED+" = "+ConfigurationDatabaseHelper.SQL_CURRENT_TIMESTAMP+
                " WHERE "+KEY_ACTION_SCENE+" = "+"\'"+action.getScene().toString()+"\'"+"AND"+
                KEY_ACTION_GADGET+" = "+"\'"+action.getGadget().getId().toString()+"\'"+");";

        sqLiteDatabase.execSQL(query);
        Log.i(TAG, "Updated action: "+action.toString());
        databaseHelper.closeDatabase();
        return true;
    }

    @Override
    public List<Action> getAllByScene(UUID sceneID) {
        // obtain thread-safe database access
        SQLiteDatabase sqLiteDatabase = databaseHelper.openDatabase();

        // build a query
        Cursor cursor = sqLiteDatabase.query(TABLE_NAME, // a. table
                TABLE_COLUMNS, // b. column names
                KEY_ACTION_SCENE+" = ?", // c. selections
                new String[] {sceneID.toString()}, // d. selections args
                null, // e. group by
                null, // f. having
                null, // g. order by
                null); // h. limit

        // prepare structured data
        ArrayList<Action> actions = new ArrayList<Action>();
        if(cursor.moveToFirst())
        {
            do
            {
                ContentValues values = new ContentValues();
                values.put(KEY_ACTION_SCENE, cursor.getString(0));
                values.put(KEY_ACTION_GADGET, cursor.getString(1));
                values.put(KEY_ACTION_ACTION, cursor.getString(2));
                values.put(KEY_ACTION_OFFSET, cursor.getLong(3));
                actions.add(ActionFactory.create(values));
            } while(cursor.moveToNext());
        }
        // close database connection and release resources
        databaseHelper.closeDatabase();
        cursor.close();
        // return gadgets list
        return actions;
    }
}
