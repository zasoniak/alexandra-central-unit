package com.kms.alexandra.data.model;


import java.util.List;
import java.util.UUID;


/**
 * common interface for scenes and actions
 *
 * @author Mateusz Zasoński
 * @version 0.1
 */
public interface SceneComponent {

    public void start(Controller controller);
    public List<SceneComponent> getComponents();
    public List<UUID> getGadgetsID();

}
