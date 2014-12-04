package com.kms.alexandracentralunit.data.model;


import org.json.JSONObject;

import java.util.List;
import java.util.Map;


/**
 * @author Mateusz Zasoński
 */
public class MultiSensor extends Gadget {

    protected List<Sensor> sensors;

    @Override
    public JSONObject toJSON() {
        return null;
    }

    @Override
    public Map<String, Object> getCurrentState() {
        return null;
    }
}
