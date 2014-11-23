package com.kms.alexandracentralunit.data.model;


import com.kms.alexandracentralunit.BLEController;

import java.util.List;


/**
 * Created by Mateusz Zasoński on 2014-10-31.
 */
public interface SceneComponent {

    public void start(BLEController controller);
    public List<SceneComponent> getComponents();

}
