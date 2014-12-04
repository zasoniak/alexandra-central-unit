package com.kms.alexandracentralunit.data.model;


/**
 * @author Mateusz Zasoński
 */
public abstract class Sensor {

    protected double value;
    protected String unit;
    protected String name;

    public abstract void calculateValue(double rawValue);

    public double getValue() {
        return this.value;
    }

}
