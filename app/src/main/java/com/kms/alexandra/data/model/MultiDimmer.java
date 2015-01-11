package com.kms.alexandra.data.model;


import android.util.Log;

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

    private List<Dimmer> channels = new ArrayList<Dimmer>();

    public MultiDimmer(UUID id, Room room, String name, String address, GadgetType type, int channelsNumber, boolean installed, int icon, int firmware) {
        super(id, room, name, address, type, channelsNumber, installed, icon, firmware);
    }

    public MultiDimmer(UUID id, String temporaryRoomId, String name, String address, GadgetType type, int socketNumber, boolean installed, int icon, int firmware) {
        super(id, temporaryRoomId, name, address, type, socketNumber, installed, icon, firmware);
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
            case SetBrightness:
                Log.d("przygotowano", "SetBrightness");
                setBrightness(Integer.parseInt(actionMessage.parameter));
                return new ActionSetBrightness(this.id, this.gatt, actionMessage.parameter);
            case SetBrightnessChannelOne:
                Log.d("przygotowano", "SetBrightnessChannelOne");
                setChannelBrightness(0, Integer.parseInt(actionMessage.parameter));
                return new ActionSetBrightnessChannelOne(this.id, this.gatt, actionMessage.parameter);
            case SetBrightnessChannelTwo:
                if(channelsNumber >= 2)
                {
                    Log.d("przygotowano", "SetBrightnessChannelTwo");
                    setChannelBrightness(1, Integer.parseInt(actionMessage.parameter));
                    return new ActionSetBrightnessChannelTwo(this.id, this.gatt, actionMessage.parameter);
                }
                return null;
            default:
                return null;
        }
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

}
