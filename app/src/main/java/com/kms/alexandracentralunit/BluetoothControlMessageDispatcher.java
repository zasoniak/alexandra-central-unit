package com.kms.alexandracentralunit;


import android.bluetooth.BluetoothGatt;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Mateusz Zasoński on 2014-11-19.
 * BluetoothRemoteControlService - class responsible for remote control via BLE
 */
public class BluetoothControlMessageDispatcher extends ControlMessageDispatcher {

    private List<BluetoothGatt> controllers = new ArrayList<BluetoothGatt>();

    public BluetoothControlMessageDispatcher() {
    }
}
