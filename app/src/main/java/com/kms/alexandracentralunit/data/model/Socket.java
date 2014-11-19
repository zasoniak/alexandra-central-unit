package com.kms.alexandracentralunit.data.model;


/**
 * Created by Mateusz Zasoński on 2014-11-17.
 * Socket - base for single electric socket
 */
public class Socket extends Switch {

    public static final String POWER_CONSUMPTION = "powerConsumption";

    protected double powerConsumption;

    public Socket() {
        this.powerConsumption = 0;
    }

    public double getPowerConsumption() {
        return powerConsumption;
    }

}
