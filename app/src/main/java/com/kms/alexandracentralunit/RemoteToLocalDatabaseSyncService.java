package com.kms.alexandracentralunit;


import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;

import java.io.UnsupportedEncodingException;


public class RemoteToLocalDatabaseSyncService extends Service {

    private ConfigurationDatabaseHelper databaseHelper;

    public RemoteToLocalDatabaseSyncService() {
        databaseHelper = ConfigurationDatabaseHelper.getInstance(this.getBaseContext());
    }

    @Override
    public void onCreate() {

    }

    @Override
    public void onStart(Intent intent, int startId) {
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams parameters = new RequestParams();

        client.post("adres", parameters, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

                try
                {
                    updateConfigurationDatabase(new String(responseBody, "UTF-8"));
                }
                catch (UnsupportedEncodingException e)
                {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }
        });

    }

    @Override
    public void onDestroy() {

    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    private void updateConfigurationDatabase(String response) {
        //TODO: parse into ContentValues and put into database

    }
}
