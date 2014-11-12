package com.kms.alexandracentralunit.data.model;


import java.util.ArrayList;
import java.util.List;


/**
 * Created by Mateusz Zasoński on 2014-10-31.
 */
public class Scene implements SceneComponent {

    public String id;
    public String name;

    public List<SceneComponent> children;
    public List<Trigger> triggers;

    public Scene(String id, String name, int offset) {
        this.id = id;
        this.name = name;
    }

    public Scene(String id, String name, List<Trigger> triggers, List<SceneComponent> children) {
        this.id = id;
        this.name = name;
        this.triggers = triggers;
        this.children = children;
    }

    @Override
    public void start() {
        for(SceneComponent child : children)
        {
            child.start();
        }

    }

    @Override
    public List<SceneComponent> getComponents() {
        if(children != null)
        {
            return children;
        }
        return new ArrayList<SceneComponent>();
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
