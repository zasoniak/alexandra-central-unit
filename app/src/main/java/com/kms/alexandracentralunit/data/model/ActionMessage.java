package com.kms.alexandracentralunit.data.model;


import java.util.UUID;


/**
 * Created by Mateusz Zasoński on 2014-11-23.
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
