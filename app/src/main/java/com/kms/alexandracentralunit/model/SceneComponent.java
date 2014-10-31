package com.kms.alexandracentralunit.model;


import java.util.List;


/**
 * Created by Mateusz Zasoński on 2014-10-31.
 */
public interface SceneComponent {

    public void start();
    public List<SceneComponent> getComponents();

}
