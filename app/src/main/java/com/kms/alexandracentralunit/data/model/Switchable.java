package com.kms.alexandracentralunit.data.model;


/**
 * basic interface for all devices with on/off state
 *
 * @author Mateusz Zasoński
 * @version 0.1
 */
public interface Switchable {

    public boolean isOn();
    public void setOn(boolean state);

}
