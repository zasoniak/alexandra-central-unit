package com.kms.alexandra.data.model;


import com.kms.alexandra.centralunit.BLEController;

import java.util.List;
import java.util.UUID;


/**
 * common interface for scenes and actions
 *
 * @author Mateusz Zasoński
 * @version 0.1
 */
public interface SceneComponent {

    public void start(BLEController controller);
    public List<SceneComponent> getComponents();
    public List<UUID> getGadgetsID();

}
