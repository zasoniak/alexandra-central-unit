package com.kms.alexandracentralunit.data;


import com.kms.alexandracentralunit.data.model.ActionMessage;
import com.kms.alexandracentralunit.data.model.BaseAction;
import com.kms.alexandracentralunit.data.model.Home;
import com.kms.alexandracentralunit.data.model.Scene;
import com.kms.alexandracentralunit.data.model.SceneComponent;
import com.kms.alexandracentralunit.data.model.Trigger;

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

    public void addTriggers(List<Trigger> triggers) {
        for(Trigger trigger : triggers)
        {
            trigger.registerObservers(this.home);
            this.triggers.add(trigger);
        }
    }

    public void addActions(List<ActionMessage> actionMessages) {
        for(ActionMessage actionMessage : actionMessages)
        {
            BaseAction action = this.home.getGadget(actionMessage.gadgetID).prepare(actionMessage);
            action.setDelay(actionMessage.delay);
            this.children.add(action);
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

