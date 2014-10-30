package com.kms.alexandracentralunit;


/**
 * Created by Mateusz Zasoński on 2014-10-16.
 */
public class SQLiteRoomRepository {

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

}
