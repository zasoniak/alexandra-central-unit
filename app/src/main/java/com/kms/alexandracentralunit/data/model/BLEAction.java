package com.kms.alexandracentralunit.data.model;


import android.bluetooth.BluetoothGatt;

import java.util.UUID;


/**
 * Created by Mateusz Zasoński on 2014-11-23.
 */
public interface BLEAction {

    public BluetoothGatt getGatt();
    public UUID getService();
    public UUID getCharacteristic();
    public String getParameter();
}
