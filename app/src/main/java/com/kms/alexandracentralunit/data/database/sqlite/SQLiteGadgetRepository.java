package com.kms.alexandracentralunit.data.database.sqlite;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.kms.alexandracentralunit.CoreService;
import com.kms.alexandracentralunit.data.GadgetFactory;
import com.kms.alexandracentralunit.data.database.GadgetRepository;
import com.kms.alexandracentralunit.data.model.Gadget;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


/**
 * Created by Mateusz Zasoński on 2014-10-14.
 */
public class SQLiteGadgetRepository implements GadgetRepository {

    // Gadgets table columns names
    public static final String KEY_GADGET_ID = "_id";
    public static final String KEY_GADGET_SYSTEM = "system_id";
    public static final String KEY_GADGET_ROOM = "room_id";
    public static final String KEY_GADGET_NAME = "name";
    public static final String KEY_GADGET_MAC_ADDRESS = "mac";
    public static final String KEY_GADGET_TYPE = "type";
    public static final String KEY_GADGET_UPDATED = "updated_at";
    // Gadgets table column array
    private static final String[] TABLE_COLUMNS = {KEY_GADGET_ID, KEY_GADGET_SYSTEM,
                                                   KEY_GADGET_ROOM, KEY_GADGET_NAME,
                                                   KEY_GADGET_MAC_ADDRESS, KEY_GADGET_TYPE,
                                                   KEY_GADGET_UPDATED};
    private static final String TAG = "SQLiteGadgetRepository";
    // Gadgets table name
    private static final String TABLE_NAME = "gadgets";
    // Predefined SQL statements
    public static final String SQL_DROP_TABLE = "DROP TABLE IF EXISTS "+TABLE_NAME;
    // Gadgets table columns types
    private static final String COMMA_SEP = ", ";
    private static final String KEY_GADGET_ID_TYPE = "TEXT PRIMARY KEY";
    private static final String KEY_GADGET_SYSTEM_TYPE = "INTEGER";
    private static final String KEY_GADGET_ROOM_TYPE = "TEXT";
    private static final String KEY_GADGET_NAME_TYPE = "TEXT";
    private static final String KEY_GADGET_MAC_ADDRESS_TYPE = "TEXT";
    private static final String KEY_GADGET_TYPE_TYPE = "INTEGER";
    private static final String KEY_GADGET_UPDATED_TYPE = "DATETIME DEFAULT CURRENT_TIMESTAMP";

    public static final String SQL_CREATE_TABLE = "CREATE TABLE "+TABLE_NAME+" ("+
            KEY_GADGET_ID+" "+KEY_GADGET_ID_TYPE+COMMA_SEP+
            KEY_GADGET_SYSTEM+" "+KEY_GADGET_SYSTEM_TYPE+COMMA_SEP+
            KEY_GADGET_ROOM+" "+KEY_GADGET_ROOM_TYPE+COMMA_SEP+
            KEY_GADGET_NAME+" "+KEY_GADGET_NAME_TYPE+COMMA_SEP+
            KEY_GADGET_MAC_ADDRESS+" "+KEY_GADGET_MAC_ADDRESS_TYPE+COMMA_SEP+
            KEY_GADGET_TYPE+" "+KEY_GADGET_TYPE_TYPE+COMMA_SEP+
            KEY_GADGET_UPDATED+" "+KEY_GADGET_UPDATED_TYPE+")";

    //TODO: implement timestamp!!!
    // class implementation
    private ConfigurationDatabaseHelper databaseHelper;

    public SQLiteGadgetRepository(Context context) {
        databaseHelper = ConfigurationDatabaseHelper.getInstance(CoreService.getContext());

    }

    @Override
    public boolean add(Gadget gadget) {
        Log.d("Gadget.add", gadget.toString());
        SQLiteDatabase sqLiteDatabase = databaseHelper.openDatabase();

        String query = "INSERT INTO "+TABLE_NAME+" ("+
                KEY_GADGET_ID+COMMA_SEP+
                KEY_GADGET_SYSTEM+COMMA_SEP+
                KEY_GADGET_ROOM+COMMA_SEP+
                KEY_GADGET_NAME+COMMA_SEP+
                KEY_GADGET_MAC_ADDRESS+COMMA_SEP+
                KEY_GADGET_TYPE+") "+"values"+" ("+
                "\'"+gadget.getId().toString()+"\'"+COMMA_SEP+
                "\'"+String.valueOf(gadget.getSystem())+"\'"+COMMA_SEP+
                "\'"+String.valueOf(gadget.getRoom())+"\'"+COMMA_SEP+
                "\'"+gadget.getName()+"\'"+COMMA_SEP+
                "\'"+gadget.getAddress()+"\'"+COMMA_SEP+
                "\'"+String.valueOf(gadget.getType())+"\'"+");";

        sqLiteDatabase.execSQL(query);
        Log.i(TAG, "Inserted new Gadget with ID: "+gadget.getId().toString());

        databaseHelper.closeDatabase();
        return true;
    }

    @Override
    public boolean delete(Gadget gadget) {
        //TODO: update for UUID and softdelete
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

        String query = "UPDATE "+TABLE_NAME+" SET "+
                KEY_GADGET_SYSTEM+" = "+String.valueOf(gadget.getSystem())+COMMA_SEP+
                KEY_GADGET_ROOM+" = "+String.valueOf(gadget.getRoom())+COMMA_SEP+
                KEY_GADGET_NAME+" = "+gadget.getName()+COMMA_SEP+
                KEY_GADGET_MAC_ADDRESS+" = "+gadget.getAddress()+COMMA_SEP+
                KEY_GADGET_TYPE+" = "+String.valueOf(gadget.getType())+
                KEY_GADGET_UPDATED+" = "+ConfigurationDatabaseHelper.SQL_CURRENT_TIMESTAMP+
                " WHERE "+KEY_GADGET_ID+" = "+"\'"+gadget.getId().toString()+"\'"+");";

        sqLiteDatabase.execSQL(query);
        Log.i(TAG, "Updated Gadget with ID: "+gadget.getId().toString());
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
            values.put(KEY_GADGET_ID, cursor.getString(0));
            values.put(KEY_GADGET_SYSTEM, cursor.getLong(1));
            values.put(KEY_GADGET_ROOM, cursor.getString(2));
            values.put(KEY_GADGET_NAME, cursor.getString(3));
            values.put(KEY_GADGET_MAC_ADDRESS, cursor.getString(4));
            values.put(KEY_GADGET_TYPE, cursor.getInt(5));
        }
        // close database connection and release resources
        databaseHelper.closeDatabase();
        cursor.close();
        return GadgetFactory.create(values);
    }

    public List<Gadget> getAll() {
        // obtain thread-safe database access
        SQLiteDatabase sqLiteDatabase = databaseHelper.openDatabase();

        // build a query
        String query = "SELECT * FROM "+TABLE_NAME+" ORDER BY "+KEY_GADGET_NAME;
        Cursor cursor = sqLiteDatabase.rawQuery(query, null);

        // prepare structured data
        ArrayList<Gadget> gadgets = new ArrayList<Gadget>();
        if(cursor.moveToFirst())
        {
            do
            {
                ContentValues values = new ContentValues();
                values.put(KEY_GADGET_ID, cursor.getString(0));
                values.put(KEY_GADGET_SYSTEM, cursor.getLong(1));
                values.put(KEY_GADGET_ROOM, cursor.getString(2));
                values.put(KEY_GADGET_NAME, cursor.getString(3));
                values.put(KEY_GADGET_MAC_ADDRESS, cursor.getString(4));
                values.put(KEY_GADGET_TYPE, cursor.getInt(5));
                gadgets.add(GadgetFactory.create(values));
            } while(cursor.moveToNext());
        }
        // close database connection and release resources
        databaseHelper.closeDatabase();
        cursor.close();
        // return Gadget (new or existing)
        return gadgets;
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
                values.put(KEY_GADGET_ID, cursor.getString(0));
                values.put(KEY_GADGET_SYSTEM, cursor.getLong(1));
                values.put(KEY_GADGET_ROOM, cursor.getString(2));
                values.put(KEY_GADGET_NAME, cursor.getString(3));
                values.put(KEY_GADGET_MAC_ADDRESS, cursor.getString(4));
                values.put(KEY_GADGET_TYPE, cursor.getInt(5));
                gadgets.add(GadgetFactory.create(values));
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
                values.put(KEY_GADGET_ID, cursor.getString(0));
                values.put(KEY_GADGET_SYSTEM, cursor.getLong(1));
                values.put(KEY_GADGET_ROOM, cursor.getString(2));
                values.put(KEY_GADGET_NAME, cursor.getString(3));
                values.put(KEY_GADGET_MAC_ADDRESS, cursor.getString(4));
                values.put(KEY_GADGET_TYPE, cursor.getInt(5));
                gadgets.add(GadgetFactory.create(values));
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
        values.put(SQLiteGadgetRepository.KEY_GADGET_ID, gadget.getId().toString());
        values.put(SQLiteGadgetRepository.KEY_GADGET_NAME, gadget.getName());
        values.put(SQLiteGadgetRepository.KEY_GADGET_MAC_ADDRESS, gadget.getAddress());
        values.put(SQLiteGadgetRepository.KEY_GADGET_ROOM, String.valueOf(gadget.getRoom()));
        values.put(SQLiteGadgetRepository.KEY_GADGET_TYPE, gadget.getType());
        values.put(SQLiteGadgetRepository.KEY_GADGET_UPDATED, 0);
        return values;
    }
}
