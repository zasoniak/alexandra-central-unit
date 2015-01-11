package com.kms.alexandra.data.model.gadgets;


import com.kms.alexandra.data.model.Switchable;

import org.json.JSONException;
import org.json.JSONObject;


/**
 * Created by Mateusz Zasoński on 2014-11-17.
 * Switch - base class for all providing simple on/off interface
 */
public class Switch implements Switchable {

    public static final String ON = "on";
    protected boolean on;

    public Switch() {
        this.on = false;
    }

    @Override
    public boolean isOn() {
        return this.on;
    }

    @Override
    public void setOn(boolean state) {
        this.on = state;
    }

    public JSONObject toJSON() {
        JSONObject jsonObject = new JSONObject();
        try
        {
            jsonObject.put(ON, this.on);
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
        return jsonObject;
    }
}
