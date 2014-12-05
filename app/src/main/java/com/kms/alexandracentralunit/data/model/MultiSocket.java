package com.kms.alexandracentralunit.data.model;


import android.util.Log;

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

    protected int socketNumber;
    protected List<Socket> channels = new ArrayList<Socket>();

    public MultiSocket(UUID id, String system, String roomId, String name, String address, GadgetType type, int socketNumber) {
        super(id, system, roomId, name, address, type);
        this.socketNumber = socketNumber;
        for(int i = 0; i < socketNumber; i++)
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
            jsonObject.put("socketNumber", this.socketNumber);
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

    public Map<String, Object> getCurrentState() {
        Map<String, Object> currentState = new HashMap<String, Object>();
        currentState.put("type", GadgetType.WallSocket.toString());
        currentState.put("socketNumber", this.socketNumber);
        currentState.put("state", this.state);
        currentState.put("on", this.isOn());

        for(int i = 0; i < channels.size(); i++)
        {
            currentState.put(String.valueOf(i), channels.get(i).getCurrentState());
        }
        return currentState;
    }

    @Override
    public BaseAction prepare(ActionMessage actionMessage) {

        switch(ActionType.valueOf(actionMessage.action))
        {
            case SwitchAll:
                return new ActionSwitchAll(this.gatt, actionMessage.parameter);
            case SwitchChannelOne:
                return new ActionSwitchChannelOne(this.gatt, actionMessage.parameter);
            case SwitchChannelTwo:
                return null;
            case SwitchChannelThree:
                return null;
            case SwitchChannelFour:
                return null;
            case SwitchChannelFive:
                return null;
            case SwitchChannelSix:
                return null;
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
        return state;
    }

    @Override
    public void setOn(boolean state) {
        Log.d("cale gniazdko", "setOn");
        for(Socket socket : channels)
        {
            socket.setOn(state);
        }
        notifyObservers("isOn", isOn());
    }

    public int getSocketNumber() {
        return socketNumber;
    }

    public void setChannelOn(int channel, boolean state) {
        Log.d("socket "+String.valueOf(channel), "ustawiono na: "+String.valueOf(state));
        channels.get(channel).setOn(state);
        notifyObservers("isOnChannel"+String.valueOf(channel), channels.get(channel).isOn());
    }

    public boolean getChannelOn(int channel) {
        return channels.get(channel).isOn();
    }

    public double getChannelPowerConsuption(int channel) {
        return channels.get(channel).getPowerConsumption();
    }

    public static enum ActionType {
        SwitchAll,
        SwitchChannelOne,
        SwitchChannelTwo,
        SwitchChannelThree,
        SwitchChannelFour,
        SwitchChannelFive,
        SwitchChannelSix
    }
}
