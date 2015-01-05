package com.kms.alexandra.data.database.json;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.concurrent.atomic.AtomicInteger;


/**
 * @author Mateusz Zasoński
 */
public class JSONConfigurationDatabaseHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "JSONConfigurationDatabase";

    private static JSONConfigurationDatabaseHelper instance;
    private AtomicInteger threadCounter = new AtomicInteger();
    private SQLiteDatabase database;

    private JSONConfigurationDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public static synchronized JSONConfigurationDatabaseHelper getInstance(Context context) {
        if(instance == null)
        {
            instance = new JSONConfigurationDatabaseHelper(context.getApplicationContext());
        }
        return instance;
    }

    //managing open database connections
    public synchronized SQLiteDatabase openDatabase() {
        if(threadCounter.incrementAndGet() == 1)
        {
            // Opening new database
            database = instance.getWritableDatabase();
        }
        return database;
    }

    //not allowing to close database before all threads end up theirs work
    public synchronized void closeDatabase() {
        if(threadCounter.decrementAndGet() == 0)
        {
            // Closing database
            database.close();
        }
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(JSONRoomRepository.SQL_CREATE_TABLE);
        sqLiteDatabase.execSQL(JSONGadgetRepository.SQL_CREATE_TABLE);
        sqLiteDatabase.execSQL(JSONSceneRepository.SQL_CREATE_TABLE);
        sqLiteDatabase.execSQL(JSONScheduleRepository.SQL_CREATE_TABLE);
        sqLiteDatabase.execSQL(JSONUserRepository.SQL_CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i2) {
        sqLiteDatabase.execSQL(JSONRoomRepository.SQL_DROP_TABLE);
        sqLiteDatabase.execSQL(JSONGadgetRepository.SQL_DROP_TABLE);
        sqLiteDatabase.execSQL(JSONSceneRepository.SQL_DROP_TABLE);
        sqLiteDatabase.execSQL(JSONScheduleRepository.SQL_DROP_TABLE);
        sqLiteDatabase.execSQL(JSONUserRepository.SQL_DROP_TABLE);
        onCreate(sqLiteDatabase);
    }
}
