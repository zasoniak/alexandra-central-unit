package com.kms.alexandracentralunit.data.model;


/**
 * Created by Mateusz Zasoński on 2014-11-17.
 */
public class Socket extends Switch {

    public static final String POWER_CONSUMPTION = "powerConsumption";

    protected double powerConsuption;

    public Socket() {
        this.powerConsuption = 0;
    }

    public double getPowerConsuption() {
        return powerConsuption;
    }

}
