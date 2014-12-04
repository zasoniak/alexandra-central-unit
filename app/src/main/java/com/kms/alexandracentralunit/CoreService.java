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

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;


/**
 * Created by Mateusz Zasoński on 2014-10-29.
 * CoreService - starting place for other parts of the system
 */
public class CoreService extends Service {

    public static final UUID CENTRAL_UNIT = UUID.fromString("f000aa20-0451-4000-b000-000000000000");
    public static final String UPDATE_MESSAGE = "com.kms.alexandracentralunit.CoreService.UPDATE_MESSAGE";
    public static final String GADGET = "gadgetName";

    public static final String HOME_ID = "home_id";
    public static final String HOME_NAME = "home_name";
    public static final String CONFIGURED = "configured";

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
        broadcaster = LocalBroadcastManager.getInstance(this);
        context = getApplicationContext();
        Firebase.setAndroidContext(getApplication());

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        if(!(sharedPreferences.getBoolean(CONFIGURED, false)))
        {
            firstRunSetup();
        }
        loadData();
        initializeConfiguration();

        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Toast.makeText(this, "Core Service Started", Toast.LENGTH_LONG).show();
        //Intent intents = new Intent(getBaseContext(), AdminActivity.class);
        // intents.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        //startActivity(intents);

        Log.d(TAG, "onStart");

        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                // sendResult(home.getRoom("0").getName());
                Log.d("ilosc pokoi:", String.valueOf(home.getRooms().size()));
                Log.d("ilosc urzadzen:", String.valueOf(home.getGadgets().size()));
                Log.d("ilosc scen:", String.valueOf(home.getScenes().size()));
                Log.d("ilosc harmonogramow:", String.valueOf(home.getSchedule().size()));

                //start scheduleManagerService
                Intent scheduleIntent = new Intent(getBaseContext(), ScheduleManagerService.class);
                startService(scheduleIntent);
                //start remote control services
                Intent remoteControlIntent = new Intent(getBaseContext(), FirebaseControlMessageDispatcher.class);
                startService(remoteControlIntent);
                CurrentStateObserver currentStateObserver = new FirebaseCurrentStateObserver();

            }
        };
        Timer timer = new Timer();
        timer.schedule(task, 10000);

        return super.onStartCommand(intent, flags, startId);
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
            this.sendBroadcast(intent);
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
        //start local repositories
        homeRepository = new JSONHomeRepository();
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String homeId = sharedPreferences.getString(HOME_ID, "5");
        String homeName = sharedPreferences.getString(HOME_NAME, "domek");

        Log.d("dane domu:", homeId+"   "+homeName);
        home = homeRepository.getHome(homeId, homeName);

        //start firebase sync service
        Intent service = new Intent(getBaseContext(), FirebaseSyncService.class);
        startService(service);
    }

    private void initializeConfiguration() {
        //TODO: inicjalizacja komunikacji między urządzeniami
        //TODO: wszystkie inne ustawienia

    }

    private void firstRunSetup() {

        //TODO: wprowadzić oczekiwanie na dane od uzytkownika
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor = sharedPref.edit();

        //TODO: connect to WIFI

        Map<String, Object> newHome = new HashMap<String, Object>();
        newHome.put("centralUnit", CENTRAL_UNIT);
        Firebase rootReference = new Firebase("https://sizzling-torch-8921.firebaseio.com/configuration/");
        Firebase homeID = rootReference.push();

        homeID.setValue(newHome);
        Log.d("homeID", homeID.getKey());
        editor.putString(HOME_ID, homeID.getKey());
        editor.putString(HOME_NAME, "Dom Krola Artura");
        editor.putBoolean(CONFIGURED, true);
        editor.apply();
        Log.d("sharedPref", "zapisano");

    }

}
