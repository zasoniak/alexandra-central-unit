package com.kms.alexandracentralunit.data.model;


import com.kms.alexandracentralunit.ControlService;

import java.util.HashMap;


/**
 * Created by Mateusz Zasoński on 2014-10-31.
 * Trigger - class providing scene startup at specified conditions
 * implements its own GadgetObserver
 */
public class Trigger {

    public String scene;
    public Gadget gadget;
    public String action;
    public HashMap<String, String> parameters;
    private GadgetObserver gadgetObserver;

    public Trigger(String scene, Gadget gadget, String action, HashMap<String, String> parameters) {
        this.scene = scene;
        this.gadget = gadget;
        this.action = action;
        this.parameters = parameters;
    }

    public String getScene() {
        return scene;
    }

    public Gadget getGadget() {
        return gadget;
    }

    public String getAction() {
        return action;
    }

    class GadgetObserver implements Observer {

        @Override
        public void update(String field, Object value) {
            if(field.equals("jol"))
            {
                ControlService.getInstance().run(scene);
            }
        }
    }
}
