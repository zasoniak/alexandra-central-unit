package com.kms.alexandra.data.model;


import android.util.ArrayMap;

import java.util.Map;
import java.util.UUID;


/**
 * Structure for gadgets' action creation
 * <p/>
 * Provides all data essential in action creation and execution process
 * As a structure all fields are public
 *
 * @author Mateusz Zasoński
 * @version 0.1
 */
public class ActionMessage {

    public static final String GADGET = "gadget";
    public static final String ACTION = "action";
    public static final String PARAMETER = "parameter";
    public static final String DELAY = "delay";
    public final UUID gadgetID;
    public final String action;
    public final String parameter;
    public final long delay;

    public ActionMessage(UUID gadgetID, String action, String parameter) {
        this.gadgetID = gadgetID;
        this.action = action;
        this.parameter = parameter;
        this.delay = 0;
    }

    public ActionMessage(UUID gadgetID, String action, String parameter, long delay) {
        this.gadgetID = gadgetID;
        this.action = action;
        this.parameter = parameter;
        this.delay = delay;
    }

    public Map<String, String> toMap() {
        Map<String, String> map = new ArrayMap<String, String>();
        map.put(GADGET, gadgetID.toString());
        map.put(ACTION, action);
        map.put(PARAMETER, parameter);
        map.put(DELAY, Long.toString(delay));
        return map;
    }
}
