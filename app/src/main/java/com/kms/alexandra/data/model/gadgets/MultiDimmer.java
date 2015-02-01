package com.kms.alexandra.data.model.gadgets;


import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothProfile;
import android.util.Log;

import com.kms.alexandra.data.model.Room;
import com.kms.alexandra.data.model.actions.ActionMessage;
import com.kms.alexandra.data.model.actions.BaseAction;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

//import android.content.SharedPreferences;
//import android.preference.PreferenceManager;
//import com.firebase.client.ChildEventListener;
//import com.firebase.client.DataSnapshot;
//import com.firebase.client.Firebase;
//import com.firebase.client.FirebaseError;
//import com.kms.alexandra.centralunit.Alexandra;
//import com.kms.alexandra.centralunit.MainActivity;


/**
 * @author Mateusz Zasoński
 */
public class MultiDimmer extends MultiLight {

    public static final String TAG = "MutliDimmer";
    private static final UUID SERVICE_DIMMER = UUID.fromString("4146d76c-99fa-11e4-89d3-123b93f75cba");
    private static final UUID CHARACTERISTIC_STATE = UUID.fromString("4146d9ba-99fa-11e4-89d3-123b93f75cba");
    private static final UUID CHARACTERISTIC_BRIGHTNESS_VALUE = UUID.fromString("4146db18-99fa-11e4-89d3-123b93f75cba");
    private static final UUID CLIENT_CHARACTERISTIC_CONFIG = UUID.fromString("00002902-0000-1000-8000-00805f9b34fb");

    private List<Dimmer> channels = new ArrayList<Dimmer>();

    public MultiDimmer(UUID id, Room room, String name, String address, GadgetType type, final int channelsNumber, boolean installed, int icon, int firmware) {
        super(id, room, name, address, type, channelsNumber, installed, icon, firmware);
        for(int i = 0; i < channelsNumber; i++)
        {
            this.channels.add(new Dimmer());
        }
        configureGATT();
    }

    public MultiDimmer(UUID id, String temporaryRoomId, String name, String address, GadgetType type, int socketNumber, boolean installed, int icon, int firmware) {
        super(id, temporaryRoomId, name, address, type, socketNumber, installed, icon, firmware);

        for(int i = 0; i < channelsNumber; i++)
        {
            this.channels.add(new Dimmer());
        }
        configureGATT();
    }

    private void configureGATT() {
        bluetoothGattCallback = new BluetoothGattCallback() {

            int gattState = 0;
            @Override
            public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
                Log.d(TAG, "Connection State Change: "+status+" -> "+connectionState(newState));
                if(status == BluetoothGatt.GATT_SUCCESS && newState == BluetoothProfile.STATE_CONNECTED)
                {
                    Log.d(TAG, "Połączyło się! Powinien pójść service discovery");
                    bluetoothGatt = gatt;
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
                        state = GadgetState.Offline;
                    }
                    else
                    {
                        if(status != BluetoothGatt.GATT_SUCCESS)
                        {
                            state = GadgetState.Error;
                            gatt.disconnect();
                        }
                    }
                }
            }

            @Override
            public void onServicesDiscovered(BluetoothGatt gatt, int status) {
                Log.d(TAG, "Services Discovered: "+status);
                state = GadgetState.OK;
                notifySetup(gatt);
            }

            @Override
            public void onCharacteristicRead(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
                if(characteristic.getUuid().equals(CHARACTERISTIC_BRIGHTNESS_VALUE))
                {
                    setChannelBrightness(0, characteristic.getIntValue(BluetoothGattCharacteristic.FORMAT_UINT8, 0));
                }
                if(characteristic.getUuid().equals(CHARACTERISTIC_STATE))
                {
                    setOn(Boolean.parseBoolean(characteristic.getStringValue(0)));
                }
            }

            @Override
            public void onCharacteristicWrite(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
                if(characteristic.getUuid().equals(CHARACTERISTIC_BRIGHTNESS_VALUE))
                {
                    setChannelBrightness(0, characteristic.getIntValue(BluetoothGattCharacteristic.FORMAT_UINT8, 0));
                }
                if(characteristic.getUuid().equals(CHARACTERISTIC_STATE))
                {
                    setOn(Boolean.parseBoolean(characteristic.getStringValue(0)));
                }
            }

            @Override
            public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
                Log.d("BLE", "changed char");
                if(characteristic.getUuid().equals(CHARACTERISTIC_BRIGHTNESS_VALUE))
                {
                    Log.d("BLE", "changed CHARACTERISTIC_BRIGHTNESS_VALUE");
                    setChannelBrightness(0, characteristic.getIntValue(BluetoothGattCharacteristic.FORMAT_UINT8, 0));
                }
                if(characteristic.getUuid().equals(CHARACTERISTIC_STATE))
                {
                    Log.d("BLE", "changed CHARACTERISTIC_STATE");
                    Log.d("przyszlo:", String.valueOf(characteristic.getIntValue(BluetoothGattCharacteristic.FORMAT_SINT8, 0)));
                    if(characteristic.getIntValue(BluetoothGattCharacteristic.FORMAT_SINT8, 0) == 1)
                    {
                        setChannelOn(0, true);
                        setOn(true);
                    }
                    else
                    {
                        setChannelOn(0, false);
                        setOn(false);
                    }
                }
            }

            @Override
            public void onDescriptorWrite(BluetoothGatt gatt, BluetoothGattDescriptor descriptor, int status) {
                notifySetup(gatt);
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

            private void notifySetup(BluetoothGatt gatt) {
                switch(gattState)
                {
                    case 0:
                    {
                        gattState++;
                        BluetoothGattCharacteristic characteristic = gatt.getService(SERVICE_DIMMER).getCharacteristic(CHARACTERISTIC_STATE);
                        gatt.setCharacteristicNotification(characteristic, true);
                        BluetoothGattDescriptor descriptor = characteristic.getDescriptor(CLIENT_CHARACTERISTIC_CONFIG);
                        descriptor.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
                        gatt.writeDescriptor(descriptor);

                        break;
                    }
                    case 1:
                    {
                        gattState++;
                        Log.d(TAG, "Notification enabled for: "+CHARACTERISTIC_STATE);
                        BluetoothGattCharacteristic characteristic = gatt.getService(SERVICE_DIMMER).getCharacteristic(CHARACTERISTIC_BRIGHTNESS_VALUE);
                        gatt.setCharacteristicNotification(characteristic, true);
                        BluetoothGattDescriptor descriptor = characteristic.getDescriptor(CLIENT_CHARACTERISTIC_CONFIG);
                        descriptor.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
                        gatt.writeDescriptor(descriptor);

                        break;
                    }
                    default:
                        Log.d(TAG, "Notification enabled for: "+CHARACTERISTIC_BRIGHTNESS_VALUE);
                        break;
                }
            }
        };
    }

    @Override
    public JSONObject toJSON() {
        JSONObject jsonObject = new JSONObject();
        try
        {
            jsonObject.put("type", GadgetType.WallSocket.toString());
            jsonObject.put("channelsNumber", this.channelsNumber);
            jsonObject.put(Switch.ON, this.isOn());

            for(int i = 0; i < channels.size(); i++)
            {
                jsonObject.put(String.valueOf(i), channels.get(i).toJSON());
            }
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Map<String, Object> getCurrentState() {
        Map<String, Object> currentState = new HashMap<String, Object>();
        currentState.put("type", GadgetType.Dimmer.toString());
        currentState.put("channelsNumber", this.channelsNumber);
        currentState.put("state", this.state);
        currentState.put("on", this.isOn());

        for(int i = 0; i < channels.size(); i++)
        {
            currentState.put(String.valueOf(i), channels.get(i).getCurrentState());
        }
        return currentState;
    }

    @Override
    public String[] getSupportedActions() {
        return new String[] {"SwitchAll", "SwitchChannelOne", "SwitchChannelTwo", "SetBrightness",
                             "SetBrightnessChannelOne", "SetBrightnessChannelTwo"};
    }

    @Override
    public BaseAction prepare(ActionMessage actionMessage) {

        switch(ActionType.valueOf(actionMessage.action))
        {
            case SwitchAll:
                Log.d("przygotowano", "swichtAll");
                return new BaseAction(this, actionMessage.action, this.bluetoothGatt, actionMessage.parameter, actionMessage.delay, SERVICE_DIMMER, CHARACTERISTIC_STATE);
            case SwitchChannelOne:
                Log.d("przygotowano", "switch channel one");
                return new BaseAction(this, actionMessage.action, this.bluetoothGatt, actionMessage.parameter, actionMessage.delay, SERVICE_DIMMER, CHARACTERISTIC_STATE);
            case SwitchChannelTwo:
                if(channelsNumber >= 2)
                {
                    Log.d("przygotowano", "switch channel two");
                    return new BaseAction(this, actionMessage.action, this.bluetoothGatt, actionMessage.parameter, actionMessage.delay, SERVICE_DIMMER, CHARACTERISTIC_STATE);
                }
                return null;
            case SetBrightness:
                Log.d("przygotowano", "SetBrightness");
                return new BaseAction(this, actionMessage.action, this.bluetoothGatt, actionMessage.parameter, actionMessage.delay, SERVICE_DIMMER, CHARACTERISTIC_BRIGHTNESS_VALUE);

            case SetBrightnessChannelOne:
                Log.d("przygotowano", "SetBrightnessChannelOne");
                return new BaseAction(this, actionMessage.action, this.bluetoothGatt, actionMessage.parameter, actionMessage.delay, SERVICE_DIMMER, CHARACTERISTIC_BRIGHTNESS_VALUE);

            case SetBrightnessChannelTwo:
                if(channelsNumber >= 2)
                {
                    Log.d("przygotowano", "SetBrightnessChannelTwo");
                    return new BaseAction(this, actionMessage.action, this.bluetoothGatt, actionMessage.parameter, actionMessage.delay, SERVICE_DIMMER, CHARACTERISTIC_BRIGHTNESS_VALUE);
                }
                return null;
            default:
                return null;
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

    public void setBrightness(int brightness) {
        for(Dimmer channel : channels)
        {
            channel.setBrightness(brightness);
        }
        notifyObservers("brightness", String.valueOf(brightness));
    }

    public void setChannelBrightness(int channel, int brightness) {
        Log.d(TAG, "setChannelBrightness "+String.valueOf(channel)+" "+String.valueOf(brightness));
        channels.get(channel).setBrightness(brightness);
        notifyObservers("brightnessChannel"+String.valueOf(channel), String.valueOf(channels.get(channel).isOn()));
    }

    public int getChannelBrightness(int channel) {
        if(channel < channelsNumber)
        {
            return channels.get(channel).getBrightness();
        }
        return 0;
    }

    //    private class FirebaseCurrentStateListener {
    //
    //        public static final String STATE = "state";
    //        public static final String BRIGHTNESS = "brightness";
    //        public static final String ON = "on";
    //
    //        private FirebaseCurrentStateListener() {
    //
    //            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(Alexandra.getContext());
    //            String homeId = sharedPreferences.getString(MainActivity.HOME_ID, "-JcMyexVThw7PEv2Z2PL");
    //
    //            Firebase stateListener = new Firebase("https://sizzling-torch-8921.firebaseio.com/currentState/"+homeId+"/"+id.toString()+"/");
    //            stateListener.addChildEventListener(new ChildEventListener() {
    //                @Override
    //                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
    //                    if(dataSnapshot.getKey().equals(STATE))
    //                    {
    //                        state = GadgetState.valueOf(dataSnapshot.getValue().toString());
    //                    }
    //                    else
    //                    {
    //                        if(dataSnapshot.getKey().equals(ON))
    //                        {
    //                            setOn(Boolean.parseBoolean(dataSnapshot.getValue().toString()));
    //                        }
    //                        else
    //                        {
    //                            if(dataSnapshot.getKey().equals("0"))
    //                            {
    //                                setChannelBrightness(0, Integer.parseInt(dataSnapshot.child(BRIGHTNESS).getValue().toString()));
    //                                setChannelOn(0, Boolean.parseBoolean(dataSnapshot.child(ON).getValue().toString()));
    //                            }
    //                            else
    //                            {
    //                                if(dataSnapshot.getKey().equals("1"))
    //                                {
    //                                    setChannelBrightness(1, Integer.parseInt(dataSnapshot.child(BRIGHTNESS).getValue().toString()));
    //                                    setChannelOn(1, Boolean.parseBoolean(dataSnapshot.child(ON).getValue().toString()));
    //                                }
    //                            }
    //                        }
    //                    }
    //                }
    //
    //                @Override
    //                public void onChildChanged(DataSnapshot dataSnapshot, String s) {
    //                    if(dataSnapshot.getKey().equals(STATE))
    //                    {
    //                        state = GadgetState.valueOf(dataSnapshot.getValue().toString());
    //                    }
    //                    else
    //                    {
    //                        if(dataSnapshot.getKey().equals(ON))
    //                        {
    //                            setOn(Boolean.parseBoolean(dataSnapshot.getValue().toString()));
    //                        }
    //                        else
    //                        {
    //                            if(dataSnapshot.getKey().equals("0"))
    //                            {
    //                                setChannelBrightness(0, Integer.parseInt(dataSnapshot.child(BRIGHTNESS).getValue().toString()));
    //                                setChannelOn(0, Boolean.parseBoolean(dataSnapshot.child(ON).getValue().toString()));
    //                            }
    //                            else
    //                            {
    //                                if(dataSnapshot.getKey().equals("1"))
    //                                {
    //                                    setChannelBrightness(1, Integer.parseInt(dataSnapshot.child(BRIGHTNESS).getValue().toString()));
    //                                    setChannelOn(1, Boolean.parseBoolean(dataSnapshot.child(ON).getValue().toString()));
    //                                }
    //                            }
    //                        }
    //                    }
    //                }
    //
    //                @Override
    //                public void onChildRemoved(DataSnapshot dataSnapshot) {
    //
    //                }
    //
    //                @Override
    //                public void onChildMoved(DataSnapshot dataSnapshot, String s) {
    //
    //                }
    //
    //                @Override
    //                public void onCancelled(FirebaseError firebaseError) {
    //
    //                }
    //            });
    //        }
    //    }


}
