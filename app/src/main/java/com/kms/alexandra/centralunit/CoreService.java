package com.kms.alexandra.centralunit;


import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.preference.PreferenceManager;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.kms.alexandra.data.FirebaseSyncService;
import com.kms.alexandra.data.database.HomeRepository;
import com.kms.alexandra.data.database.json.JSONHomeRepository;
import com.kms.alexandra.data.model.Home;

import java.util.Calendar;
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

    public static final String UPDATE_MESSAGE = "com.kms.alexandra.centralunit.CoreService.UPDATE_MESSAGE";
    public static final String GADGET = "gadgetName";
    public static final String HOME_ID = "home_id";
    public static final String HOME_NAME = "home_name";
    public static final String CONFIGURED = "configured";
    // Message types sent from the BluetoothChatService Handler
    public static final int MESSAGE_STATE_CHANGE = 1;
    public static final int MESSAGE_READ = 2;
    public static final int MESSAGE_WRITE = 3;
    public static final int MESSAGE_DEVICE_NAME = 4;
    public static final int MESSAGE_TOAST = 5;
    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch(msg.what)
            {
                case MESSAGE_STATE_CHANGE:
                    switch(msg.arg1)
                    {
                        case BluetoothChatService.STATE_CONNECTED:
                            break;
                        case BluetoothChatService.STATE_CONNECTING:
                            break;
                        case BluetoothChatService.STATE_LISTEN:
                        case BluetoothChatService.STATE_NONE:
                            break;
                    }
                    break;
                case MESSAGE_WRITE:
                    byte[] writeBuf = (byte[]) msg.obj;
                    // construct a string from the buffer
                    String writeMessage = new String(writeBuf);
                    break;
                case MESSAGE_READ:
                    byte[] readBuf = (byte[]) msg.obj;
                    // construct a string from the valid bytes in the buffer
                    String readMessage = new String(readBuf, 0, msg.arg1);

                    String[] input = readMessage.split("\\s+");
                    if(input[0].equals("AlexandraConfiguration"))
                    {
                        String ssid = input[1];
                        String password = input[2];
                        connectToWifi(ssid, password);
                    }
                    break;
                case MESSAGE_DEVICE_NAME:
                    // save the connected device's name
                    break;
                case MESSAGE_TOAST:
                    break;
            }
        }
    };
    public static final String DEVICE_NAME = "device_name";
    public static final String TOAST = "toast";
    private static final UUID CENTRAL_UNIT = UUID.fromString("f000aa20-0451-4000-b000-000000000000");
    private static final String TAG = "CoreService";
    private static Context context;
    private static Home home;
    private static String homeID;
    private static HomeRepository homeRepository;
    private BluetoothAdapter bluetoothAdapter;

    public CoreService() {
    }

    public static Context getContext() {
        return CoreService.context;
    }

    public static Home getHome() {
        return home;
    }

    public static String getHomeId() {
        return homeID;
    }

    public static HomeRepository getHomeRepository() {
        return homeRepository;
    }

    @Override
    public void onCreate() {
        LocalBroadcastManager broadcaster = LocalBroadcastManager.getInstance(this);
        context = getApplicationContext();
        Firebase.setAndroidContext(getApplication());

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        //        if(!(sharedPreferences.getBoolean(CONFIGURED, false)))
        if(true)
        {
            connectToBluetooth();
            boolean connectedToWifi = connectToWifi("Livebox-D69B", "2E62120CE85F61E6C402CE9E72");
            //            boolean connectedToWifi = connectToWifi("NOKIA Lumia 630_8259", "1N;s0033");
            //  connectedToWifi = connectToWifi("NexusWiFi", "vanitasvanitatum");
            if(connectedToWifi)
            {
                logEvent("firstRunSetup", "success");
            }
            else
            {
                logEvent("firstRunSetup", "cannot establish connection");
            }

        }
        else
        {
            homeID = sharedPreferences.getString(HOME_ID, "0");
            loadData();
            initializeConfiguration();

        }

        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Toast.makeText(this, "Core Service Started", Toast.LENGTH_LONG).show();
        //Intent intents = new Intent(getBaseContext(), AdminActivity.class);
        // intents.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        //startActivity(intents);

        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                // sendResult(home.getRoom("0").getName());
                Log.d("ilosc pokoi:", String.valueOf(home.getRooms().size()));
                Log.d("ilosc urzadzen:", String.valueOf(home.getGadgets().size()));
                Log.d("ilosc scen:", String.valueOf(home.getScenes().size()));
                Log.d("ilosc harmonogramow:", String.valueOf(home.getSchedule().size()));

                //start scheduleManager
                ScheduleManager scheduleManager = ScheduleManager.getInstance();
                //start remote control services
                Intent remoteControlIntent = new Intent(getBaseContext(), FirebaseControlMessageDispatcher.class);
                startService(remoteControlIntent);

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
    private void logEvent(String type, String message) {
        Intent intent = new Intent(getBaseContext(), HistorianBroadcastReceiver.class);
        intent.putExtra(HistorianBroadcastReceiver.LOG_TYPE, HistorianBroadcastReceiver.LogType.System);
        intent.putExtra(HistorianBroadcastReceiver.TYPE, type);
        intent.putExtra(HistorianBroadcastReceiver.MESSAGE, message);
        intent.putExtra(HistorianBroadcastReceiver.TIME, Calendar.getInstance().getTime().toString());
        sendBroadcast(intent);
    }

    private void loadData() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String homeId = sharedPreferences.getString(HOME_ID, "5");
        String homeName = sharedPreferences.getString(HOME_NAME, "domek");
        //start local repositories
        homeRepository = new JSONHomeRepository(getApplicationContext());
        Log.d("dane domu:", homeId+"   "+homeName);
        home = homeRepository.getHome(homeId, homeName);

        //start firebase sync service
        Intent service = new Intent(getBaseContext(), FirebaseSyncService.class);
        startService(service);
    }

    private void initializeConfiguration() {
        FirebaseCurrentStateObserver.getInstance();
        ScheduleManager.getInstance();

        //TODO: inicjalizacja komunikacji między urządzeniami
        //TODO: wszystkie inne ustawienia

    }

    /**
     * connecting to wifi with provided credentials
     *
     * @param ssid     - network identificator
     * @param password - network password
     * @return flag indicating wifi connection state
     */
    private boolean connectToWifi(String ssid, String password) {

        final String TAG_WIFI_CONNECTING = "connectingToWiFi";
        WifiManager wifi = (WifiManager) this.getSystemService(Context.WIFI_SERVICE);

        if(!wifi.isWifiEnabled())
        {
            if(!wifi.setWifiEnabled(true))
            {
                Log.e("WiFi", "without WiFi won't work");
                return false;
            }
        }

        WifiConfiguration conf = new WifiConfiguration();
        conf.SSID = "\""+ssid+"\"";
        conf.preSharedKey = "\""+password+"\"";
        conf.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
        conf.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
        conf.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK);
        conf.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP);
        conf.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.CCMP);
        conf.allowedProtocols.set(WifiConfiguration.Protocol.RSN);
        conf.status = WifiConfiguration.Status.ENABLED;
        int networkId = wifi.addNetwork(conf);
        wifi.disconnect();
        wifi.enableNetwork(networkId, true);
        boolean completed = wifi.reconnect();

        if(!completed)
        {
            Log.d(TAG_WIFI_CONNECTING, "repeat wifi connection attempt");
            try
            {
                Thread.sleep(5000);
            }
            catch (InterruptedException e)
            {
                e.printStackTrace();
            }
            wifi.disconnect();
            wifi.enableNetwork(networkId, true);
            completed = wifi.reconnect();
        }

        /**
         * delay introduced in order to wait for establishing connection
         * (flag from reconnect() is set before network is really working)
         */
        try
        {
            Thread.sleep(5000);
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }

        setupHome();
        return completed;
    }

    private void setupHome() {
        Map<String, Object> newHome = new HashMap<String, Object>();
        newHome.put("centralUnit", CENTRAL_UNIT);
        //            Firebase rootReference = new Firebase("https://sizzling-torch-8921.firebaseio.com/configuration/");
        //            Firebase homeIdRef = rootReference.push();
        //            homeIdRef.setValue(newHome);
        //CoreService.homeID=homeIdRef.getKey();
        CoreService.homeID = "-JcMyexVThw7PEv2Z2PL";

        /**
         * saving data into SharedPreferences
         */
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(HOME_ID, "-JcMyexVThw7PEv2Z2PL");
        //    editor.putString(HOME_ID, homeID.getKey());
        editor.putString(HOME_NAME, "Dom Krola Artura");
        editor.putBoolean(CONFIGURED, true);
        editor.apply();
        Log.d("sharedPref", "zapisano");

        homeID = sharedPref.getString(HOME_ID, "0");
        loadData();
        initializeConfiguration();
    }

    public void connectToBluetooth() {
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        //chat service
        BluetoothChatService bluetoothChatService = new BluetoothChatService(this, mHandler);
        bluetoothChatService.start();
    }

}
