package com.kms.alexandra.data.database.json;


import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.kms.alexandra.data.database.RoomRepository;
import com.kms.alexandra.data.model.Room;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


/**
 * JSON implementation of room repository
 * <p/>
 * provides add / delete / update / find / getAll methods
 *
 * @author Mateusz Zasoński
 * @version 0.1
 */
public class JSONRoomRepository implements RoomRepository {

    public static final String KEY_ROOM_ID = "_id";
    public static final String KEY_ROOM_OBJECT = "object";
    private static final String[] TABLE_COLUMNS = {KEY_ROOM_ID, KEY_ROOM_OBJECT};
    private static final String TABLE_NAME = "rooms";
    public static final String SQL_DROP_TABLE = "DROP TABLE IF EXISTS "+TABLE_NAME;
    private static final String COMMA_SEP = ", ";
    private static final String KEY_ROOM_ID_TYPE = "TEXT";
    private static final String KEY_ROOM_OBJECT_TYPE = "TEXT";
    public static final String SQL_CREATE_TABLE = "CREATE TABLE "+TABLE_NAME+" ("+
            KEY_ROOM_ID+" "+KEY_ROOM_ID_TYPE+COMMA_SEP+
            KEY_ROOM_OBJECT+" "+KEY_ROOM_OBJECT_TYPE+")";
    private JSONConfigurationDatabaseHelper databaseHelper;

    public JSONRoomRepository(Context context) {
        databaseHelper = JSONConfigurationDatabaseHelper.getInstance(context);
    }

    @Override
    public boolean add(Room room) {
        JSONObject object = toJSONObject(room);
        SQLiteDatabase sqLiteDatabase = databaseHelper.openDatabase();

        String query = "INSERT INTO "+TABLE_NAME+" ("+
                KEY_ROOM_ID+COMMA_SEP+
                KEY_ROOM_OBJECT+") "+"values"+" ("+
                "\'"+room.getId()+"\'"+COMMA_SEP+
                "\'"+object.toString()+"\'"+");";
        sqLiteDatabase.execSQL(query);

        databaseHelper.closeDatabase();
        return true;
    }

    @Override
    public boolean delete(Room room) {
        SQLiteDatabase sqLiteDatabase = databaseHelper.openDatabase();

        String query = "DELETE FROM "+TABLE_NAME+
                " WHERE "+KEY_ROOM_ID+" = "+"\'"+room.getId()+"\'"+";";

        sqLiteDatabase.execSQL(query);
        databaseHelper.closeDatabase();
        return true;
    }

    @Override
    public boolean update(Room room) {
        JSONObject object = toJSONObject(room);
        SQLiteDatabase sqLiteDatabase = databaseHelper.openDatabase();

        String query = "UPDATE "+TABLE_NAME+" SET "+
                KEY_ROOM_OBJECT+" = "+"\'"+object.toString()+"\'"+
                " WHERE "+KEY_ROOM_ID+" = "+"\'"+room.getId()+"\'"+";";

        sqLiteDatabase.execSQL(query);
        databaseHelper.closeDatabase();
        return true;
    }

    @Override
    public Room find(String id) {
        // obtain thread-safe database access
        SQLiteDatabase sqLiteDatabase = databaseHelper.openDatabase();

        // build a query
        Cursor cursor = sqLiteDatabase.query(TABLE_NAME, // a. table
                TABLE_COLUMNS, // b. column names
                KEY_ROOM_ID+" = ?", // c. selections
                new String[] {id}, // d. selections args
                null, // e. group by
                null, // f. having
                null, // g. order by
                null); // h. limit

        // prepare structured data
        Room room;
        if(cursor != null)
        {
            cursor.moveToFirst();
            try
            {
                room = fromJSONObject(new JSONObject(cursor.getString(1)));
                databaseHelper.closeDatabase();
                cursor.close();
                return room;
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
    public List<Room> getAll() {
        // obtain thread-safe database access
        SQLiteDatabase sqLiteDatabase = databaseHelper.openDatabase();

        // build a query
        String query = "SELECT * FROM "+TABLE_NAME+" ORDER BY "+KEY_ROOM_ID;
        Cursor cursor = sqLiteDatabase.rawQuery(query, null);

        // prepare structured data
        ArrayList<Room> rooms = new ArrayList<Room>();
        if(cursor.moveToFirst())
        {
            do
            {
                try
                {
                    rooms.add(fromJSONObject(new JSONObject(cursor.getString(1))));
                }
                catch (JSONException e)
                {
                    e.printStackTrace();
                    return rooms;
                }
            } while(cursor.moveToNext());
        }
        // close database connection and release resources
        databaseHelper.closeDatabase();
        cursor.close();
        // return rooms
        return rooms;
    }

    private Room fromJSONObject(JSONObject object) {
        String id;
        String name;
        int color;
        List<UUID> gadgets = new ArrayList<UUID>();
        try
        {
            id = object.getString(Room.ID);
            name = object.getString(Room.NAME);
            color = object.getInt(Room.COLOR);
            JSONArray uuids = object.getJSONArray(Room.GADGETS);
            for(int i = 0; i < uuids.length(); i++)
            {
                gadgets.add(UUID.fromString(uuids.getString(i)));
            }
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
        return new Room(id, name, color, gadgets);
    }

    private JSONObject toJSONObject(Room room) {
        JSONObject result = new JSONObject();
        try
        {
            result.put(Room.ID, room.getId());
            result.put(Room.NAME, room.getName());
            result.put(Room.COLOR, room.getColor());
            JSONArray array = new JSONArray(room.getGadgets());
            result.put(Room.GADGETS, array);
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
        return result;
    }
}
