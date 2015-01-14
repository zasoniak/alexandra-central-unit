package com.kms.alexandra.data.model.gadgets;


import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothProfile;
import android.util.Log;

import com.kms.alexandra.data.model.Room;
import com.kms.alexandra.data.model.actions.ActionMessage;
import com.kms.alexandra.data.model.actions.ActionSetBrightness;
import com.kms.alexandra.data.model.actions.ActionSetBrightnessChannelOne;
import com.kms.alexandra.data.model.actions.ActionSetBrightnessChannelTwo;
import com.kms.alexandra.data.model.actions.ActionSwitchAll;
import com.kms.alexandra.data.model.actions.ActionSwitchChannelOne;
import com.kms.alexandra.data.model.actions.ActionSwitchChannelTwo;
import com.kms.alexandra.data.model.actions.BaseAction;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;


/**
 * @author Mateusz Zasoński
 */
public class MultiDimmer extends MultiLight {

    public static final String TAG = "MutliDimmer";
    private static final UUID SERVICE_DIMMER = UUID.fromString("4146d76c-99fa-11e4-89d3-123b93f75cba");
    private static final UUID CHARACTERISTIC_BRIGHTNESS_VALUE = UUID.fromString("4146db18-99fa-11e4-89d3-123b93f75cba");

    private List<Dimmer> channels = new ArrayList<Dimmer>();

    public MultiDimmer(UUID id, Room room, String name, String address, GadgetType type, int channelsNumber, boolean installed, int icon, int firmware) {
        super(id, room, name, address, type, channelsNumber, installed, icon, firmware);

        for(int i = 0; i < channelsNumber; i++)
        {
            this.channels.add(new Dimmer());
        }

        bluetoothGattCallback = new BluetoothGattCallback() {

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

            @Override
            public void onServicesDiscovered(BluetoothGatt gatt, int status) {
                Log.d(TAG, "Services Discovered: "+status);
                state = GadgetState.OK;
            /*
             * With services discovered, we are going to reset our state machine and start
             * working through the sensors we need to enable
             */
                gattCheck(gatt);
            }

            @Override
            public void onCharacteristicRead(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
                if(characteristic.getUuid().equals(CHARACTERISTIC_BRIGHTNESS_VALUE))
                {
                    setChannelBrightness(0, characteristic.getIntValue(BluetoothGattCharacteristic.FORMAT_UINT8, 0));
                }
                //For each read, pass the data up to the UI thread to update the display

            }

            @Override
            public void onCharacteristicWrite(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
                if(characteristic.getUuid().equals(CHARACTERISTIC_BRIGHTNESS_VALUE))
                {
                    setChannelBrightness(0, characteristic.getIntValue(BluetoothGattCharacteristic.FORMAT_UINT8, 0));
                }
            }

            @Override
            public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
                if(characteristic.getUuid().equals(CHARACTERISTIC_BRIGHTNESS_VALUE))
                {
                    setChannelBrightness(0, characteristic.getIntValue(BluetoothGattCharacteristic.FORMAT_UINT8, 0));
                }
            }

            @Override
            public void onReadRemoteRssi(BluetoothGatt gatt, int rssi, int status) {
                Log.d(TAG, "Remote RSSI: "+rssi);
            }

        };

    }

    public MultiDimmer(UUID id, String temporaryRoomId, String name, String address, GadgetType type, int socketNumber, boolean installed, int icon, int firmware) {
        super(id, temporaryRoomId, name, address, type, socketNumber, installed, icon, firmware);

        for(int i = 0; i < channelsNumber; i++)
        {
            this.channels.add(new Dimmer());
        }
        bluetoothGattCallback = new BluetoothGattCallback() {

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
            }

            @Override
            public void onCharacteristicRead(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
                if(characteristic.getUuid().equals(CHARACTERISTIC_BRIGHTNESS_VALUE))
                {
                    setChannelBrightness(0, characteristic.getIntValue(BluetoothGattCharacteristic.FORMAT_UINT8, 0));
                }
                //For each read, pass the data up to the UI thread to update the display

            }

            @Override
            public void onCharacteristicWrite(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
                if(characteristic.getUuid().equals(CHARACTERISTIC_BRIGHTNESS_VALUE))
                {
                    setChannelBrightness(0, characteristic.getIntValue(BluetoothGattCharacteristic.FORMAT_UINT8, 0));
                }
            }

            @Override
            public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
                if(characteristic.getUuid().equals(CHARACTERISTIC_BRIGHTNESS_VALUE))
                {
                    setChannelBrightness(0, characteristic.getIntValue(BluetoothGattCharacteristic.FORMAT_UINT8, 0));
                }
            }

            @Override
            public void onReadRemoteRssi(BluetoothGatt gatt, int rssi, int status) {
                Log.d(TAG, "Remote RSSI: "+rssi);
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
                Log.d("przygotowano", "SwitchAll");
                setOn(Boolean.parseBoolean(actionMessage.parameter));
                return new ActionSwitchAll(this.id, this.bluetoothGatt, actionMessage.parameter);
            case SwitchChannelOne:
                Log.d("przygotowano", "SwitchChannelOne");
                setChannelOn(0, Boolean.parseBoolean(actionMessage.parameter));
                return new ActionSwitchChannelOne(this.id, this.bluetoothGatt, actionMessage.parameter);
            case SwitchChannelTwo:
                if(channelsNumber >= 2)
                {
                    Log.d("przygotowano", "SwitchChannelTwo");
                    setChannelOn(1, Boolean.parseBoolean(actionMessage.parameter));
                    return new ActionSwitchChannelTwo(this.id, this.bluetoothGatt, actionMessage.parameter);
                }
                return null;
            case SetBrightness:
                Log.d("przygotowano", "SetBrightness");
                setBrightness(Integer.parseInt(actionMessage.parameter));
                return new ActionSetBrightness(this.id, this.bluetoothGatt, actionMessage.parameter);
            case SetBrightnessChannelOne:
                Log.d("przygotowano", "SetBrightnessChannelOne");
                setChannelBrightness(0, Integer.parseInt(actionMessage.parameter));
                return new ActionSetBrightnessChannelOne(this.id, this.bluetoothGatt, actionMessage.parameter);
            case SetBrightnessChannelTwo:
                if(channelsNumber >= 2)
                {
                    Log.d("przygotowano", "SetBrightnessChannelTwo");
                    setChannelBrightness(1, Integer.parseInt(actionMessage.parameter));
                    return new ActionSetBrightnessChannelTwo(this.id, this.bluetoothGatt, actionMessage.parameter);
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


}
