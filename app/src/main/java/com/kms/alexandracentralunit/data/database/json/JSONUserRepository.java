package com.kms.alexandracentralunit.data.database.json;


import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.kms.alexandracentralunit.data.database.UserRepository;
import com.kms.alexandracentralunit.data.model.User;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


/**
 * JSON implementation of schedule repository
 * <p/>
 * provides add / delete / update / find / getAll methods
 *
 * @author Mateusz Zasoński
 * @version 0.1
 */
public class JSONUserRepository implements UserRepository {

    public static final String KEY_USER_ID = "_id";
    public static final String KEY_USER_OBJECT = "object";
    private static final String[] TABLE_COLUMNS = {KEY_USER_ID, KEY_USER_OBJECT};
    private static final String TABLE_NAME = "users";
    public static final String SQL_DROP_TABLE = "DROP TABLE IF EXISTS "+TABLE_NAME;
    private static final String COMMA_SEP = ", ";
    private static final String KEY_USER_ID_TYPE = "TEXT";
    private static final String KEY_USER_OBJECT_TYPE = "TEXT";
    public static final String SQL_CREATE_TABLE = "CREATE TABLE "+TABLE_NAME+" ("+
            KEY_USER_ID+" "+KEY_USER_ID_TYPE+COMMA_SEP+
            KEY_USER_OBJECT+" "+KEY_USER_OBJECT_TYPE+")";

    private JSONConfigurationDatabaseHelper databaseHelper;

    public JSONUserRepository(Context context) {
        databaseHelper = JSONConfigurationDatabaseHelper.getInstance(context);
    }

    @Override
    public boolean add(User user) {
        JSONObject object = toJSONObject(user);
        SQLiteDatabase sqLiteDatabase = databaseHelper.openDatabase();

        String query = "INSERT INTO "+TABLE_NAME+" ("+
                KEY_USER_ID+COMMA_SEP+
                KEY_USER_OBJECT+") "+"values"+" ("+
                "\'"+user.getId()+"\'"+COMMA_SEP+
                "\'"+object.toString()+"\'"+");";
        sqLiteDatabase.execSQL(query);

        databaseHelper.closeDatabase();
        return true;
    }

    @Override
    public boolean delete(User user) {
        SQLiteDatabase sqLiteDatabase = databaseHelper.openDatabase();

        String query = "DELETE FROM "+TABLE_NAME+
                " WHERE "+KEY_USER_ID+" = "+"\'"+user.getId()+"\'"+");";

        sqLiteDatabase.execSQL(query);
        databaseHelper.closeDatabase();
        return true;
    }

    @Override
    public boolean update(User user) {
        JSONObject object = toJSONObject(user);
        SQLiteDatabase sqLiteDatabase = databaseHelper.openDatabase();

        String query = "UPDATE "+TABLE_NAME+" SET "+
                KEY_USER_OBJECT+" = "+object.toString()+
                " WHERE "+KEY_USER_OBJECT+" = "+"\'"+user.getId()+"\'"+");";

        sqLiteDatabase.execSQL(query);
        databaseHelper.closeDatabase();
        return true;
    }

    @Override
    public User find(String id) {
        // obtain thread-safe database access
        SQLiteDatabase sqLiteDatabase = databaseHelper.openDatabase();

        // build a query
        Cursor cursor = sqLiteDatabase.query(TABLE_NAME, // a. table
                TABLE_COLUMNS, // b. column names
                KEY_USER_ID+" = ?", // c. selections
                new String[] {}, // d. selections args
                null, // e. group by
                null, // f. having
                null, // g. order by
                null); // h. limit

        // prepare structured data
        User user;
        if(cursor != null)
        {
            cursor.moveToFirst();
            try
            {
                user = fromJSONObject(new JSONObject(cursor.getString(1)));
                databaseHelper.closeDatabase();
                cursor.close();
                return user;
            }
            catch (JSONException e)
            {
                e.printStackTrace();
                return null;
            }
        }
        return null;
    }

    @Override
    public List<User> getAll() {
        // obtain thread-safe database access
        SQLiteDatabase sqLiteDatabase = databaseHelper.openDatabase();

        // build a query
        String query = "SELECT * FROM "+TABLE_NAME+" ORDER BY "+KEY_USER_ID;
        Cursor cursor = sqLiteDatabase.rawQuery(query, null);

        // prepare structured data
        ArrayList<User> users = new ArrayList<User>();
        if(cursor.moveToFirst())
        {
            do
            {
                try
                {
                    users.add(fromJSONObject(new JSONObject(cursor.getString(1))));
                }
                catch (JSONException e)
                {
                    e.printStackTrace();
                    return users;
                }
            } while(cursor.moveToNext());
        }
        // close database connection and release resources
        databaseHelper.closeDatabase();
        cursor.close();
        // return rooms
        return users;
    }

    private User fromJSONObject(JSONObject object) {
        return new User();
    }

    private JSONObject toJSONObject(User user) {
        return new JSONObject();
    }
}
