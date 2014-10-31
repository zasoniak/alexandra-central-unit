package com.kms.alexandracentralunit.model;


import java.util.List;


/**
 * Created by Mateusz Zasoński on 2014-10-31.
 */
public class Scene implements SceneComponent {

    public String name;
    public int offset;

    @Override
    public void start() {

    }

    @Override
    public List<SceneComponent> getComponents() {
        return null;
    }
}
