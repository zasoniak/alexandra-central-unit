package com.kms.alexandra.data;


import com.kms.alexandra.data.model.ActionMessage;
import com.kms.alexandra.data.model.BaseAction;
import com.kms.alexandra.data.model.Home;
import com.kms.alexandra.data.model.Scene;
import com.kms.alexandra.data.model.SceneComponent;
import com.kms.alexandra.data.model.Trigger;

import java.util.ArrayList;
import java.util.List;


/**
 * Builder for scene creation
 * Created by Mateusz Zasoński on 2014-10-31.
 *
 * @author Mateusz Zasoński
 * @version 0.1
 */
public class SceneBuilder {

    private String id;
    private String name;
    private Home home;

    private List<SceneComponent> children = new ArrayList<SceneComponent>();
    private List<Trigger> triggers = new ArrayList<Trigger>();

    public SceneBuilder(Home home) {
        this.home = home;

    }

    public void create(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public void create(String name) {
        this.id = "0";
        this.name = name;
    }

    public void addTriggers(List<Trigger> triggers) {
        for(Trigger trigger : triggers)
        {
            this.triggers.add(trigger);
        }
    }

    public void addTrigger(Trigger trigger) {
        this.triggers.add(trigger);
    }

    public void addActions(List<ActionMessage> actionMessages) {
        for(ActionMessage actionMessage : actionMessages)
        {
            if(this.home.getGadget(actionMessage.gadgetID) != null)
            {
                BaseAction action = this.home.getGadget(actionMessage.gadgetID).prepare(actionMessage);
                action.setDelay(actionMessage.delay);
                this.children.add(action);
            }
        }
    }

    public void addSubscenes(List<String> subsceneIDs) {
        for(String subsceneID : subsceneIDs)
        {
            if(this.home.getScene(subsceneID) != null)
            {
                this.children.add(this.home.getScene(subsceneID));
            }
        }
    }

    public Scene getScene() {
        return new Scene(this.id, this.name, this.triggers, this.children);
    }

}

