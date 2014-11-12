package com.kms.alexandracentralunit.data;


import com.kms.alexandracentralunit.data.model.Action;
import com.kms.alexandracentralunit.data.model.Scene;
import com.kms.alexandracentralunit.data.model.SceneComponent;
import com.kms.alexandracentralunit.data.model.Trigger;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Mateusz Zasoński on 2014-10-31.
 */
public class SceneBuilder {

    public String id;

    public String name;

    public List<SceneComponent> children = new ArrayList<SceneComponent>();
    public List<Trigger> triggers = new ArrayList<Trigger>();

    public SceneBuilder() {
    }

    public void create(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public void addTriggers(List<Trigger> triggers) {
        this.triggers = triggers;
    }

    public void addActions(List<Action> action) {
        this.children.addAll(action);
    }

    public void addSubscenes(List<Scene> subscenes) {
        this.children.addAll(subscenes);
    }

    public Scene getScene() {
        return new Scene(this.id, this.name, this.triggers, this.children);
    }

}

