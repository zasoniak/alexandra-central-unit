package com.kms.alexandracentralunit.data.model;


import java.util.List;


/**
 * Created by Mateusz Zasoński on 2014-10-31.
 */
public class Action implements SceneComponent {

    public int offset;
    private Gadget gadget;
    private String actionCode;

    public Action(Gadget gadget, String actionCode, int offset) {
        this.gadget = gadget;
        this.actionCode = actionCode;
        this.offset = offset;
    }

    @Override
    public void start() {

    }

    @Override
    public List<SceneComponent> getComponents() {
        return null;
    }

    public Gadget getGadget() {
        return gadget;
    }

    public String getActionCode() {
        return actionCode;
    }

}
