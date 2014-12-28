package com.kms.alexandracentralunit;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import org.json.JSONObject;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;


/**
 * @author Mateusz Zasoński
 */
public class JSONHistorianRepository extends SQLiteOpenHelper implements HistorianRepository {

    public static final String KEY_HISTORIAN_ID = "_id";
    public static final String KEY_HISTORIAN_TYPE = "type";
    public static final String KEY_HISTORIAN_LOG = "log";
    public static final String KEY_HISTORIAN_TIMESTAMP = "timestamp";
    private static final String[] TABLE_COLUMNS = {KEY_HISTORIAN_ID, KEY_HISTORIAN_TYPE,
                                                   KEY_HISTORIAN_LOG, KEY_HISTORIAN_TIMESTAMP};
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "JSONHistorianDatabase";
    private static final String TABLE_NAME = "historian";
    public static final String SQL_DROP_TABLE = "DROP TABLE IF EXISTS "+TABLE_NAME;
    private static final String COMMA_SEP = ", ";
    private static final String KEY_HISTORIAN_ID_TYPE = "INTEGER PRIMARY KEY";
    private static final String KEY_HISTORIAN_TYPE_TYPE = "TEXT";
    private static final String KEY_HISTORIAN_LOG_TYPE = "TEXT";
    private static final String KEY_HISTORIAN_TIMESTAMP_TYPE = "DATETIME DEFAULT CURRENT_TIMESTAMP";
    public static final String SQL_CREATE_TABLE = "CREATE TABLE "+TABLE_NAME+" ("+
            KEY_HISTORIAN_ID+" "+KEY_HISTORIAN_ID_TYPE+COMMA_SEP+
            KEY_HISTORIAN_TYPE+" "+KEY_HISTORIAN_TYPE_TYPE+COMMA_SEP+
            KEY_HISTORIAN_LOG+" "+KEY_HISTORIAN_LOG_TYPE+COMMA_SEP+
            KEY_HISTORIAN_TIMESTAMP+" "+KEY_HISTORIAN_TIMESTAMP_TYPE+")";
    private static JSONHistorianRepository instance;
    private AtomicInteger threadCounter = new AtomicInteger();
    private SQLiteDatabase database;

    private JSONHistorianRepository(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public static synchronized JSONHistorianRepository getInstance(Context context) {
        if(instance == null)
        {
            instance = new JSONHistorianRepository(context.getApplicationContext());
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
        sqLiteDatabase.execSQL(SQL_CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i2) {
        sqLiteDatabase.execSQL(SQL_DROP_TABLE);
        onCreate(sqLiteDatabase);
    }

    @Override
    public boolean log(HistorianBroadcastReceiver.LogType type, Map objectMap) {
        JSONObject object = new JSONObject(objectMap);
        SQLiteDatabase sqLiteDatabase = this.openDatabase();
        Log.d("historian log:", object.toString());
        String query = "INSERT INTO "+TABLE_NAME+" ("+
                KEY_HISTORIAN_TYPE+COMMA_SEP+
                KEY_HISTORIAN_LOG+") "+"values"+" ("+
                "\'"+type.toString()+"\'"+COMMA_SEP+
                "\'"+object.toString()+"\'"+");";
        sqLiteDatabase.execSQL(query);
        this.closeDatabase();
        return true;
    }

    @Override
    public List<JSONObject> getAll() {
        return null;
    }

    @Override
    public List<JSONObject> getAllByDate(Date from, Date to) {
        return null;
    }

    @Override
    public List<JSONObject> getAllByDateAndType(Date from, Date to, HistorianBroadcastReceiver.LogType type) {
        return null;
    }
}
