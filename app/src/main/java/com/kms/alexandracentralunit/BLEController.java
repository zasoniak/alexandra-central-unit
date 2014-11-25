package com.kms.alexandracentralunit;


import android.bluetooth.BluetoothGattCharacteristic;

import com.kms.alexandracentralunit.data.model.BaseAction;

import java.util.concurrent.DelayQueue;


/**
 * Created by Mateusz Zasoński on 2014-11-23.
 * BLEController - class allowing safe-thread
 */
public class BLEController {

    private static final long MESSAGE_INTERVAL = 50;
    private DelayQueue<BaseAction> actionQueue = new DelayQueue<BaseAction>();

    //TODO: sprawdzic czy nie wyrzuca go w międzyczasie z jakis względów
    private volatile boolean isRunning;

    public BLEController() {
        BLEMessenger messenger = new BLEMessenger();
        messenger.setDaemon(true);
        messenger.start();
        isRunning = true;

    }

    public synchronized void queue(BaseAction action) {
        actionQueue.put(action);
    }

    class BLEMessenger extends Thread {

        @Override
        public void run() {
            while(isRunning)
            {
                BaseAction action = actionQueue.poll();
                if(action != null)
                {
                    BluetoothGattCharacteristic characteristic = action.getGatt().getService(action.getService()).getCharacteristic(action.getCharacteristic());
                    characteristic.setValue(action.getParameter());
                    action.getGatt().writeCharacteristic(characteristic);
                }
                try
                {
                    Thread.sleep(MESSAGE_INTERVAL);
                }
                catch (InterruptedException e)
                {
                    e.printStackTrace();
                }
            }
        }
    }

}
