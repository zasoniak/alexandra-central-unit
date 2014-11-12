package com.kms.alexandracentralunit;


import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.kms.alexandracentralunit.data.FirebaseSyncService;
import com.kms.alexandracentralunit.data.database.HomeRepository;
import com.kms.alexandracentralunit.data.database.json.JSONHomeRepository;
import com.kms.alexandracentralunit.data.model.Home;

import java.util.Timer;
import java.util.TimerTask;


public class CoreService extends Service {

    public static final String UPDATE_MESSAGE = "com.kms.alexandracentralunit.CoreService.UPDATE_MESSAGE";
    public static final String GADGET = "gadgetName";

    public static final String HOME_ID = "home_id";
    public static final String HOME_NAME = "home_name";


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

    public static String getHomeId() {
        return home.getId();
    }

    public static HomeRepository getHomeRepository() {
        return homeRepository;
    }

    @Override
    public void onCreate() {
        firstRunSetup();
        broadcaster = LocalBroadcastManager.getInstance(this);
        context = getApplicationContext();
        homeRepository = new JSONHomeRepository();
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String homeId = sharedPreferences.getString(HOME_ID, "5");
        String homeName = sharedPreferences.getString(HOME_NAME, "domek");
        home = homeRepository.getHome(homeId, homeName);
        Firebase.setAndroidContext(getApplication());
        super.onCreate();
    }

    @Override
    public void onStart(Intent intent, int startid) {

        //Intent intents = new Intent(getBaseContext(), AdminActivity.class);
        // intents.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        //startActivity(intents);

        Toast.makeText(this, "Core Service Started", Toast.LENGTH_LONG).show();

        //start firebase sync service
        Intent service = new Intent(getBaseContext(), FirebaseSyncService.class);
        startService(service);

        Log.d(TAG, "onStart");

        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                // sendResult(home.getRoom("0").getName());
                Log.d("ilosc pokoi:", String.valueOf(home.getRooms().size()));
                Log.d("ilosc urzadzen:", String.valueOf(home.getGadgets().size()));
                Log.d("ilosc scen:", String.valueOf(home.getScenes().size()));
                Log.d("ilosc harmonogramow:", String.valueOf(home.getSchedule().size()));
            }
        };
        Timer timer = new Timer();
        timer.schedule(task, 5000);
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
        //TODO:
        sendBroadcast(intent);
    }

    private void loadData() {

    }

    private void initializeConfiguration() {

    }

    private void firstRunSetup() {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(HOME_ID, "0");
        editor.putString(HOME_NAME, "Dom Krola Artura");
        editor.apply();
    }

}
