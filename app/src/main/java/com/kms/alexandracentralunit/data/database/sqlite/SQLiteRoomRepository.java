package com.kms.alexandracentralunit.data.database.sqlite;


import android.content.Context;

import com.kms.alexandracentralunit.data.database.RoomRepository;
import com.kms.alexandracentralunit.data.model.Room;

import java.util.List;


/**
 * Created by Mateusz Zasoński on 2014-10-16.
 */
public class SQLiteRoomRepository implements RoomRepository {

    private static final String TAG = "SQLiteRoomRepository";
    // Devices table name
    private static final String TABLE_NAME = "rooms";
    public static final String SQL_DROP_TABLE = "DROP TABLE IF EXISTS "+TABLE_NAME;
    private static final String COMMA_SEP = ", ";
    // Devices Table Columns names
    private static final String KEY_ROOM_ID = "_id";
    private static final String KEY_ROOM_ID_TYPE = "INTEGER PRIMARY KEY";
    private static final String KEY_ROOM_NAME = "name";
    private static final String KEY_ROOM_NAME_TYPE = "TEXT";
    private static final String KEY_ROOM_TYPE = "type";
    private static final String KEY_ROOM_TYPE_TYPE = "INTEGER";
    private static final String KEY_ROOM_SYNCSTATUS = "syncstatus";
    private static final String[] ROOMS_COLUMNS = {KEY_ROOM_ID, KEY_ROOM_NAME, KEY_ROOM_TYPE,
                                                   KEY_ROOM_SYNCSTATUS};
    private static final String KEY_ROOM_SYNCSTATUS_TYPE = "INTEGER";
    public static final String SQL_CREATE_TABLE = "CREATE TABLE "+TABLE_NAME+" ("+
            KEY_ROOM_ID+" "+KEY_ROOM_ID_TYPE+COMMA_SEP+
            KEY_ROOM_NAME+" "+KEY_ROOM_NAME_TYPE+COMMA_SEP+
            KEY_ROOM_TYPE+" "+KEY_ROOM_TYPE_TYPE+COMMA_SEP+
            KEY_ROOM_SYNCSTATUS+" "+KEY_ROOM_SYNCSTATUS_TYPE+")";
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
    public Room find(long id) {
        return null;
    }

    @Override
    public List<Room> getAll() {
        return null;
    }

}
