package com.kms.alexandracentralunit.data.model;


/**
 * Created by Mateusz Zasoński on 2014-11-17.
 */
public class Switch implements Switchable {

    public static final String IS_ON = "isOn";
    protected boolean on;

    public Switch() {
        this.on = false;
    }

    @Override
    public boolean isOn() {
        return this.on;
    }

    @Override
    public void setOn(boolean state) {
        this.on = state;
    }

}
