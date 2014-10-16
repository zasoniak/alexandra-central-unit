package com.kms.alexandracentralunit;


import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Mateusz Zasoński on 2014-10-14.
 */
public class SQLiteModuleRepository implements ModuleRepository {

    private static final String TAG = "SQLiteModuleRepository";
    // Devices table name
    private static final String TABLE_NAME = "modules";
    public static final String SQL_DROP_TABLE = "DROP TABLE IF EXISTS "+TABLE_NAME;
    private static final String COMMA_SEP = ", ";
    // Devices Table Columns names
    private static final String KEY_MODULE_ID = "_id";
    private static final String KEY_MODULE_ID_TYPE = "INTEGER PRIMARY KEY";
    private static final String KEY_MODULE_NAME = "name";
    private static final String KEY_MODULE_NAME_TYPE = "TEXT";
    private static final String KEY_MODULE_MAC_ADDRESS = "address";
    private static final String KEY_MODULE_MAC_ADDRESS_TYPE = "TEXT";
    private static final String KEY_MODULE_ROOM = "room_id";
    private static final String KEY_MODULE_ROOM_TYPE = "INTEGER";
    private static final String KEY_MODULE_TYPE = "type";
    private static final String KEY_MODULE_TYPE_TYPE = "INTEGER";
    private static final String KEY_MODULE_SYNCSTATUS = "syncstatus";
    private static final String[] MODULES_COLUMNS = {KEY_MODULE_ID, KEY_MODULE_NAME,
                                                     KEY_MODULE_MAC_ADDRESS, KEY_MODULE_ROOM,
                                                     KEY_MODULE_TYPE, KEY_MODULE_SYNCSTATUS};
    private static final String KEY_MODULE_SYNCSTATUS_TYPE = "INTEGER";
    public static final String SQL_CREATE_TABLE = "CREATE TABLE "+TABLE_NAME+" ("+
            KEY_MODULE_ID+" "+KEY_MODULE_ID_TYPE+COMMA_SEP+
            KEY_MODULE_NAME+" "+KEY_MODULE_NAME_TYPE+COMMA_SEP+
            KEY_MODULE_MAC_ADDRESS+" "+KEY_MODULE_MAC_ADDRESS_TYPE+COMMA_SEP+
            KEY_MODULE_ROOM+" "+KEY_MODULE_ROOM_TYPE+COMMA_SEP+
            KEY_MODULE_TYPE+" "+KEY_MODULE_TYPE_TYPE+COMMA_SEP+
            KEY_MODULE_SYNCSTATUS+" "+KEY_MODULE_SYNCSTATUS_TYPE+")";
    private ConfigurationDatabaseHelper databaseHelper;

    public SQLiteModuleRepository(Context context) {
        databaseHelper = ConfigurationDatabaseHelper.getInstance(context);
    }

    @Override
    public boolean add(Module module) {
        Log.d("Module.add", module.toString());
        SQLiteDatabase sqLiteDatabase = databaseHelper.openDatabase();

        ContentValues values = moduleToContentValues(module);
        long moduleId = sqLiteDatabase.insert(SQLiteModuleRepository.TABLE_NAME, "null", values);
        Log.i(TAG, "Inserted new Module with ID: "+moduleId);

        databaseHelper.closeDatabase();
        return true;
    }

    @Override
    public boolean delete(Module module) {
        Log.d("Module.remove", module.toString());
        SQLiteDatabase sqLiteDatabase = databaseHelper.openDatabase();

        long moduleId = sqLiteDatabase.delete(SQLiteModuleRepository.TABLE_NAME, KEY_MODULE_ID+" = ?", new String[] {
                String.valueOf(module.getId())});
        Log.i(TAG, "Deleted Module with ID: "+moduleId);
        databaseHelper.closeDatabase();

        return true;
    }

    @Override
    public boolean update(Module module) {
        Log.d("Module.update", module.toString());
        SQLiteDatabase sqLiteDatabase = databaseHelper.getWritableDatabase();

        ContentValues values = moduleToContentValues(module);
        long moduleId = sqLiteDatabase.update(SQLiteModuleRepository.TABLE_NAME, values, KEY_MODULE_ID+" = ?", new String[] {
                String.valueOf(module.getId())});
        Log.i(TAG, "Updated Module with ID: "+moduleId);
        sqLiteDatabase.close();

        return true;
    }

    public Module find(long id) {
        ContentValues values = new ContentValues();
        return ModuleFactory.create(values);
    }

    public List<Module> getAll() {
        return new ArrayList<Module>();
    }

    public List<Module> getAllByRoom(long roomID) {
        return new ArrayList<Module>();
    }

    public List<Module> getAllByType(int type) {
        return new ArrayList<Module>();
    }

    private ContentValues moduleToContentValues(Module module) {
        ContentValues values = new ContentValues();
        values.put(SQLiteModuleRepository.KEY_MODULE_ID, module.getId());
        values.put(SQLiteModuleRepository.KEY_MODULE_NAME, module.getName());
        values.put(SQLiteModuleRepository.KEY_MODULE_MAC_ADDRESS, module.getAddress());
        values.put(SQLiteModuleRepository.KEY_MODULE_ROOM, module.getRoom());
        values.put(SQLiteModuleRepository.KEY_MODULE_TYPE, module.getType());
        values.put(SQLiteModuleRepository.KEY_MODULE_SYNCSTATUS, 0);
        return values;
    }

}
