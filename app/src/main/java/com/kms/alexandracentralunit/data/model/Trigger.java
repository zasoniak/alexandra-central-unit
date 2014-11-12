package com.kms.alexandracentralunit.data.model;


import java.util.HashMap;
import java.util.UUID;


/**
 * Created by Mateusz Zasoński on 2014-10-31.
 */
public class Trigger {

    public String scene;
    public UUID gadget;
    public String action;
    public HashMap<String, String> parameters;

    public Trigger(String scene, UUID gadget, String action, HashMap<String, String> parameters) {
        this.scene = scene;
        this.gadget = gadget;
        this.action = action;
        this.parameters = parameters;
    }

    public String getScene() {
        return scene;
    }

    public UUID getGadget() {
        return gadget;
    }

    public String getAction() {
        return action;
    }
}
