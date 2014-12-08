package com.kms.alexandracentralunit.data.model;


import android.bluetooth.BluetoothGatt;

import java.util.UUID;


/**
 * Created by Mateusz Zasoński on 2014-11-23.
 * provides basic implementation of action
 */
public class ActionSwitchAll extends BaseAction {

    private static final UUID SERVICE = UUID.fromString("f000aa20-0451-4000-b000-000000000000");
    private static final UUID CHARACTERISTIC = UUID.fromString("f000aa20-0451-4000-b000-000000000000");

    public ActionSwitchAll(UUID gadgetID, BluetoothGatt gatt, String parameter) {
        this.gadgetID = gadgetID;
        this.gatt = gatt;
        this.action = "SwitchAll";
        this.parameter = parameter;
    }

    @Override
    public UUID getService() {
        return SERVICE;
    }

    @Override
    public UUID getCharacteristic() {
        return CHARACTERISTIC;
    }
}
