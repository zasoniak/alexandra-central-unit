package com.kms.alexandracentralunit.data.model;


import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;


/**
 * Created by Mateusz Zasoński on 2014-10-31.
 */
public class Action implements SceneComponent {

    public int offset;
    private String scene;
    private Gadget gadget;
    private String action;
    private HashMap<String, String> parameters;

    public Action(String scene, Gadget gadget, String action, HashMap<String, String> parameters, int offset) {
        this.scene = scene;
        this.gadget = gadget;
        this.action = action;
        this.parameters = parameters;
        this.offset = offset;
    }

    @Override
    public void start() {
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                gadget.run(parameters);
            }
        };
        Timer timer = new Timer();
        timer.schedule(task, offset);

    }

    @Override
    public List<SceneComponent> getComponents() {
        return null;
    }

    public Gadget getGadget() {
        return gadget;
    }

    public HashMap<String, String> getParameters() {
        return parameters;
    }

    public String getScene() {
        return scene;
    }

    public int getOffset() {
        return offset;
    }

    public String toString() {
        return this.gadget.toString()+", akcja: "+this.parameters;
    }

}
