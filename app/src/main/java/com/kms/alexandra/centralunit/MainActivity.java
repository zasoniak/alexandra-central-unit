package com.kms.alexandra.centralunit;


import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothProfile;
import android.content.BroadcastReceiver;
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
import com.kms.alexandra.data.model.gadgets.Gadget;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.UUID;


/**
 * @author Mateusz Zasoński
 */
public class MainActivity extends Activity {

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
                    byte[] writeBuf = (byte[]) msg.obj;
                    // construct a string from the buffer
                    String writeMessage = new String(writeBuf);
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
    private BluetoothGattCallback gattCallback = new BluetoothGattCallback() {

        @Override
        public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
            Log.d(TAG, "Connection State Change: "+status+" -> "+connectionState(newState));
            if(status == BluetoothGatt.GATT_SUCCESS && newState == BluetoothProfile.STATE_CONNECTED)
            {
                Log.d(TAG, "Połączyło się! Powinien pójść service discovery");
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
            gattCheck(gatt);
            /*
             * With services discovered, we are going to reset our state machine and start
             * working through the sensors we need to enable
             */
        }

        @Override
        public void onCharacteristicRead(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
            //For each read, pass the data up to the UI thread to update the display

        }

        @Override
        public void onReadRemoteRssi(BluetoothGatt gatt, int rssi, int status) {
            Log.d(TAG, "Remote RSSI: "+rssi);
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
    // Create a BroadcastReceiver for ACTION_FOUND
    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            // When discovery finds a device
            if(BluetoothDevice.ACTION_FOUND.equals(action))
            {
                // Get the BluetoothDevice object from the Intent
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                // Add the name and address to an array adapter to show in a ListView
                partsArrayList.add(device.getName()+"\n"+device.getAddress());
                adapter.notifyDataSetChanged();

                if(device.getAddress().equals("00:1A:7D:DA:71:04") && !(devices.contains(device)))
                {
                    device.connectGatt(getApplicationContext(), true, gattCallback);
                    devices.add(device);
                }
            }
        }
    };
    private BluetoothAdapter.LeScanCallback leScanCallback = new BluetoothAdapter.LeScanCallback() {
        @Override
        public void onLeScan(BluetoothDevice bluetoothDevice, int i, byte[] bytes) {
            Log.d(TAG, "discovered device: "+bluetoothDevice.getName());
            Home home = ((Alexandra) getApplicationContext()).getHome();
            if(home != null)
            {
                for(Gadget gadget : home.getGadgets())
                {
                    if(gadget.getMAC().equals(bluetoothDevice.getAddress()))
                    {
                        Log.d(TAG, "trying to connect GATT");
                        gadget.setBluetoothGatt(bluetoothDevice.connectGatt(getApplicationContext(), true, gadget.getBluetoothGattCallback()));
                        devices.add(bluetoothDevice);
                    }
                }
            }
        }
    };
    public static final String CONFIGURED = "configured";
    public static final String HOME_ID = "home_id";
    public static final String HOME_NAME = "name";
    BluetoothAdapter bluetoothAdapter;
    private ArrayList<BluetoothDevice> devices;
    private ArrayList<String> partsArrayList;
    private ArrayAdapter<String> adapter;

    private void startScan() {
        Log.i("BLE", "start scan");
        bluetoothAdapter.startLeScan(leScanCallback);
    }

    private void stopScan() {
        Log.i("BLE", "stop scan");
        bluetoothAdapter.stopLeScan(leScanCallback);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Firebase.setAndroidContext(getApplicationContext());
        //        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        //        if(!(sharedPreferences.getBoolean(CONFIGURED, false)))
        //        {
        //            firstRunSetup();
        //        }
        //        else
        //        {
        //            (new Setup(getApplicationContext())).start();
        //        }
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
            case R.id.action_BLE:
                connectToBLE();
            default:
                return false;
        }
    }

    private void refresh() {
        if(((Alexandra) getApplicationContext()).getHome() != null)
        {
            partsArrayList.clear();
            partsArrayList.add(((Alexandra) getApplicationContext()).getHome().getName());
            adapter.notifyDataSetChanged();
            for(Gadget gadget : ((Alexandra) getApplicationContext()).getHome().getGadgets())
            {
                Log.i(TAG, gadget.getName());
                partsArrayList.add(gadget.getName());
                adapter.notifyDataSetChanged();
            }

            //            if(devices != null)
            //            {
            //                Log.i("devices", String.valueOf(devices.size()));
            //                for(BluetoothDevice device : devices)
            //                {
            //                    Log.i("gattCheck", "up to go");
            //                    gattCheck(device.connectGatt(this, true, gattCallback));
            //                }
            //
            //            }

        }
    }

    private void gattCheck(BluetoothGatt gatt) {
        UUID serviceID = UUID.fromString("4146d76c-99fa-11e4-89d3-123b93f75cba");
        UUID characteristicID = UUID.fromString("4146db18-99fa-11e4-89d3-123b93f75cba");
        BluetoothGattCharacteristic characteristic = gatt.getService(serviceID).getCharacteristic(characteristicID);
        characteristic.setValue(40, BluetoothGattCharacteristic.FORMAT_SINT8, 0);
        Log.i("write char", "up to go");
        gatt.writeCharacteristic(characteristic);
    }

    private void loadDefault() {
        saveConfiguration("-JcMyexVThw7PEv2Z2qq");
        connectToWifi("Livebox-D69B", "2E62120CE85F61E6C402CE9E72");
    }

    private void saveConfiguration(String homeId) {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(HOME_ID, homeId);
        editor.putBoolean(CONFIGURED, true);
        editor.apply();
    }

    private void firstRunSetup() {
        BluetoothManager manager = (BluetoothManager) getSystemService(BLUETOOTH_SERVICE);
        bluetoothAdapter = manager.getAdapter();
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
        (new Setup(getApplicationContext())).start();
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
        bluetoothAdapter = manager.getAdapter();
        devices = new ArrayList<BluetoothDevice>();
        stopScan();
        startScan();
    }

    //    private void connectToBLE() {
    //        devices = new ArrayList<BluetoothDevice>();
    //        // Register the BroadcastReceiver
    //        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
    //        registerReceiver(mReceiver, filter); // Don't forget to unregister during onDestroy
    //        BluetoothManager manager = (BluetoothManager) getSystemService(BLUETOOTH_SERVICE);
    //        bluetoothAdapter = manager.getAdapter();
    //        if(bluetoothAdapter.getScanMode() != BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE)
    //        {
    //            Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
    //            discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 120);
    //            startActivity(discoverableIntent);
    //        }
    //        Log.i("discovery", "setting up");
    //        bluetoothAdapter = manager.getAdapter();
    //        bluetoothAdapter.startDiscovery();
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
            connectToBLE();
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
        }

    }
}
