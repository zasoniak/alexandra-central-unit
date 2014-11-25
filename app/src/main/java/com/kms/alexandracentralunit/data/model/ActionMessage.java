package com.kms.alexandracentralunit.data.model;


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

    public UUID gadgetID;
    public String action;
    public String parameter;
    public long delay;

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
}
