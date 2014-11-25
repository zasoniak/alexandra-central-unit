package com.kms.alexandracentralunit.data.model;


import com.kms.alexandracentralunit.ControlService;

import java.util.HashMap;
import java.util.Map;
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

    private String sceneID;
    private UUID gadgetID;
    private Map<String, String> conditions;

    private GadgetObserver gadgetObserver;

    public Trigger(String scene, UUID gadget, HashMap<String, String> conditions) {
        this.sceneID = scene;
        this.gadgetID = gadget;
        this.conditions = conditions;
    }

    public String getScene() {
        return sceneID;
    }

    public UUID getGadget() {
        return gadgetID;
    }

    public void registerObserver(Gadget gadget) {
        this.gadgetObserver = new GadgetObserver(gadget);
        gadget.registerObserver(this.gadgetObserver);
    }

    /**
     * implementation of Observer interface
     * <p/>
     * observe changes in connected Gadget, compare with conditions and
     * run specified scene accordingly
     * Inner class for Trigger
     */
    class GadgetObserver implements Observer {

        Gadget gadget;

        GadgetObserver(Gadget gadget) {
            this.gadget = gadget;
        }

        @Override
        public void update(String field, Object value) {

            //TODO: przemyslec co w przypadku gdy chcemy kilka warunkow miec spelnionych
            //TODO: check conditions required
            if(conditions.containsKey(field) && conditions.get(field).equals(value))
            {
                ControlService.getInstance().run(sceneID);
            }
        }
    }
}
