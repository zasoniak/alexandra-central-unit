package com.kms.alexandra.centralunit;


import android.bluetooth.BluetoothGattCharacteristic;
import android.util.Log;

import com.kms.alexandra.data.model.BaseAction;
import com.kms.alexandra.data.model.Controller;

import java.util.concurrent.DelayQueue;
import java.util.concurrent.TimeUnit;


/**
 * allows thread-safe BLE communication
 *
 * @author Mateusz Zasoński
 * @version 0.1
 */
public class BLEController implements Controller {

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

    @Override
    public synchronized void queue(BaseAction action) {
        action.setSubmissionTime(System.currentTimeMillis());
        actionQueue.put(action);
        long delay = action.getDelay(TimeUnit.MILLISECONDS);
        Log.d("BLEController", "zakolejkowano akcje "+action.getParameter()+" czas: "+String.valueOf(action.getDelay(TimeUnit.MILLISECONDS)));
    }

    private class BLEMessenger extends Thread {

        @Override
        public void run() {
            while(isRunning)
            {
                BaseAction action = actionQueue.poll();
                if(action != null)
                {
                    Log.d("BLEController, akcja: ", action.getService().toString());
                    if(action.getGatt() != null)
                    {
                        BluetoothGattCharacteristic characteristic = action.getGatt().getService(action.getService()).getCharacteristic(action.getCharacteristic());
                        characteristic.setValue(action.getParameter());
                        action.getGatt().writeCharacteristic(characteristic);
                    }
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
