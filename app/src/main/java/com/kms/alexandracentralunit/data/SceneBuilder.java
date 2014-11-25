package com.kms.alexandracentralunit.data;


import com.kms.alexandracentralunit.CoreService;
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

    private List<SceneComponent> children = new ArrayList<SceneComponent>();
    private List<Trigger> triggers = new ArrayList<Trigger>();

    public SceneBuilder() {
    }

    public void create(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public void addTriggers(List<Trigger> triggers) {
        Home home = CoreService.getHome();
        for(Trigger trigger : triggers)
        {
            trigger.registerObserver(home.getGadget(trigger.getGadget()));
            this.triggers.add(trigger);
        }
    }

    public void addActions(List<ActionMessage> actionMessages) {
        Home home = CoreService.getHome();
        for(ActionMessage actionMessage : actionMessages)
        {
            BaseAction action = home.getGadget(actionMessage.gadgetID).prepare(actionMessage);
            action.setDelay(actionMessage.delay);
            this.children.add(action);
        }
    }

    public void addSubscenes(List<String> subsceneIDs) {

        Home home = CoreService.getHome();
        for(String subsceneID : subsceneIDs)
        {
            if(home.getScene(subsceneID) != null)
            {
                this.children.add(home.getScene(subsceneID));
            }
        }
    }

    public Scene getScene() {
        return new Scene(this.id, this.name, this.triggers, this.children);
    }

}

