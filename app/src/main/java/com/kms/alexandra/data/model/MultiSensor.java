package com.kms.alexandra.data.model;


import org.json.JSONObject;

import java.util.List;
import java.util.Map;
import java.util.UUID;


/**
 * @author Mateusz Zasoński
 */
public class MultiSensor extends Gadget {

    protected List<Sensor> sensors;

    public MultiSensor(UUID id, String temporaryRoomId, String name, String MAC, GadgetType type, int channels, boolean installed, int icon, int firmware) {
        super(id, temporaryRoomId, name, MAC, type, channels, installed, icon, firmware);
    }

    public MultiSensor(UUID id, Room room, String name, String MAC, GadgetType type, int channels, boolean installed, int icon, int firmware) {
        super(id, room, name, MAC, type, channels, installed, icon, firmware);
    }

    @Override
    public JSONObject toJSON() {
        return null;
    }

    @Override
    public Map<String, Object> getCurrentState() {
        return null;
    }

    @Override
    public String[] getSupportedActions() {
        return new String[0];
    }

    @Override
    public BaseAction prepare(ActionMessage actionMessage) {
        return null;
    }
}
