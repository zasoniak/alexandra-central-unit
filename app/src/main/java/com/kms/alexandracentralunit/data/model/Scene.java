package com.kms.alexandracentralunit.data.model;


import java.util.ArrayList;
import java.util.List;
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

}
