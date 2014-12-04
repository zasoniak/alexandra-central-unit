package com.kms.alexandracentralunit.data.model;


import com.kms.alexandracentralunit.BLEController;

import java.util.List;


/**
 * common interface for scenes and actions
 *
 * @author Mateusz Zasoński
 * @version 0.1
 */
public interface SceneComponent {

    public void start(BLEController controller);
    public List<SceneComponent> getComponents();

}
