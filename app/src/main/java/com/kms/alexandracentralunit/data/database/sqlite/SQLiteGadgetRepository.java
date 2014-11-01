package com.kms.alexandracentralunit.data.database.sqlite;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.kms.alexandracentralunit.data.GadgetLinker;
import com.kms.alexandracentralunit.data.database.GadgetRepository;
import com.kms.alexandracentralunit.data.model.Gadget;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


/**
 * Created by Mateusz Zasoński on 2014-10-14.
 */
public class SQLiteGadgetRepository implements GadgetRepository {

    // Devices Table Columns names
    // TODO: change data types
    public static final String KEY_GADGET_ID = "_id";
    public static final String KEY_GADGET_NAME = "name";
    public static final String KEY_GADGET_MAC_ADDRESS = "address";
    public static final String KEY_GADGET_ROOM = "room_id";
    public static final String KEY_GADGET_TYPE = "type";
    private static final String TAG = "SQLiteGadgetRepository";
    // Devices table name
    private static final String TABLE_NAME = "gadgets";
    public static final String SQL_DROP_TABLE = "DROP TABLE IF EXISTS "+TABLE_NAME;
    private static final String COMMA_SEP = ", ";
    private static final String KEY_GADGET_ID_TYPE = "TEXT PRIMARY KEY";
    private static final String KEY_GADGET_NAME_TYPE = "TEXT";
    private static final String KEY_GADGET_MAC_ADDRESS_TYPE = "TEXT";
    private static final String KEY_GADGET_ROOM_TYPE = "TEXT";
    private static final String KEY_GADGET_TYPE_TYPE = "INTEGER";
    private static final String KEY_GADGET_UPDATE_TIME = "syncstatus";
    private static final String[] TABLE_COLUMNS = {KEY_GADGET_ID, KEY_GADGET_NAME,
                                                   KEY_GADGET_MAC_ADDRESS, KEY_GADGET_ROOM,
                                                   KEY_GADGET_TYPE, KEY_GADGET_UPDATE_TIME};
    private static final String KEY_GADGET_UPDATE_TIME_TYPE = "INTEGER";
    public static final String SQL_CREATE_TABLE = "CREATE TABLE "+TABLE_NAME+" ("+
            KEY_GADGET_ID+" "+KEY_GADGET_ID_TYPE+COMMA_SEP+
            KEY_GADGET_NAME+" "+KEY_GADGET_NAME_TYPE+COMMA_SEP+
            KEY_GADGET_MAC_ADDRESS+" "+KEY_GADGET_MAC_ADDRESS_TYPE+COMMA_SEP+
            KEY_GADGET_ROOM+" "+KEY_GADGET_ROOM_TYPE+COMMA_SEP+
            KEY_GADGET_TYPE+" "+KEY_GADGET_TYPE_TYPE+COMMA_SEP+
            KEY_GADGET_UPDATE_TIME+" "+KEY_GADGET_UPDATE_TIME_TYPE+")";
    private ConfigurationDatabaseHelper databaseHelper;
    private GadgetLinker linker;

    public SQLiteGadgetRepository(Context context) {
        databaseHelper = ConfigurationDatabaseHelper.getInstance(context);
        linker = GadgetLinker.getInstance(context);
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
        SQLiteDatabase sqLiteDatabase = databaseHelper.openDatabase();

        ContentValues values = gadgetToContentValues(gadget);
        long gadgetId = sqLiteDatabase.update(SQLiteGadgetRepository.TABLE_NAME, values, KEY_GADGET_ID+" = ?", new String[] {
                String.valueOf(gadget.getId())});
        Log.i(TAG, "Updated Gadget with ID: "+gadgetId);
        databaseHelper.closeDatabase();

        return true;
    }

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
        ContentValues values = new ContentValues();
        if(cursor != null)
        {
            cursor.moveToFirst();
            values.put(KEY_GADGET_ID, cursor.getLong(0));
            values.put(KEY_GADGET_NAME, cursor.getString(1));
            values.put(KEY_GADGET_MAC_ADDRESS, cursor.getString(2));
            values.put(KEY_GADGET_ROOM, cursor.getLong(3));
            values.put(KEY_GADGET_TYPE, cursor.getInt(4));
        }
        // close database connection and release resources
        databaseHelper.closeDatabase();
        cursor.close();
        // return Gadget (new or existing)
        return linker.find(values);
    }

    public List<Gadget> getAll() {
        // obtain thread-safe database access
        SQLiteDatabase sqLiteDatabase = databaseHelper.openDatabase();

        // build a query
        String query = "SELECT * FROM "+TABLE_NAME+"ORDER BY "+KEY_GADGET_NAME;
        Cursor cursor = sqLiteDatabase.rawQuery(query, null);

        // prepare structured data
        ArrayList<ContentValues> values = new ArrayList<ContentValues>();
        if(cursor.moveToFirst())
        {
            do
            {
                ContentValues tempValues = new ContentValues();
                tempValues.put(KEY_GADGET_ID, cursor.getLong(0));
                tempValues.put(KEY_GADGET_NAME, cursor.getString(1));
                tempValues.put(KEY_GADGET_MAC_ADDRESS, cursor.getString(2));
                tempValues.put(KEY_GADGET_ROOM, cursor.getLong(3));
                tempValues.put(KEY_GADGET_TYPE, cursor.getInt(4));
                values.add(tempValues);
            } while(cursor.moveToNext());
        }
        // close database connection and release resources
        databaseHelper.closeDatabase();
        cursor.close();
        // return Gadget (new or existing)
        return linker.getAll(values);
    }

    public List<Gadget> getAllByRoom(UUID roomID) {
        // obtain thread-safe database access
        SQLiteDatabase sqLiteDatabase = databaseHelper.openDatabase();

        // build a query
        Cursor cursor = sqLiteDatabase.query(TABLE_NAME, // a. table
                TABLE_COLUMNS, // b. column names
                KEY_GADGET_ROOM+" = ?", // c. selections
                new String[] {roomID.toString()}, // d. selections args
                null, // e. group by
                null, // f. having
                null, // g. order by
                null); // h. limit

        // prepare structured data
        ArrayList<Gadget> gadgets = new ArrayList<Gadget>();
        if(cursor.moveToFirst())
        {
            do
            {
                ContentValues values = new ContentValues();
                values.put(KEY_GADGET_ID, cursor.getLong(0));
                values.put(KEY_GADGET_NAME, cursor.getString(1));
                values.put(KEY_GADGET_MAC_ADDRESS, cursor.getString(2));
                values.put(KEY_GADGET_ROOM, cursor.getLong(3));
                values.put(KEY_GADGET_TYPE, cursor.getInt(4));
                gadgets.add(linker.find(values));
            } while(cursor.moveToNext());
        }
        // close database connection and release resources
        databaseHelper.closeDatabase();
        cursor.close();
        // return gadgets list
        return gadgets;
    }

    public List<Gadget> getAllByType(int type) {
        // obtain thread-safe database access
        SQLiteDatabase sqLiteDatabase = databaseHelper.openDatabase();

        // build a query
        Cursor cursor = sqLiteDatabase.query(TABLE_NAME, // a. table
                TABLE_COLUMNS, // b. column names
                KEY_GADGET_TYPE+" = ?", // c. selections
                new String[] {String.valueOf(type)}, // d. selections args
                null, // e. group by
                null, // f. having
                null, // g. order by
                null); // h. limit

        // prepare structured data
        ArrayList<Gadget> gadgets = new ArrayList<Gadget>();
        if(cursor.moveToFirst())
        {
            do
            {
                ContentValues values = new ContentValues();
                values.put(KEY_GADGET_ID, cursor.getLong(0));
                values.put(KEY_GADGET_NAME, cursor.getString(1));
                values.put(KEY_GADGET_MAC_ADDRESS, cursor.getString(2));
                values.put(KEY_GADGET_ROOM, cursor.getLong(3));
                values.put(KEY_GADGET_TYPE, cursor.getInt(4));
                gadgets.add(linker.find(values));
            } while(cursor.moveToNext());
        }
        // close database connection and release resources
        databaseHelper.closeDatabase();
        cursor.close();
        // return gadgets list
        return gadgets;
    }

    private ContentValues gadgetToContentValues(Gadget gadget) {
        ContentValues values = new ContentValues();
        values.put(SQLiteGadgetRepository.KEY_GADGET_ID, gadget.getId());
        values.put(SQLiteGadgetRepository.KEY_GADGET_NAME, gadget.getName());
        values.put(SQLiteGadgetRepository.KEY_GADGET_MAC_ADDRESS, gadget.getAddress());
        values.put(SQLiteGadgetRepository.KEY_GADGET_ROOM, gadget.getRoom());
        values.put(SQLiteGadgetRepository.KEY_GADGET_TYPE, gadget.getType());
        values.put(SQLiteGadgetRepository.KEY_GADGET_UPDATE_TIME, 0);
        return values;
    }

}
