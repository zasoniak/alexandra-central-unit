package com.kms.alexandracentralunit.data.model;


import java.util.UUID;


/**
 * Structure for events driven messages.
 *
 * @author Mateusz Zasoński
 * @version 0.1
 */
public class ActionMessage {

    public UUID gadgetID;
    public String action;
    public String parameter;

    public ActionMessage(UUID gadgetID, String action, String parameter) {
        this.gadgetID = gadgetID;
        this.action = action;
        this.parameter = parameter;
    }
}
