package com.kms.alexandra.data.model.gadgets;


import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


/**
 * @author Mateusz Zasoński
 */
public class Dimmer extends Light {

    public static final String BRIGHTNESS = "brightness";

    private int brightness;

    public Dimmer() {
        this.brightness = 0;
    }

    public int getBrightness() {
        return brightness;
    }

    public void setBrightness(int brightness) {
        this.brightness = brightness;
    }

    @Override
    public void setOn(boolean state) {
        this.on = state;
    }

    public Map<String, Object> getCurrentState() {
        Map<String, Object> currentState = new HashMap<String, Object>();
        Log.d("dimmer", String.valueOf(this.on));
        Log.d("dimmer", String.valueOf(this.brightness));
        currentState.put(ON, this.on);
        currentState.put(BRIGHTNESS, this.brightness);
        return currentState;
    }

    @Override
    public JSONObject toJSON() {
        JSONObject jsonObject = new JSONObject();
        try
        {
            jsonObject.put(ON, this.on);
            jsonObject.put(BRIGHTNESS, this.brightness);
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
        return jsonObject;
    }
}
