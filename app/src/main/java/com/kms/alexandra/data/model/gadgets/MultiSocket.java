package com.kms.alexandra.data.model.gadgets;


import android.util.Log;

import com.kms.alexandra.data.model.Room;
import com.kms.alexandra.data.model.Switchable;
import com.kms.alexandra.data.model.actions.ActionMessage;
import com.kms.alexandra.data.model.actions.BaseAction;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;


/**
 * Created by Mateusz Zasoński on 2014-11-17.
 * MultiSocket - class for all devices with electric sockets like Wall Sockets, Extension Cords etc.'
 *
 * @author Mateusz Zasoński
 * @version 0.1
 */
public class MultiSocket extends Gadget implements Switchable {

    public static final UUID SERVICE_SOCKET = UUID.fromString("1f77b020-82d5-11e4-b4a9-0800200c9a66");
    public static final UUID CHARACTERISTIC_CURRENT_VALUE = UUID.fromString("1f77b021-82d5-11e4-b4a9-0800200c9a66");
    public static final UUID CHARACTERISTIC_STATE = UUID.fromString("1f77b022-82d5-11e4-b4a9-0800200c9a66");

    protected int channelsNumber;
    protected List<Socket> channels = new ArrayList<Socket>();
    protected boolean on;

    public MultiSocket(UUID id, Room room, String name, String address, GadgetType type, int channelsNumber, boolean installed, int icon, int firmware) {
        super(id, room, name, address, type, channelsNumber, installed, icon, firmware);
        this.channelsNumber = channelsNumber;
        this.on = false;
        for(int i = 0; i < channelsNumber; i++)
        {
            this.channels.add(new Socket());
        }
    }

    public MultiSocket(UUID id, String temporaryRoomId, String name, String address, GadgetType type, int channelsNumber, boolean installed, int icon, int firmware) {
        super(id, temporaryRoomId, name, address, type, channelsNumber, installed, icon, firmware);
        this.channelsNumber = channelsNumber;
        this.on = false;
        for(int i = 0; i < channelsNumber; i++)
        {
            this.channels.add(new Socket());
        }
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
        currentState.put("type", GadgetType.WallSocket.toString());
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
        return new String[] {"SwitchAll", "SwitchChannelOne", "SwitchChannelTwo",
                             "SwitchChannelThree", "SwitchChannelFour", "SwitchChannelFive",
                             "SwitchChannelSix"};
    }

    //    @Override
    //    public BaseAction prepare(ActionMessage actionMessage) {
    //
    //        switch(ActionType.valueOf(actionMessage.action))
    //        {
    //            case SwitchAll:
    //                Log.d("przygotowano", "swichtAll");
    //                setOn(Boolean.parseBoolean(actionMessage.parameter));
    //                return new ActionSwitchAll(this, this.bluetoothGatt, actionMessage.parameter);
    //            case SwitchChannelOne:
    //                Log.d("przygotowano", "switch channel one");
    //                setChannelOn(0, Boolean.parseBoolean(actionMessage.parameter));
    //                return new ActionSwitchChannelOne(this, this.bluetoothGatt, actionMessage.parameter);
    //            case SwitchChannelTwo:
    //                if(channelsNumber >= 2)
    //                {
    //                    Log.d("przygotowano", "switch channel two");
    //                    setChannelOn(1, Boolean.parseBoolean(actionMessage.parameter));
    //                    return new ActionSwitchChannelTwo(this, this.bluetoothGatt, actionMessage.parameter);
    //                }
    //            default:
    //                return null;
    //        }
    //    }


    @Override
    public BaseAction prepare(ActionMessage actionMessage) {

        switch(ActionType.valueOf(actionMessage.action))
        {
            case SwitchAll:
                Log.d("przygotowano", "swichtAll");
                return new BaseAction(this, actionMessage.action, this.bluetoothGatt, actionMessage.parameter, actionMessage.delay, SERVICE_SOCKET, CHARACTERISTIC_STATE);
            case SwitchChannelOne:
                Log.d("przygotowano", "switch channel one");
                return new BaseAction(this, actionMessage.action, this.bluetoothGatt, actionMessage.parameter, actionMessage.delay, SERVICE_SOCKET, CHARACTERISTIC_STATE);
            case SwitchChannelTwo:
                if(channelsNumber >= 2)
                {
                    Log.d("przygotowano", "switch channel two");
                    return new BaseAction(this, actionMessage.action, this.bluetoothGatt, actionMessage.parameter, actionMessage.delay, SERVICE_SOCKET, CHARACTERISTIC_STATE);
                }
            default:
                return null;
        }
    }

    @Override
    public boolean isOn() {
        boolean state = false;
        for(Socket socket : channels)
        {
            state |= socket.isOn();
        }
        if(this.on != state)
        {
            this.on = state;
        }
        return this.on;
    }

    @Override
    public void setOn(boolean state) {
        for(Socket socket : channels)
        {
            socket.setOn(state);
        }
        notifyObservers("isOn", String.valueOf(isOn()));
    }

    public int getChannelsNumber() {
        return this.channelsNumber;
    }

    public void setChannelOn(int channel, boolean state) {
        channels.get(channel).setOn(state);
        isOn();
        notifyObservers("isOnChannel"+String.valueOf(channel), String.valueOf(channels.get(channel).isOn()));
    }

    public boolean getChannelOn(int channel) {
        return channel > channelsNumber && channels.get(channel).isOn();
    }

    public double getChannelPowerConsuption(int channel) {
        if(channel < channelsNumber)
        {
            return channels.get(channel).getPowerConsumption();
        }
        return 0;
    }
}
