package com.kms.alexandracentralunit.data.model;


import com.kms.alexandracentralunit.Control;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


/**
 * Checking fire conditions for specified scene
 * <p/>
 * Provides mechanism for monitoring Gadget parameters and decides
 * whether the scene ought to be executed
 * Includes internal implementation of Observer for Gadget object
 *
 * @author Mateusz Zasoński
 * @version 0.1
 */
public class Trigger {

    public static final String SCENE = "scene";
    public static final String CONDITIONS = "conditions";
    public static final String CONDITION_GADGET = "gadget";
    public static final String CONDITION_PARAMETER = "parameter";
    public static final String CONDITION_VALUE = "value";

    private String sceneID;

    private List<GadgetObserver> gadgetObservers = new ArrayList<GadgetObserver>();

    public Trigger(String scene) {
        this.sceneID = scene;
    }

    public String getScene() {
        return sceneID;
    }

    public void addObserver(UUID gadgetID, String parameter, String value) {
        this.gadgetObservers.add(new GadgetObserver(gadgetID, parameter, value));
    }

    public void registerObservers(Home home) {
        for(GadgetObserver observer : gadgetObservers)
        {
            if(home.getGadget(observer.gadgetID) != null)
            {
                home.getGadget(observer.gadgetID).registerObserver(observer);
            }
        }
    }

    private void checkConditions() {
        boolean flag = true;
        for(GadgetObserver observer : gadgetObservers)
        {
            flag &= observer.getFlag();
        }

        if(flag)
        {
            Control.getInstance().run(sceneID);
        }
    }

    /**
     * implementation of Observer interface
     * <p/>
     * observe changes in connected Gadget, compare with conditions and
     * run specified scene accordingly
     * Inner class for Trigger
     */
    class GadgetObserver implements Observer {

        public final UUID gadgetID;
        public String parameter;
        public String value;
        private boolean flag = false;

        GadgetObserver(UUID gadgetID, String parameter, String value) {
            this.gadgetID = gadgetID;
            this.parameter = parameter;
            this.value = value;
        }

        @Override
        public void update(String field, Object value) {
            if(this.parameter.equals(field) && this.value.equals(value))
            {
                flag = true;
                checkConditions();
            }
            else
            {
                flag = false;
            }
        }

        public boolean getFlag() {
            return flag;
        }
    }
}
