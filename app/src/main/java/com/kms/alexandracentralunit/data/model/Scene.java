package com.kms.alexandracentralunit.data.model;


import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;


/**
 * Created by Mateusz Zasoński on 2014-10-31.
 */
public class Scene implements SceneComponent {

    public UUID id;
    public String name;
    public int offset;

    public List<SceneComponent> children;
    public List<Trigger> triggers;

    public Scene(UUID id, String name, int offset) {
        this.id = id;
        this.name = name;
        this.offset = offset;
    }

    @Override
    public void start() {

        //TODO: check if that work

        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                for(SceneComponent child : children)
                {
                    child.start();
                }
            }
        };
        Timer timer = new Timer();
        timer.schedule(task, offset);
    }

    @Override
    public List<SceneComponent> getComponents() {
        if(children != null)
        {
            return children;
        }
        return new ArrayList<SceneComponent>();
    }

    public void checkTriggers(Gadget gadget, int actionCode) {
        for(Trigger trigger : triggers)
        {
            if(trigger.gadget.equals(gadget) && trigger.actionCode == actionCode)
            {
                this.start();
            }
        }
    }
}
