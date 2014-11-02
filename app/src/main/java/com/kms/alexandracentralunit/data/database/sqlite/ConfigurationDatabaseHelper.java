package com.kms.alexandracentralunit.data.database.sqlite;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.concurrent.atomic.AtomicInteger;


/**
 * Created by Mateusz Zasoński on 2014-10-14.
 */
public class ConfigurationDatabaseHelper extends SQLiteOpenHelper {

    private static final String TAG = "Configuration Database";

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "configurationDatabase";

    private static ConfigurationDatabaseHelper instance;
    private AtomicInteger threadCounter = new AtomicInteger();
    private SQLiteDatabase database;

    //private constructor for singleton
    private ConfigurationDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    //getting instance with singleton pattern
    public static synchronized ConfigurationDatabaseHelper getInstance(Context context) {
        //Using context to ensure that we don't accidentially leak on Activity's context
        if(instance == null)
        {
            instance = new ConfigurationDatabaseHelper(context.getApplicationContext());
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
        Log.i(TAG, "Creating database ["+DATABASE_NAME+" v."+DATABASE_VERSION+"]...");

        sqLiteDatabase.execSQL(SQLiteGadgetRepository.SQL_CREATE_TABLE);
        sqLiteDatabase.execSQL(SQLiteActionRepository.SQL_CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        Log.i(TAG, "Upgrading database ["+DATABASE_NAME+" v."+oldVersion+"] to ["+DATABASE_NAME+" v."+newVersion+"]...");

        sqLiteDatabase.execSQL(SQLiteGadgetRepository.SQL_DROP_TABLE);
        sqLiteDatabase.execSQL(SQLiteActionRepository.SQL_DROP_TABLE);
        onCreate(sqLiteDatabase);
    }

}

