package com.kms.alexandracentralunit;


import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.Toast;

import com.kms.alexandracentralunit.data.database.HomeRepository;
import com.kms.alexandracentralunit.data.database.json.JSONHomeRepository;
import com.kms.alexandracentralunit.data.model.Gadget;
import com.kms.alexandracentralunit.data.model.Home;


public class CoreService extends Service {

    public static final String UPDATE_MESSAGE = "com.kms.alexandracentralunit.CoreService.UPDATE_MESSAGE";
    public static final String GADGET = "gadgetName";
    private static final String TAG = "CoreService";

    private static Context context;
    private static Home home;
    private static HomeRepository homeRepository;
    public LocalBroadcastManager broadcaster;

    public CoreService() {
    }

    public static Context getContext() {
        return CoreService.context;
    }

    public static Home getHome() {
        return home;
    }

    public static HomeRepository getHomeRepository() {
        return homeRepository;
    }

    @Override
    public void onCreate() {
        broadcaster = LocalBroadcastManager.getInstance(this);
        context = getApplicationContext();
        homeRepository = new JSONHomeRepository();
        home = homeRepository.getHome();

        for(Gadget gadget : home.getGadgets())
        {
            gadget.setup();
        }

        super.onCreate();
    }

    @Override
    public void onStart(Intent intent, int startid) {
        //Intent intents = new Intent(getBaseContext(), AdminActivity.class);
        // intents.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        //startActivity(intents);
        Toast.makeText(this, "Core Service Started", Toast.LENGTH_LONG).show();
        Log.d(TAG, "onStart");

    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    public void sendResult(String message) {
        Intent intent = new Intent(UPDATE_MESSAGE);
        if(message != null)
        {
            intent.putExtra(GADGET, message);
            broadcaster.sendBroadcast(intent);
        }
    }

    /**
     * log data
     */
    private void logData() {
        Intent intent = new Intent(getBaseContext(), HistorianBroadcastReceiver.class);
        intent.putExtra("table", "measurements");
        intent.putExtra("type", "temperature");
        intent.putExtra("value", "XXX");
        sendBroadcast(intent);
    }

}
