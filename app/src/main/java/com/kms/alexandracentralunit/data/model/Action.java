package com.kms.alexandracentralunit.data.model;


import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;


/**
 * Created by Mateusz Zasoński on 2014-10-31.
 */
public class Action implements SceneComponent {

    public int offset;
    private UUID scene;
    private Gadget gadget;
    private String parameters;

    public Action(UUID scene, Gadget gadget, String parameters, int offset) {
        this.scene = scene;
        this.gadget = gadget;
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

    public String getParameters() {
        return parameters;
    }

    public UUID getScene() {
        return scene;
    }

    public int getOffset() {
        return offset;
    }

    public String toString() {
        return this.gadget.toString()+", akcja: "+this.parameters;
    }

}
