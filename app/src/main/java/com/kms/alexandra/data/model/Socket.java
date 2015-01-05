package com.kms.alexandra.data.model;


import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


/**
 * Created by Mateusz Zasoński on 2014-11-17.
 * Socket - base for single electric socket
 */
public class Socket extends Switch {

    public static final String POWER_CONSUMPTION = "powerConsumption";

    protected double powerConsumption;

    public Socket() {
        this.powerConsumption = 0;
    }

    public double getPowerConsumption() {
        return powerConsumption;
    }

    @Override
    public JSONObject toJSON() {
        JSONObject jsonObject = new JSONObject();
        try
        {
            jsonObject.put(ON, this.on);
            jsonObject.put(POWER_CONSUMPTION, this.powerConsumption);
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
        return jsonObject;
    }

    public Map<String, Object> getCurrentState() {
        Map<String, Object> currentState = new HashMap<String, Object>();
        currentState.put(ON, this.on);
        currentState.put(POWER_CONSUMPTION, this.powerConsumption);
        return currentState;
    }
}
