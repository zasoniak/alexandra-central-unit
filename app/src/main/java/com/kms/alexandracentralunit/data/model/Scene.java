package com.kms.alexandracentralunit.data.model;


import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;


/**
 * Created by Mateusz Zasoński on 2014-10-31.
 */
public class Scene implements SceneComponent {

    public long id;
    public String name;
    public int offset;

    public List<SceneComponent> children;
    public List<Trigger> triggers;

    public Scene(long id, long system, String name, int offset) {
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

    public long getId() {
        return id;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public List<SceneComponent> getChildren() {
        return children;
    }

    public void setChildren(List<SceneComponent> children) {
        this.children = children;
    }

    public List<Trigger> getTriggers() {
        return triggers;
    }

    public void setTriggers(List<Trigger> triggers) {
        this.triggers = triggers;
    }

    public void checkTriggers(Gadget gadget, String action) {
        for(Trigger trigger : triggers)
        {
            if(trigger.gadget.equals(gadget) && trigger.getAction().equals(action))
            {
                this.start();
            }
        }
    }
}
