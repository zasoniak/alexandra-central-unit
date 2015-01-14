package com.kms.alexandra.data.model.actions;


import android.bluetooth.BluetoothGatt;

import java.util.UUID;


/**
 * @author Mateusz Zasoński
 */
public class ActionSetBrightnessChannelOne extends BaseAction {

    private static final UUID SERVICE = UUID.fromString("4146d76c-99fa-11e4-89d3-123b93f75cba");
    private static final UUID CHARACTERISTIC = UUID.fromString("4146db18-99fa-11e4-89d3-123b93f75cba");

    public ActionSetBrightnessChannelOne(UUID gadgetID, BluetoothGatt gatt, String parameter) {
        this.gadgetID = gadgetID;
        this.gatt = gatt;
        this.action = "SwitchChannelOne";
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
