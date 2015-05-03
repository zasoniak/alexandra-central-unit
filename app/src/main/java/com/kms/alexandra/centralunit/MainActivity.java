package com.kms.alexandra.centralunit;


import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanFilter;
import android.bluetooth.le.ScanResult;
import android.bluetooth.le.ScanSettings;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.kms.alexandra.R;
import com.kms.alexandra.data.FirebaseSyncService;
import com.kms.alexandra.data.HomeManager;
import com.kms.alexandra.data.LocalHomeManagerDecorator;
import com.kms.alexandra.data.database.HomeRepository;
import com.kms.alexandra.data.database.json.JSONHomeRepository;
import com.kms.alexandra.data.model.Home;
import com.kms.alexandra.data.model.Room;
import com.kms.alexandra.data.model.Scene;
import com.kms.alexandra.data.model.ScheduledScene;
import com.kms.alexandra.data.model.gadgets.Gadget;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.UUID;


/**
 * @author Mateusz Zasoński
 */
public class MainActivity extends Activity {

    public static final UUID ID = UUID.fromString("f1e1ddd0-a547-11e4-bcd8-0800200c9a66");
    public static final String TAG = "Alexandra";
    private final Handler mHandler = new Handler() {
        // Message types sent from the BluetoothChatService Handler
        public static final int MESSAGE_STATE_CHANGE = 1;
        public static final int MESSAGE_READ = 2;
        public static final int MESSAGE_WRITE = 3;
        public static final int MESSAGE_DEVICE_NAME = 4;
        public static final int MESSAGE_TOAST = 5;

        @Override
        public void handleMessage(Message msg) {
            switch(msg.what)
            {
                case MESSAGE_STATE_CHANGE:
                    Log.i(TAG, "MESSAGE_STATE_CHANGE: "+msg.arg1);
                    switch(msg.arg1)
                    {
                        case BluetoothChatService.STATE_CONNECTED:
                            Log.i(TAG, "STATE_CONNECTED");
                            break;
                        case BluetoothChatService.STATE_CONNECTING:
                            Log.i(TAG, "STATE_CONNECTING");
                            break;
                        case BluetoothChatService.STATE_LISTEN:
                            Log.i(TAG, "STATE_LISTEN");
                        case BluetoothChatService.STATE_NONE:
                            Log.i(TAG, "STATE_NONE");
                            break;
                    }
                    break;
                case MESSAGE_WRITE:
                    //byte[] writeBuf = (byte[]) msg.obj;
                    // construct a string from the buffer
                    // String writeMessage = new String(writeBuf);
                    break;
                case MESSAGE_READ:
                    byte[] readBuf = (byte[]) msg.obj;
                    // construct a string from the valid bytes in the buffer
                    String readMessage = new String(readBuf, 0, msg.arg1);
                    StringBuilder builder = new StringBuilder(readMessage);

                    int begining = readMessage.indexOf("<");
                    int end = readMessage.indexOf(">");
                    String ssid = readMessage.substring(begining+1, end);
                    Log.d("SSID", ssid);
                    builder.setCharAt(begining, '!');
                    builder.setCharAt(end, '!');
                    readMessage = builder.toString();

                    begining = readMessage.indexOf("<");
                    end = readMessage.indexOf(">");
                    String password = readMessage.substring(begining+1, end);
                    Log.d("SSID", password);
                    builder.setCharAt(begining, '!');
                    builder.setCharAt(end, '!');
                    readMessage = builder.toString();

                    begining = readMessage.indexOf("<");
                    end = readMessage.indexOf(">");
                    String homeId = readMessage.substring(begining+1, end);
                    Log.d("SSID", homeId);
                    builder.setCharAt(begining, '!');
                    builder.setCharAt(end, '!');
                    readMessage = builder.toString();

                    saveConfiguration(homeId);
                    connectToWifi(ssid, password);
                    //TODO: potwierdzenie wysyłać?
                    Log.d(TAG, readMessage);
                    break;
                case MESSAGE_DEVICE_NAME:
                    // save the connected device's name
                    break;
                case MESSAGE_TOAST:
                    break;
            }
        }
    };

    private ScanCallback scanCallback = new ScanCallback() {
        @Override
        public void onScanResult(int callbackType, ScanResult result) {

            BluetoothDevice bluetoothDevice = result.getDevice();
            Log.d(TAG, "discovered device: "+bluetoothDevice.getName());
            Log.d(TAG, "device MAC: "+bluetoothDevice.getAddress());
            Home home = ((Alexandra) getApplicationContext()).getHome();
            if(home != null)
            {
                for(Gadget gadget : home.getGadgets())
                {
                    Log.d(TAG, "gadget MAC: "+gadget.getMAC());

                    if(gadget.getMAC().equals(bluetoothDevice.getAddress()))
                    {
                        Log.d(TAG, "trying to connect GATT");
                        gadget.setBluetoothGatt(bluetoothDevice.connectGatt(getApplicationContext(), true, gadget.getBluetoothGattCallback()));
                    }
                }
            }

        }
    };
    private BluetoothAdapter.LeScanCallback leScanCallback = new BluetoothAdapter.LeScanCallback() {
        @Override
        public void onLeScan(BluetoothDevice bluetoothDevice, int i, byte[] bytes) {
            Log.d(TAG, "discovered device: "+bluetoothDevice.getName());
            Log.d(TAG, "device MAC: "+bluetoothDevice.getAddress());
            Home home = ((Alexandra) getApplicationContext()).getHome();
            if(home != null)
            {
                for(Gadget gadget : home.getGadgets())
                {
                    Log.d(TAG, "gadget MAC: "+gadget.getMAC());

                    if(gadget.getMAC().equals(bluetoothDevice.getAddress()))
                    {
                        Log.d(TAG, "trying to connect GATT");
                        gadget.setBluetoothGatt(bluetoothDevice.connectGatt(getApplicationContext(), true, gadget.getBluetoothGattCallback()));
                    }
                }
            }
        }
    };
    public static final String CONFIGURATION_TAG = "configuration";
    public static final String CONFIGURED = "configured";
    public static final String HOME_ID = "home_id";
    public static final String HOME_NAME = "name";
    BluetoothAdapter bluetoothAdapter;
    private ArrayList<String> partsArrayList;
    private ArrayAdapter<String> adapter;

    private void startScan() {
        Log.i("BLE", "start scan");
        BluetoothLeScanner scanner = bluetoothAdapter.getBluetoothLeScanner();
        ScanSettings settings = new ScanSettings.Builder().setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY).build();
        List<ScanFilter> filters = new ArrayList<ScanFilter>();
        scanner.startScan(filters, settings, scanCallback);
    }

    private void stopScan() {
        Log.i("BLE", "stop scan");
        BluetoothLeScanner scanner = bluetoothAdapter.getBluetoothLeScanner();
        scanner.stopScan(scanCallback);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Firebase.setAndroidContext(getApplicationContext());
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        if((sharedPreferences.getBoolean(CONFIGURED, false)))
        {
            //firstRunSetup();
            (new Setup(getApplicationContext())).start();
        }
        //                else
        //                {
        //                    (new Setup(getApplicationContext())).start();
        //                }
        // saveConfiguration("-JcMyexVThw7PEv2Z2PL");
        //  (new Setup(getApplicationContext())).start();
        //        boolean connectedToWifi = connectToWifi("Livebox-D69B", "2E62120CE85F61E6C402CE9E72");
        setContentView(R.layout.activity_admin);
        ListView listView = (ListView) findViewById(R.id.home_parts_list);
        partsArrayList = new ArrayList<String>();
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, partsArrayList);
        listView.setAdapter(adapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.admin, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch(item.getItemId())
        {
            case R.id.action_settings:
                firstRunSetup();
                return true;
            case R.id.action_refresh:
                refresh();
                return true;
            case R.id.action_default:
                if(!getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE))
                {
                    Toast.makeText(this, "No LE Support.", Toast.LENGTH_SHORT).show();
                }
                loadDefault();
                return true;
            case R.id.action_BLE:
                connectToBLE();
                return true;
            default:
                return false;
        }
    }

    private void refresh() {
        partsArrayList.clear();
        if(((Alexandra) getApplicationContext()).getHome() != null)
        {
            partsArrayList.clear();
            partsArrayList.add(((Alexandra) getApplicationContext()).getHome().getName());
            partsArrayList.add("GADGETS:");
            for(Gadget gadget : ((Alexandra) getApplicationContext()).getHome().getGadgets())
            {
                Log.i(TAG, gadget.getName());

                partsArrayList.add(gadget.getName()+" stan: "+gadget.getState().toString());
            }
            partsArrayList.add("ROOMS:");
            for(Room room : ((Alexandra) getApplicationContext()).getHome().getRooms())
            {
                partsArrayList.add(room.getName());
            }
            partsArrayList.add("SCENES:");
            for(Scene scene : ((Alexandra) getApplicationContext()).getHome().getScenes())
            {
                partsArrayList.add(scene.getName());
            }
            partsArrayList.add("SCHEDULES:");
            for(ScheduledScene schedule : ((Alexandra) getApplicationContext()).getHome().getSchedule())
            {
                partsArrayList.add(schedule.getScene()+" -> "+schedule.getHour()+":"+schedule.getMinutes());
            }
        }
        adapter.notifyDataSetChanged();
    }

    private void loadDefault() {
        //saveConfiguration("-JcMyexVThw7PEv2Z2PL");
        // saveConfiguration("-JcMyexVThw7PEv2Z2qq");
        saveConfiguration("-Jg_uD_kB2NLYkOA6nIo");
        connectToWifi("Livebox-D69B", "2E62120CE85F61E6C402CE9E72");
    }

    private void saveConfiguration(String homeId) {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(HOME_ID, homeId);
        editor.putBoolean(CONFIGURED, true);
        editor.apply();
        logEvent(CONFIGURATION_TAG, "configuration saved");
    }

    private void firstRunSetup() {
        BluetoothManager manager = (BluetoothManager) getSystemService(BLUETOOTH_SERVICE);
        bluetoothAdapter = manager.getAdapter();
        bluetoothAdapter.setName("Alexandra-f1e1ddd0");
        if(bluetoothAdapter.getScanMode() != BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE)
        {
            Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
            discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 120);
            startActivity(discoverableIntent);
        }
        BluetoothChatService bluetoothChatService = new BluetoothChatService(getApplicationContext(), mHandler);
        bluetoothChatService.start();
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
        if(completed)
        {
            (new Setup(getApplicationContext())).start();
            logEvent(CONFIGURATION_TAG, "connected to Wi-Fi");
        }

        return completed;
    }

    private void connectToBLE() {
        BluetoothManager manager = (BluetoothManager) getSystemService(BLUETOOTH_SERVICE);
        bluetoothAdapter = manager.getAdapter();
        if(bluetoothAdapter.getScanMode() != BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE)
        {
            Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
            discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 120);
            startActivity(discoverableIntent);
        }
        Log.i("BLE", "setting up");
        logEvent(CONFIGURATION_TAG, "setting up BLE");
        bluetoothAdapter = manager.getAdapter();
        stopScan();
        startScan();
    }

    //    private void connectToBLE() {
    //        BluetoothManager manager = (BluetoothManager) getSystemService(BLUETOOTH_SERVICE);
    //        bluetoothAdapter = manager.getAdapter();
    //        if(bluetoothAdapter.getScanMode() != BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE)
    //        {
    //            Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
    //            discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 120);
    //            startActivity(discoverableIntent);
    //        }
    //        Log.i("BLE", "setting up");
    //        logEvent(CONFIGURATION_TAG, "setting up BLE");
    //        bluetoothAdapter = manager.getAdapter();
    //        stopScan();
    //        startScan();
    //    }

    private void logEvent(String type, String message) {
        Intent intent = new Intent(getBaseContext(), HistorianBroadcastReceiver.class);
        intent.putExtra(HistorianBroadcastReceiver.LOG_TYPE, HistorianBroadcastReceiver.LogType.System);
        intent.putExtra(HistorianBroadcastReceiver.TYPE, type);
        intent.putExtra(HistorianBroadcastReceiver.MESSAGE, message);
        intent.putExtra(HistorianBroadcastReceiver.TIME, Calendar.getInstance().getTime().toString());
        sendBroadcast(intent);
    }

    private class Setup extends Thread {

        private Context context;

        public Setup(Context context) {
            this.context = context;
        }

        @Override
        public void run() {
            loadData();
            initializeConfiguration();
            //connectToBLE();
        }

        private void loadData() {
            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
            String homeId = sharedPreferences.getString(HOME_ID, "0");
            HomeRepository homeRepository = new JSONHomeRepository(context);
            Home home = homeRepository.getHome(homeId);
            ((Alexandra) getApplicationContext()).setHomeManager(new LocalHomeManagerDecorator((new HomeManager(home, homeRepository, context))));
            Intent syncIntent = new Intent(getApplicationContext(), FirebaseSyncService.class);
            startService(syncIntent);
        }

        private void initializeConfiguration() {
            FirebaseCurrentStateObserver.getInstance();
            ScheduleManager.getInstance(context);

            Intent remoteControlIntent = new Intent(context, FirebaseControlMessageDispatcher.class);
            startService(remoteControlIntent);
            Intent remoteControlIntent2 = new Intent(context, SocketIOControlMessageDispatcher.class);
            startService(remoteControlIntent2);
        }

    }
}
