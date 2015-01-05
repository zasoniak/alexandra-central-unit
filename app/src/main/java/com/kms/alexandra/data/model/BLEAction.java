package com.kms.alexandra.data.model;


import android.bluetooth.BluetoothGatt;

import java.util.UUID;


/**
 * interface for actions performed via BLE technology
 *
 * @author Mateusz Zasoński
 * @version 0.1
 */
public interface BLEAction {

    public BluetoothGatt getGatt();
    public UUID getService();
    public UUID getCharacteristic();
    public String getParameter();
}
