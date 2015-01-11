package com.kms.alexandra.data.model.Actions;


import android.bluetooth.BluetoothGatt;

import java.util.UUID;


/**
 * @author Mateusz Zasoński
 */
public class ActionSetBrightnessChannelTwo extends BaseAction {

    private static final UUID SERVICE = UUID.fromString("f000aa20-0451-4000-b000-111111111111");
    private static final UUID CHARACTERISTIC = UUID.fromString("f000aa20-0451-4000-b000-000000000000");

    public ActionSetBrightnessChannelTwo(UUID gadgetID, BluetoothGatt gatt, String parameter) {
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
