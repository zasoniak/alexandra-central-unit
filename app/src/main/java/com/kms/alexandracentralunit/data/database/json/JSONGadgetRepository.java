package com.kms.alexandracentralunit.data.database.json;


import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.kms.alexandracentralunit.data.GadgetFactory;
import com.kms.alexandracentralunit.data.database.GadgetRepository;
import com.kms.alexandracentralunit.data.model.Gadget;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


/**
 * JSON implementation of gadget repository
 *
 * provides add / delete / update / find / getAll methods
 *
 * @author Mateusz Zasoński
 * @version 0.1
 */
public class JSONGadgetRepository implements GadgetRepository {

    public static final String KEY_GADGET_ID = "_id";
    public static final String KEY_GADGET_OBJECT = "object";
    private static final String[] TABLE_COLUMNS = {KEY_GADGET_ID, KEY_GADGET_OBJECT};
    private static final String TABLE_NAME = "gadgets";
    public static final String SQL_DROP_TABLE = "DROP TABLE IF EXISTS "+TABLE_NAME;
    private static final String COMMA_SEP = ", ";
    private static final String KEY_GADGET_ID_TYPE = "TEXT";
    private static final String KEY_GADGET_OBJECT_TYPE = "TEXT";
    public static final String SQL_CREATE_TABLE = "CREATE TABLE "+TABLE_NAME+" ("+
            KEY_GADGET_ID+" "+KEY_GADGET_ID_TYPE+COMMA_SEP+
            KEY_GADGET_OBJECT+" "+KEY_GADGET_OBJECT_TYPE+")";

    private JSONConfigurationDatabaseHelper databaseHelper;

    public JSONGadgetRepository(Context context) {
        databaseHelper = JSONConfigurationDatabaseHelper.getInstance(context);
    }

    @Override
    public boolean add(Gadget gadget) {
        JSONObject object = toJSONObject(gadget);
        SQLiteDatabase sqLiteDatabase = databaseHelper.openDatabase();

        String query = "INSERT INTO "+TABLE_NAME+" ("+
                KEY_GADGET_ID+COMMA_SEP+
                KEY_GADGET_OBJECT+") "+"values"+" ("+
                "\'"+gadget.getId()+"\'"+COMMA_SEP+
                "\'"+object.toString()+"\'"+");";
        sqLiteDatabase.execSQL(query);

        databaseHelper.closeDatabase();
        return true;
    }

    @Override
    public boolean delete(Gadget gadget) {
        SQLiteDatabase sqLiteDatabase = databaseHelper.openDatabase();

        String query = "DELETE FROM "+TABLE_NAME+
                " WHERE "+KEY_GADGET_ID+" = "+"\'"+gadget.getId()+"\'"+");";

        sqLiteDatabase.execSQL(query);
        databaseHelper.closeDatabase();
        return true;
    }

    @Override
    public boolean update(Gadget gadget) {
        JSONObject object = toJSONObject(gadget);
        SQLiteDatabase sqLiteDatabase = databaseHelper.openDatabase();

        String query = "UPDATE "+TABLE_NAME+" SET "+
                KEY_GADGET_OBJECT+" = "+"\'"+object.toString()+"\'"+
                " WHERE "+KEY_GADGET_ID+" = "+"\'"+gadget.getId()+"\'"+";";

        sqLiteDatabase.execSQL(query);
        databaseHelper.closeDatabase();
        return true;
    }

    @Override
    public Gadget find(UUID id) {
        // obtain thread-safe database access
        SQLiteDatabase sqLiteDatabase = databaseHelper.openDatabase();

        // build a query
        Cursor cursor = sqLiteDatabase.query(TABLE_NAME, // a. table
                TABLE_COLUMNS, // b. column names
                KEY_GADGET_ID+" = ?", // c. selections
                new String[] {id.toString()}, // d. selections args
                null, // e. group by
                null, // f. having
                null, // g. order by
                null); // h. limit

        // prepare structured data
        Gadget gadget;
        if(cursor != null)
        {
            cursor.moveToFirst();
            try
            {
                gadget = fromJSONObject(new JSONObject(cursor.getString(1)));
                databaseHelper.closeDatabase();
                cursor.close();
                return gadget;
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
    public List<Gadget> getAll() {
        // obtain thread-safe database access
        SQLiteDatabase sqLiteDatabase = databaseHelper.openDatabase();

        // build a query
        String query = "SELECT * FROM "+TABLE_NAME+" ORDER BY "+KEY_GADGET_ID;
        Cursor cursor = sqLiteDatabase.rawQuery(query, null);

        // prepare structured data
        ArrayList<Gadget> gadgets = new ArrayList<Gadget>();
        if(cursor.moveToFirst())
        {
            do
            {
                try
                {
                    gadgets.add(fromJSONObject(new JSONObject(cursor.getString(1))));
                }
                catch (JSONException e)
                {
                    e.printStackTrace();
                    return gadgets;
                }
            } while(cursor.moveToNext());
        }
        // close database connection and release resources
        databaseHelper.closeDatabase();
        cursor.close();
        // return rooms
        return gadgets;
    }

    @Override
    public List<Gadget> getAllByRoom(String roomID) {
        // obtain thread-safe database access
        SQLiteDatabase sqLiteDatabase = databaseHelper.openDatabase();

        // build a query
        String query = "SELECT * FROM "+TABLE_NAME+" ORDER BY "+KEY_GADGET_ID;
        Cursor cursor = sqLiteDatabase.rawQuery(query, null);

        // prepare structured data
        ArrayList<Gadget> gadgets = new ArrayList<Gadget>();
        if(cursor.moveToFirst())
        {
            do
            {
                try
                {
                    Gadget newGadget = fromJSONObject(new JSONObject(cursor.getString(1)));
                    if(newGadget.getRoom().equals(roomID))
                    {
                        gadgets.add(newGadget);
                    }
                }
                catch (JSONException e)
                {
                    e.printStackTrace();
                    return gadgets;
                }
            } while(cursor.moveToNext());
        }
        // close database connection and release resources
        databaseHelper.closeDatabase();
        cursor.close();
        // return rooms
        return gadgets;
    }

    @Override
    public List<Gadget> getAllByType(String type) {
        // obtain thread-safe database access
        SQLiteDatabase sqLiteDatabase = databaseHelper.openDatabase();

        // build a query
        String query = "SELECT * FROM "+TABLE_NAME+" ORDER BY "+KEY_GADGET_ID;
        Cursor cursor = sqLiteDatabase.rawQuery(query, null);

        // prepare structured data
        ArrayList<Gadget> gadgets = new ArrayList<Gadget>();
        if(cursor.moveToFirst())
        {
            do
            {
                try
                {
                    Gadget newGadget = fromJSONObject(new JSONObject(cursor.getString(1)));
                    if(newGadget.getType().equals(Gadget.GadgetType.valueOf(type)))
                    {
                        gadgets.add(newGadget);
                    }
                }
                catch (JSONException e)
                {
                    e.printStackTrace();
                    return gadgets;
                }
                catch (IllegalArgumentException e)
                {
                    e.printStackTrace();
                    return gadgets;
                }
            } while(cursor.moveToNext());
        }
        // close database connection and release resources
        databaseHelper.closeDatabase();
        cursor.close();
        // return rooms
        return gadgets;
    }

    private Gadget fromJSONObject(JSONObject object) {
        UUID id;
        String name;
        String MAC;
        String roomId;
        Gadget.GadgetType type;
        int channels;
        try
        {
            id = UUID.fromString(object.getString(Gadget.ID));
            name = object.getString(Gadget.NAME);
            MAC = object.getString(Gadget.MAC_ADDRESS);
            roomId = object.getString(Gadget.ROOM_ID);
            type = Gadget.GadgetType.valueOf(object.getString(Gadget.TYPE));
            channels = object.getInt(Gadget.CHANNELS);
            return GadgetFactory.create(id, roomId, name, MAC, type, channels);

        }
        catch (JSONException e)
        {
            e.printStackTrace();
            return null;
        }
        catch (IllegalArgumentException e)
        {
            e.printStackTrace();
            return null;
        }
    }

    private JSONObject toJSONObject(Gadget gadget) {
        JSONObject result = new JSONObject();
        try
        {
            result.put(Gadget.ID, gadget.getId().toString());
            result.put(Gadget.NAME, gadget.getName());
            result.put(Gadget.MAC_ADDRESS, gadget.getMAC());
            result.put(Gadget.ROOM_ID, gadget.getRoom());
            result.put(Gadget.TYPE, gadget.getType());
            result.put(Gadget.CHANNELS, gadget.getChannels());
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
        return result;
    }

}
