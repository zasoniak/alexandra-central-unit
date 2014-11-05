package com.kms.alexandracentralunit.data.database.sqlite;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.kms.alexandracentralunit.data.RoomFactory;
import com.kms.alexandracentralunit.data.database.RoomRepository;
import com.kms.alexandracentralunit.data.model.Room;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


/**
 * Created by Mateusz Zasoński on 2014-10-16.
 */
public class SQLiteRoomRepository implements RoomRepository {

    // Devices Table Columns names
    public static final String KEY_ROOM_ID = "_id";
    public static final String KEY_ROOM_SYSTEM = "system_id";
    public static final String KEY_ROOM_NAME = "name";
    public static final String KEY_ROOM_COLOR = "color";
    public static final String KEY_ROOM_CREATED = "created_at";
    public static final String KEY_ROOM_UPDATED = "updated_at";
    public static final String KEY_ROOM_CREATED_BY = "created_by";

    private static final String[] TABLE_COLUMNS = {KEY_ROOM_ID, KEY_ROOM_SYSTEM, KEY_ROOM_NAME,
                                                   KEY_ROOM_COLOR, KEY_ROOM_CREATED,
                                                   KEY_ROOM_UPDATED, KEY_ROOM_CREATED_BY};
    private static final String TAG = "SQLiteRoomRepository";
    // Devices table name
    private static final String TABLE_NAME = "rooms";
    public static final String SQL_DROP_TABLE = "DROP TABLE IF EXISTS "+TABLE_NAME;
    private static final String COMMA_SEP = ", ";
    private static final String KEY_ROOM_ID_TYPE = "TEXT";
    private static final String KEY_ROOM_SYSTEM_TYPE = "TEXT";
    private static final String KEY_ROOM_NAME_TYPE = "TEXT";
    private static final String KEY_ROOM_COLOR_TYPE = "INTEGER";
    private static final String KEY_ROOM_CREATED_TYPE = "DATETIME DEFAULT CURRENT_TIMESTAMP";
    private static final String KEY_ROOM_UPDATED_TYPE = "DATETIME DEFAULT CURRENT_TIMESTAMP";
    private static final String KEY_ROOM_CREATED_BY_TYPE = "INTEGER";

    public static final String SQL_CREATE_TABLE = "CREATE TABLE "+TABLE_NAME+" ("+
            KEY_ROOM_ID+" "+KEY_ROOM_ID_TYPE+COMMA_SEP+
            KEY_ROOM_SYSTEM+" "+KEY_ROOM_SYSTEM_TYPE+COMMA_SEP+
            KEY_ROOM_NAME+" "+KEY_ROOM_NAME_TYPE+COMMA_SEP+
            KEY_ROOM_COLOR+" "+KEY_ROOM_COLOR_TYPE+COMMA_SEP+
            KEY_ROOM_CREATED+" "+KEY_ROOM_CREATED_TYPE+COMMA_SEP+
            KEY_ROOM_UPDATED+" "+KEY_ROOM_UPDATED_TYPE+COMMA_SEP+
            KEY_ROOM_CREATED_BY+" "+KEY_ROOM_CREATED_BY_TYPE+")";

    private ConfigurationDatabaseHelper databaseHelper;

    public SQLiteRoomRepository(Context context) {
        databaseHelper = ConfigurationDatabaseHelper.getInstance(context);
    }

    @Override
    public boolean add(Room room) {
        Log.d("Room.add", room.toString());
        SQLiteDatabase sqLiteDatabase = databaseHelper.openDatabase();

        String query = "INSERT INTO "+TABLE_NAME+" ("+
                KEY_ROOM_ID+COMMA_SEP+
                KEY_ROOM_SYSTEM+COMMA_SEP+
                KEY_ROOM_NAME+COMMA_SEP+
                KEY_ROOM_COLOR+COMMA_SEP+
                KEY_ROOM_CREATED_BY+") "+"values"+" ("+
                "\'"+room.getId().toString()+"\'"+COMMA_SEP+
                "\'"+String.valueOf(room.getSystemId())+"\'"+COMMA_SEP+
                "\'"+room.getName()+"\'"+COMMA_SEP+
                "\'"+String.valueOf(room.getColor())+"\'"+COMMA_SEP+
                "\'"+"Sony"+"\'"+");";

        sqLiteDatabase.execSQL(query);
        Log.i(TAG, "Inserted new Room: "+room.toString());

        databaseHelper.closeDatabase();
        return true;
    }

    @Override
    public boolean delete(Room room) {
        //TODO: uzupelnic
        return false;
    }

    @Override
    public boolean update(Room room) {
        Log.d("Room.update", room.toString());
        SQLiteDatabase sqLiteDatabase = databaseHelper.openDatabase();

        String query = "UPDATE "+TABLE_NAME+" SET "+
                KEY_ROOM_SYSTEM+" = "+String.valueOf(room.getSystemId())+COMMA_SEP+
                KEY_ROOM_NAME+" = "+room.getName()+COMMA_SEP+
                KEY_ROOM_COLOR+" = "+String.valueOf(room.getColor())+
                KEY_ROOM_UPDATED+" = "+ConfigurationDatabaseHelper.SQL_CURRENT_TIMESTAMP+
                " WHERE "+KEY_ROOM_ID+" = "+"\'"+room.getId().toString()+"\'"+");";

        sqLiteDatabase.execSQL(query);
        Log.i(TAG, "Updated Room: "+room.toString());
        databaseHelper.closeDatabase();
        return true;
    }

    @Override
    public Room find(UUID id) {
        // obtain thread-safe database access
        SQLiteDatabase sqLiteDatabase = databaseHelper.openDatabase();

        // build a query
        Cursor cursor = sqLiteDatabase.query(TABLE_NAME, // a. table
                TABLE_COLUMNS, // b. column names
                KEY_ROOM_ID+" = ?", // c. selections
                new String[] {id.toString()}, // d. selections args
                null, // e. group by
                null, // f. having
                null, // g. order by
                null); // h. limit

        // prepare structured data
        ContentValues values = new ContentValues();
        if(cursor != null)
        {
            cursor.moveToFirst();
            values.put(KEY_ROOM_ID, cursor.getString(0));
            values.put(KEY_ROOM_SYSTEM, cursor.getLong(1));
            values.put(KEY_ROOM_NAME, cursor.getString(2));
            values.put(KEY_ROOM_COLOR, cursor.getInt(3));
        }
        // close database connection and release resources
        databaseHelper.closeDatabase();
        cursor.close();
        return RoomFactory.create(values);
    }

    @Override
    public List<Room> getAll() {
        // obtain thread-safe database access
        SQLiteDatabase sqLiteDatabase = databaseHelper.openDatabase();

        // build a query
        String query = "SELECT * FROM "+TABLE_NAME+" ORDER BY "+KEY_ROOM_NAME;
        Cursor cursor = sqLiteDatabase.rawQuery(query, null);

        // prepare structured data
        ArrayList<Room> rooms = new ArrayList<Room>();
        if(cursor.moveToFirst())
        {
            do
            {
                ContentValues values = new ContentValues();
                values.put(KEY_ROOM_ID, cursor.getString(0));
                values.put(KEY_ROOM_SYSTEM, cursor.getLong(1));
                values.put(KEY_ROOM_NAME, cursor.getString(2));
                values.put(KEY_ROOM_COLOR, cursor.getInt(3));
                rooms.add(RoomFactory.create(values));
            } while(cursor.moveToNext());
        }
        // close database connection and release resources
        databaseHelper.closeDatabase();
        cursor.close();
        // return rooms
        return rooms;
    }

}
