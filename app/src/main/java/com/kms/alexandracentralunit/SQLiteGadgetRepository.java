package com.kms.alexandracentralunit;


import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.kms.alexandracentralunit.Database.ConfigurationDatabaseHelper;
import com.kms.alexandracentralunit.Models.Gadget;
import com.kms.alexandracentralunit.Repositories.GadgetRepository;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Mateusz Zasoński on 2014-10-14.
 */
public class SQLiteGadgetRepository implements GadgetRepository {

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

    public SQLiteGadgetRepository(Context context) {
        databaseHelper = ConfigurationDatabaseHelper.getInstance(context);
    }

    @Override
    public boolean add(Gadget gadget) {
        Log.d("Module.add", gadget.toString());
        SQLiteDatabase sqLiteDatabase = databaseHelper.openDatabase();

        ContentValues values = moduleToContentValues(gadget);
        long moduleId = sqLiteDatabase.insert(SQLiteGadgetRepository.TABLE_NAME, "null", values);
        Log.i(TAG, "Inserted new Module with ID: "+moduleId);

        databaseHelper.closeDatabase();
        return true;
    }

    @Override
    public boolean delete(Gadget gadget) {
        Log.d("Module.remove", gadget.toString());
        SQLiteDatabase sqLiteDatabase = databaseHelper.openDatabase();

        long moduleId = sqLiteDatabase.delete(SQLiteGadgetRepository.TABLE_NAME, KEY_MODULE_ID+" = ?", new String[] {
                String.valueOf(gadget.getId())});
        Log.i(TAG, "Deleted Module with ID: "+moduleId);
        databaseHelper.closeDatabase();

        return true;
    }

    @Override
    public boolean update(Gadget gadget) {
        Log.d("Module.update", gadget.toString());
        SQLiteDatabase sqLiteDatabase = databaseHelper.getWritableDatabase();

        ContentValues values = moduleToContentValues(gadget);
        long moduleId = sqLiteDatabase.update(SQLiteGadgetRepository.TABLE_NAME, values, KEY_MODULE_ID+" = ?", new String[] {
                String.valueOf(gadget.getId())});
        Log.i(TAG, "Updated Module with ID: "+moduleId);
        sqLiteDatabase.close();

        return true;
    }

    public Gadget find(long id) {
        ContentValues values = new ContentValues();
        return GadgetFactory.create(values);
    }

    public List<Gadget> getAll() {
        return new ArrayList<Gadget>();
    }

    public List<Gadget> getAllByRoom(long roomID) {
        return new ArrayList<Gadget>();
    }

    public List<Gadget> getAllByType(int type) {
        return new ArrayList<Gadget>();
    }

    private ContentValues moduleToContentValues(Gadget gadget) {
        ContentValues values = new ContentValues();
        values.put(SQLiteGadgetRepository.KEY_MODULE_ID, gadget.getId());
        values.put(SQLiteGadgetRepository.KEY_MODULE_NAME, gadget.getName());
        values.put(SQLiteGadgetRepository.KEY_MODULE_MAC_ADDRESS, gadget.getAddress());
        values.put(SQLiteGadgetRepository.KEY_MODULE_ROOM, gadget.getRoom());
        values.put(SQLiteGadgetRepository.KEY_MODULE_TYPE, gadget.getType());
        values.put(SQLiteGadgetRepository.KEY_MODULE_SYNCSTATUS, 0);
        return values;
    }

}
