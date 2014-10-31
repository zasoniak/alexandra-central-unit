package com.kms.alexandracentralunit;


import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.kms.alexandracentralunit.Database.ConfigurationDatabaseHelper;
import com.kms.alexandracentralunit.model.Gadget;
import com.kms.alexandracentralunit.repository.GadgetRepository;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Mateusz Zasoński on 2014-10-14.
 */
public class SQLiteGadgetRepository implements GadgetRepository {

    private static final String TAG = "SQLiteGadgetRepository";
    // Devices table name
    private static final String TABLE_NAME = "gadgets";
    public static final String SQL_DROP_TABLE = "DROP TABLE IF EXISTS "+TABLE_NAME;
    private static final String COMMA_SEP = ", ";
    // Devices Table Columns names
    private static final String KEY_GADGET_ID = "_id";
    private static final String KEY_GADGET_ID_TYPE = "INTEGER PRIMARY KEY";
    private static final String KEY_GADGET_NAME = "name";
    private static final String KEY_GADGET_NAME_TYPE = "TEXT";
    private static final String KEY_GADGET_MAC_ADDRESS = "address";
    private static final String KEY_GADGET_MAC_ADDRESS_TYPE = "TEXT";
    private static final String KEY_GADGET_ROOM = "room_id";
    private static final String KEY_GADGET_ROOM_TYPE = "INTEGER";
    private static final String KEY_GADGET_TYPE = "type";
    private static final String KEY_GADGET_TYPE_TYPE = "INTEGER";
    private static final String KEY_GADGET_SYNCSTATUS = "syncstatus";
    private static final String[] GADGETS_COLUMNS = {KEY_GADGET_ID, KEY_GADGET_NAME,
                                                     KEY_GADGET_MAC_ADDRESS, KEY_GADGET_ROOM,
                                                     KEY_GADGET_TYPE, KEY_GADGET_SYNCSTATUS};
    private static final String KEY_GADGET_SYNCSTATUS_TYPE = "INTEGER";
    public static final String SQL_CREATE_TABLE = "CREATE TABLE "+TABLE_NAME+" ("+
            KEY_GADGET_ID+" "+KEY_GADGET_ID_TYPE+COMMA_SEP+
            KEY_GADGET_NAME+" "+KEY_GADGET_NAME_TYPE+COMMA_SEP+
            KEY_GADGET_MAC_ADDRESS+" "+KEY_GADGET_MAC_ADDRESS_TYPE+COMMA_SEP+
            KEY_GADGET_ROOM+" "+KEY_GADGET_ROOM_TYPE+COMMA_SEP+
            KEY_GADGET_TYPE+" "+KEY_GADGET_TYPE_TYPE+COMMA_SEP+
            KEY_GADGET_SYNCSTATUS+" "+KEY_GADGET_SYNCSTATUS_TYPE+")";
    private ConfigurationDatabaseHelper databaseHelper;

    public SQLiteGadgetRepository(Context context) {
        databaseHelper = ConfigurationDatabaseHelper.getInstance(context);
    }

    @Override
    public boolean add(Gadget gadget) {
        Log.d("Gadget.add", gadget.toString());
        SQLiteDatabase sqLiteDatabase = databaseHelper.openDatabase();

        ContentValues values = gadgetToContentValues(gadget);
        long gadgetId = sqLiteDatabase.insert(SQLiteGadgetRepository.TABLE_NAME, "null", values);
        Log.i(TAG, "Inserted new Gadget with ID: "+gadgetId);

        databaseHelper.closeDatabase();
        return true;
    }

    @Override
    public boolean delete(Gadget gadget) {
        Log.d("Gadget.remove", gadget.toString());
        SQLiteDatabase sqLiteDatabase = databaseHelper.openDatabase();

        long gadgetId = sqLiteDatabase.delete(SQLiteGadgetRepository.TABLE_NAME, KEY_GADGET_ID+" = ?", new String[] {
                String.valueOf(gadget.getId())});
        Log.i(TAG, "Deleted Gadget with ID: "+gadgetId);
        databaseHelper.closeDatabase();

        return true;
    }

    @Override
    public boolean update(Gadget gadget) {
        Log.d("Gadget.update", gadget.toString());
        SQLiteDatabase sqLiteDatabase = databaseHelper.getWritableDatabase();

        ContentValues values = gadgetToContentValues(gadget);
        long gadgetId = sqLiteDatabase.update(SQLiteGadgetRepository.TABLE_NAME, values, KEY_GADGET_ID+" = ?", new String[] {
                String.valueOf(gadget.getId())});
        Log.i(TAG, "Updated Gadget with ID: "+gadgetId);
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

    private ContentValues gadgetToContentValues(Gadget gadget) {
        ContentValues values = new ContentValues();
        values.put(SQLiteGadgetRepository.KEY_GADGET_ID, gadget.getId());
        values.put(SQLiteGadgetRepository.KEY_GADGET_NAME, gadget.getName());
        values.put(SQLiteGadgetRepository.KEY_GADGET_MAC_ADDRESS, gadget.getAddress());
        values.put(SQLiteGadgetRepository.KEY_GADGET_ROOM, gadget.getRoom());
        values.put(SQLiteGadgetRepository.KEY_GADGET_TYPE, gadget.getType());
        values.put(SQLiteGadgetRepository.KEY_GADGET_SYNCSTATUS, 0);
        return values;
    }

}
