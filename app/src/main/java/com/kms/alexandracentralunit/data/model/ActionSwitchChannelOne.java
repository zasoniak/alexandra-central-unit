package com.kms.alexandracentralunit.data.model;


import android.bluetooth.BluetoothGatt;

import java.util.UUID;


/**
 * action switching channel one
 *
 * @author Mateusz Zasoński
 * @version 0.1
 */
public class ActionSwitchChannelOne extends BaseAction {

    private static final UUID SERVICE = UUID.fromString("f000aa20-0451-4000-b000-111111111111");
    private static final UUID CHARACTERISTIC = UUID.fromString("f000aa20-0451-4000-b000-000000000000");

    public ActionSwitchChannelOne(UUID gadgetID, BluetoothGatt gatt, String parameter) {
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
