package com.kms.alexandracentralunit.data.database.sqlite;


import android.content.Context;

import com.kms.alexandracentralunit.data.database.RoomRepository;
import com.kms.alexandracentralunit.data.model.Room;

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
        return false;
    }

    @Override
    public boolean delete(Room room) {
        return false;
    }

    @Override
    public boolean update(Room room) {
        return false;
    }

    @Override
    public Room find(UUID id) {
        return null;
    }

    @Override
    public List<Room> getAll() {
        return null;
    }

}
