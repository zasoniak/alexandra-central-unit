package com.kms.alexandra.centralunit;


import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.firebase.client.Firebase;
import com.kms.alexandra.R;
import com.kms.alexandra.data.HomeManager;
import com.kms.alexandra.data.LocalHomeManagerDecorator;
import com.kms.alexandra.data.database.HomeRepository;
import com.kms.alexandra.data.database.json.JSONHomeRepository;
import com.kms.alexandra.data.model.Home;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;


/**
 * @author Mateusz Zasoński
 */
public class MainActivity extends Activity {

    public static final String TAG = "Alexandra";
    public static final String CONFIGURED = "configured";
    public static final String HOME_ID = "home_id";
    public static final String HOME_NAME = "name";
    private final int DISCOVERY_REQUEST = 14124;
    private BluetoothSocketListener bluetoothSocketListener;
    private BluetoothAdapter bluetooth;
    private BluetoothSocket socket;
    private UUID uuid = UUID.fromString("498a6920-99dc-11e4-bd06-0800200c9a66");
    private boolean cancelFlag = true;

    private BroadcastReceiver receiver;
    private ArrayList<String> partsArrayList;

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
        saveConfiguration("-JcMyexVThw7PEv2Z2PL");
        boolean connectedToWifi = connectToWifi("Livebox-D69B", "2E62120CE85F61E6C402CE9E72");

        setContentView(R.layout.activity_admin);
        ListView listView = (ListView) findViewById(R.id.home_parts_list);
        partsArrayList = new ArrayList<String>();
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, partsArrayList);
        listView.setAdapter(adapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
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
            default:
                return false;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        final String BT_TAG = "Bluetooth";
        if(requestCode == DISCOVERY_REQUEST)
        {
            boolean isDiscoverable = resultCode > 0;
            if(isDiscoverable)
            {
                final String name = TAG+" central unit";
                try
                {
                    final BluetoothServerSocket bluetoothServerSocket = bluetooth.listenUsingRfcommWithServiceRecord(name, uuid);
                    AsyncTask<Integer, Void, BluetoothSocket> acceptThread = new AsyncTask<Integer, Void, BluetoothSocket>() {
                        @Override
                        protected BluetoothSocket doInBackground(Integer... params) {

                            try
                            {
                                socket = bluetoothServerSocket.accept(params[0]*2000);
                                return socket;
                            }
                            catch (IOException e)
                            {
                                Log.d(BT_TAG, e.getMessage());
                            }

                            return null;
                        }

                        @Override
                        protected void onPostExecute(BluetoothSocket result) {
                            if(result != null)
                            {
                                bluetoothSocketListener = new BluetoothSocketListener(result);
                                TimerTask cancel = new TimerTask() {
                                    @Override
                                    public void run() {
                                        cancelFlag = false;
                                    }
                                };
                                Timer cancelTimer = new Timer();
                                cancelTimer.schedule(cancel, 20000);
                            }
                        }
                    };
                    acceptThread.execute(resultCode);
                }
                catch (IOException e)
                {
                    Log.d(BT_TAG, e.getMessage());
                }
            }
        }
    }

    private void firstRunSetup() {
        configureBluetooth();
        ensureDiscoverable();
    }

    private void saveConfiguration(String homeId) {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(HOME_ID, homeId);
        editor.putBoolean(CONFIGURED, true);
        editor.apply();
    }

    private void configureBluetooth() {
        bluetooth = BluetoothAdapter.getDefaultAdapter();
    }

    private void ensureDiscoverable() {
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if(bluetoothAdapter.getScanMode() != BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE)
        {
            Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
            discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 120);
            startActivityForResult(discoverableIntent, DISCOVERY_REQUEST);
        }
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

    private void logEvent(String type, String message) {
        Intent intent = new Intent(getBaseContext(), HistorianBroadcastReceiver.class);
        intent.putExtra(HistorianBroadcastReceiver.LOG_TYPE, HistorianBroadcastReceiver.LogType.System);
        intent.putExtra(HistorianBroadcastReceiver.TYPE, type);
        intent.putExtra(HistorianBroadcastReceiver.MESSAGE, message);
        intent.putExtra(HistorianBroadcastReceiver.TIME, Calendar.getInstance().getTime().toString());
        sendBroadcast(intent);
    }

    private class BluetoothSocketListener implements Runnable {

        private BluetoothSocket socket;

        public BluetoothSocketListener(BluetoothSocket socket) {
            this.socket = socket;
        }

        public void run() {
            final int BUFFER_SIZE = 1024;
            byte[] buffer = new byte[BUFFER_SIZE];
            int bytes = 0;
            while(cancelFlag)
            {
                try
                {
                    InputStream inStream = socket.getInputStream();
                    bytes = inStream.read(buffer, bytes, BUFFER_SIZE-bytes);
                    String inputString = new String(buffer);
                    Log.d("przyszlo:", inputString);
                    saveConfiguration("rl");
                    connectToWifi("sd", "asd");
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
        }
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
        }

        private void loadData() {
            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
            String homeId = sharedPreferences.getString(HOME_ID, "-JcMyexVThw7PEv2Z2PL");
            HomeRepository homeRepository = new JSONHomeRepository(context);
            Home home = homeRepository.getHome(homeId);
            ((Alexandra) getApplicationContext()).setHomeManager(new LocalHomeManagerDecorator((new HomeManager(home, homeRepository, context))));
        }

        private void initializeConfiguration() {
            FirebaseCurrentStateObserver.getInstance();
            ScheduleManager.getInstance(context);

            Intent remoteControlIntent = new Intent(context, FirebaseControlMessageDispatcher.class);
            startService(remoteControlIntent);
        }
    }
}
