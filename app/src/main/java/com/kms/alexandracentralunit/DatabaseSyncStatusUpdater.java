package com.kms.alexandracentralunit;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


/**
 * DatabaseSyncStatusUpdater
 * This is broadcast receiver called every specified time (and before action is taken?)
 */
public class DatabaseSyncStatusUpdater extends BroadcastReceiver {

    public DatabaseSyncStatusUpdater() {
    }

    /**
     * @param context
     * @param intent
     */
    @Override
    public void onReceive(final Context context, Intent intent) {

        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams parameters = new RequestParams();

        client.post("adres", parameters, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject object) {
                try
                {
                    if(object.getInt("status") != 0)
                    {
                        final Intent intent = new Intent(context, RemoteToLocalDatabaseSyncService.class);
                        //place for other information
                        context.startService(intent);
                    }
                }
                catch (JSONException ex)
                {
                    Log.e("JSON exception", object.toString());
                }
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray array) {

            }
        });
    }
}
