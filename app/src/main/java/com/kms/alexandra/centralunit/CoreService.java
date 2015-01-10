package com.kms.alexandra.centralunit;


import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothProfile;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.util.SparseArray;
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
public class CoreService extends Service implements BluetoothAdapter.LeScanCallback {

    public static final String UPDATE_MESSAGE = "com.kms.alexandra.centralunit.CoreService.UPDATE_MESSAGE";
    public static final String GADGET = "gadgetName";
    public static final String HOME_ID = "home_id";
    public static final String HOME_NAME = "home_name";
    public static final String CONFIGURED = "configured";
    private static final UUID CENTRAL_UNIT = UUID.fromString("f000aa20-0451-4000-b000-000000000000");
    private static final String TAG = "CoreService";
    private BluetoothGattCallback gattCallback = new BluetoothGattCallback() {
        private final UUID WIFI_ACCESS_SERVICE = UUID.fromString("f000aa20-0451-4000-b000-000000000000");
        private final UUID SSID_CHARACTERISTIC = UUID.fromString("f000aa20-0451-4000-b000-000000000000");
        private final UUID PASSWORD_CHARACTERISTIC = UUID.fromString("f000aa20-0451-4000-b000-000000000000");

        private String ssid;
        private String password;

        @Override
        public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
            Log.d(TAG, "Connection State Change: "+status+" -> "+connectionState(newState));
            if(status == BluetoothGatt.GATT_SUCCESS && newState == BluetoothProfile.STATE_CONNECTED)
            {
                /*
                 * Once successfully connected, we must next discover all the services on the
                 * device before we can read and write their characteristics.
                 */
                gatt.discoverServices();
            }
            else
            {
                if(status == BluetoothGatt.GATT_SUCCESS && newState == BluetoothProfile.STATE_DISCONNECTED)
                {
                /*
                 * If at any point we disconnect, send a message to clear the weather values
                 * out of the UI
                 */
                }
                else
                {
                    if(status != BluetoothGatt.GATT_SUCCESS)
                    {
                /*
                 * If there is a failure at any stage, simply disconnect
                 */
                        gatt.disconnect();
                    }
                }
            }
        }

        @Override
        public void onServicesDiscovered(BluetoothGatt gatt, int status) {
            Log.d(TAG, "Services Discovered: "+status);
            /*
             * With services discovered, we are going to reset our state machine and start
             * working through the sensors we need to enable
             */

            if(gatt.getService(WIFI_ACCESS_SERVICE) != null)
            {
                Log.d(TAG, "SSID read");
                readSSID(gatt);
            }

        }

        @Override
        public void onCharacteristicRead(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
            //For each read, pass the data up to the UI thread to update the display
            if(SSID_CHARACTERISTIC.equals(characteristic.getUuid()))
            {
                if(characteristic.getValue() != null)
                {
                    ssid = characteristic.getStringValue(0);
                    Log.d(TAG, "Password read");
                    readPassword(gatt);
                }
            }
            if(PASSWORD_CHARACTERISTIC.equals(characteristic.getUuid()))
            {
                if(characteristic.getValue() != null)
                {
                    password = characteristic.getStringValue(0);
                    connectToWifi(ssid, password);
                }
            }
        }

        @Override
        public void onReadRemoteRssi(BluetoothGatt gatt, int rssi, int status) {
            Log.d(TAG, "Remote RSSI: "+rssi);
        }

        private void readSSID(BluetoothGatt gatt) {
            BluetoothGattCharacteristic characteristic;
            characteristic = gatt.getService(WIFI_ACCESS_SERVICE).getCharacteristic(SSID_CHARACTERISTIC);
            gatt.readCharacteristic(characteristic);

        }

        private void readPassword(BluetoothGatt gatt) {
            BluetoothGattCharacteristic characteristic;
            characteristic = gatt.getService(WIFI_ACCESS_SERVICE).getCharacteristic(PASSWORD_CHARACTERISTIC);
            gatt.readCharacteristic(characteristic);
        }

        private String connectionState(int status) {
            switch(status)
            {
                case BluetoothProfile.STATE_CONNECTED:
                    return "Connected";
                case BluetoothProfile.STATE_DISCONNECTED:
                    return "Disconnected";
                case BluetoothProfile.STATE_CONNECTING:
                    return "Connecting";
                case BluetoothProfile.STATE_DISCONNECTING:
                    return "Disconnecting";
                default:
                    return String.valueOf(status);
            }
        }
    };
    private static Context context;
    private static Home home;
    private static String homeID;
    private static HomeRepository homeRepository;
    private LocalBroadcastManager broadcaster;
    private boolean connectedToWifi = false;
    private BluetoothAdapter bluetoothAdapter;
    private SparseArray<BluetoothDevice> devices;
    private BluetoothGatt connectedGatt;
    private Handler handler;
    private Runnable mStopRunnable = new Runnable() {
        @Override
        public void run() {
            stopScan();
        }
    };
    private Runnable mStartRunnable = new Runnable() {
        @Override
        public void run() {
            startScan();
        }
    };

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
        broadcaster = LocalBroadcastManager.getInstance(this);
        context = getApplicationContext();
        Firebase.setAndroidContext(getApplication());

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        //if(!(sharedPreferences.getBoolean(CONFIGURED, false)))
        if(true)
        {
            // firstRunSetup();
            connectedToWifi = connectToWifi("Livebox-D69B", "2E62120CE85F61E6C402CE9E72");
            // connectedToWifi = connectToWifi("NexusWiFi", "vanitasvanitatum");
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

        Log.d(TAG, "onStart");

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
        //start local repositories
        homeRepository = new JSONHomeRepository(getApplicationContext());
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
        // CurrentStateObserver currentStateObserver = FirebaseCurrentStateObserver.getInstance();
        //TODO: inicjalizacja komunikacji między urządzeniami
        //TODO: wszystkie inne ustawienia

    }

    /**
     * first run configuration
     * <p/>
     * includes:
     * - passing WiFi credentials and establishing connection
     * - creating Home directory on server
     * - saving data essential for proper work
     */
    private void firstRunSetup() {
        /**
         * receiving data from main user
         */
        connectToBLE();

        /**
         * connecting to WiFi network
         */
        // connectedToWifi = connectToWifi("Livebox-D69B", "2E62120CE85F61E6C402CE9E72");
        //  connectedToWifi = connectToWifi("NexusWiFi", "vanitasvanitatum");

    }

    private void setupHome() {
        Map<String, Object> newHome = new HashMap<String, Object>();
        newHome.put("centralUnit", CENTRAL_UNIT);
        //            Firebase rootReference = new Firebase("https://sizzling-torch-8921.firebaseio.com/configuration/");
        //            Firebase homeIdRef = rootReference.push();
        //            homeIdRef.setValue(newHome);
        //CoreService.homeID=homeIdRef.getKey();
        CoreService.homeID = "-JcMyexVThw7PEv2Z2PL";
        //            Log.d("homeID", homeIdRef.getKey());

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

    private void connectToBLE() {
        BluetoothManager manager = (BluetoothManager) getSystemService(BLUETOOTH_SERVICE);
        bluetoothAdapter = manager.getAdapter();
        devices = new SparseArray<BluetoothDevice>();
        startScan();
    }

    private void startScan() {
        bluetoothAdapter.startLeScan(this);
        handler.postDelayed(mStopRunnable, 2500);
    }

    private void stopScan() {
        bluetoothAdapter.stopLeScan(this);
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

    @Override
    public void onLeScan(BluetoothDevice bluetoothDevice, int i, byte[] bytes) {
        Log.d(TAG, "discovered device: "+bluetoothDevice.getName());
        devices.put(bluetoothDevice.hashCode(), bluetoothDevice);
        bluetoothDevice.connectGatt(this, false, gattCallback);
    }
}
