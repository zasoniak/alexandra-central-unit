package com.kms.alexandra.data.model;


import android.util.Log;

import com.kms.alexandra.data.model.actions2.ActionMessage;
import com.kms.alexandra.data.model.actions2.ActionSwitchAll;
import com.kms.alexandra.data.model.actions2.ActionSwitchChannelOne;
import com.kms.alexandra.data.model.actions2.ActionSwitchChannelTwo;
import com.kms.alexandra.data.model.actions2.BaseAction;

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
public class MultiLight extends Gadget implements Switchable {

    protected int channelsNumber;
    protected boolean on;
    private List<Light> channels = new ArrayList<Light>();

    public MultiLight(UUID id, Room room, String name, String address, GadgetType type, int channelsNumber, boolean installed, int icon, int firmware) {
        super(id, room, name, address, type, channelsNumber, installed, icon, firmware);
        this.channelsNumber = channelsNumber;
        this.on = false;
        for(int i = 0; i < channelsNumber; i++)
        {
            this.channels.add(new Light());
        }
    }

    public MultiLight(UUID id, String temporaryRoomId, String name, String address, GadgetType type, int socketNumber, boolean installed, int icon, int firmware) {
        super(id, temporaryRoomId, name, address, type, socketNumber, installed, icon, firmware);
        this.channelsNumber = socketNumber;
        this.on = false;
        for(int i = 0; i < socketNumber; i++)
        {
            this.channels.add(new Light());
        }
    }

    @Override
    public JSONObject toJSON() {
        JSONObject jsonObject = new JSONObject();
        try
        {
            jsonObject.put("type", GadgetType.LightSwitch.toString());
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
        currentState.put("type", GadgetType.LightSwitch.toString());
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
        return new String[] {"SwitchAll", "SwitchChannelOne", "SwitchChannelTwo"};
    }

    @Override
    public BaseAction prepare(ActionMessage actionMessage) {

        switch(ActionType.valueOf(actionMessage.action))
        {
            case SwitchAll:
                Log.d("przygotowano", "SwitchAll");
                setOn(Boolean.parseBoolean(actionMessage.parameter));
                return new ActionSwitchAll(this.id, this.gatt, actionMessage.parameter);
            case SwitchChannelOne:
                Log.d("przygotowano", "SwitchChannelOne");
                setChannelOn(0, Boolean.parseBoolean(actionMessage.parameter));
                return new ActionSwitchChannelOne(this.id, this.gatt, actionMessage.parameter);
            case SwitchChannelTwo:
                if(channelsNumber >= 2)
                {
                    Log.d("przygotowano", "SwitchChannelTwo");
                    setChannelOn(1, Boolean.parseBoolean(actionMessage.parameter));
                    return new ActionSwitchChannelTwo(this.id, this.gatt, actionMessage.parameter);
                }
                return null;
            default:
                return null;
        }
    }

    @Override
    public boolean isOn() {
        boolean state = false;
        for(Light light : channels)
        {
            state |= light.isOn();
        }
        if(this.on != state)
        {
            this.on = state;
        }
        return this.on;
    }

    @Override
    public void setOn(boolean state) {
        for(Light light : channels)
        {
            light.setOn(state);
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
        return channels.get(channel).isOn();
    }

}
