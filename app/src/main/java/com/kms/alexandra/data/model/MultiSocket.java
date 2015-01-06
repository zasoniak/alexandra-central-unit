package com.kms.alexandra.data.model;


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
    protected boolean on;

    public MultiSocket(UUID id, String roomId, String name, String address, GadgetType type, int socketNumber, boolean installed) {
        super(id, roomId, name, address, type, socketNumber, installed);
        this.socketNumber = socketNumber;
        this.on = false;
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
    public String[] getSupportedActions() {
        return new String[] {"SwitchAll", "SwitchChannelOne", "SwitchChannelTwo",
                             "SwitchChannelThree", "SwitchChannelFour", "SwitchChannelFive",
                             "SwitchChannelSix"};
    }

    @Override
    public BaseAction prepare(ActionMessage actionMessage) {

        switch(ActionType.valueOf(actionMessage.action))
        {
            case SwitchAll:
                Log.d("przygotowano", "swichtAll");
                setOn(Boolean.parseBoolean(actionMessage.parameter));
                return new ActionSwitchAll(this.id, this.gatt, actionMessage.parameter);
            case SwitchChannelOne:
                Log.d("przygotowano", "switch channel one");
                setChannelOn(0, Boolean.parseBoolean(actionMessage.parameter));
                return new ActionSwitchChannelOne(this.id, this.gatt, actionMessage.parameter);
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

    public int getSocketNumber() {
        return this.socketNumber;
    }

    public void setChannelOn(int channel, boolean state) {
        channels.get(channel).setOn(state);
        isOn();
        notifyObservers("isOnChannel"+String.valueOf(channel), String.valueOf(channels.get(channel).isOn()));
    }

    public boolean getChannelOn(int channel) {
        return channels.get(channel).isOn();
    }

    public double getChannelPowerConsuption(int channel) {
        return channels.get(channel).getPowerConsumption();
    }

    //    public static enum ActionType {
    //        SwitchAll,
    //        SwitchChannelOne,
    //        SwitchChannelTwo,
    //        SwitchChannelThree,
    //        SwitchChannelFour,
    //        SwitchChannelFive,
    //        SwitchChannelSix
    //    }
}
