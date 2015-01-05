package com.kms.alexandra.data.model;


import java.util.UUID;


/**
 * @author Mateusz Zasoński
 */
public class TemperatureSensor extends Sensor {

    public static final UUID service = UUID.fromString("f000aa20-0451-4000-b000-000000000000");
    public static final UUID characteristic = UUID.fromString("f000aa20-0451-4000-b000-000000000000");

    public TemperatureSensor() {
        this.name = "Temperature";
        this.unit = "Celsius";
        this.value = 0;
    }

    @Override
    public void calculateValue(double rawValue) {
        //TODO: implement calculations
        this.value = rawValue;
    }
}
