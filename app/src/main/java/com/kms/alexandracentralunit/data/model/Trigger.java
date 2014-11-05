package com.kms.alexandracentralunit.data.model;


import java.util.UUID;


/**
 * Created by Mateusz Zasoński on 2014-10-31.
 */
public class Trigger {

    public UUID scene;
    public Gadget gadget;
    public String action;

    public Trigger(UUID scene, Gadget gadget, String action) {
        this.scene = scene;
        this.gadget = gadget;
        this.action = action;
    }

    public UUID getScene() {
        return scene;
    }

    public Gadget getGadget() {
        return gadget;
    }

    public String getAction() {
        return action;
    }
}
