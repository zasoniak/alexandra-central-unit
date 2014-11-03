package com.kms.alexandracentralunit.data.model;


/**
 * Created by Mateusz Zasoński on 2014-10-31.
 */
public class Trigger {

    public Gadget gadget;
    public int actionCode;

    public Trigger(Gadget gadget, int actionCode) {
        this.gadget = gadget;
        this.actionCode = actionCode;
    }
}
