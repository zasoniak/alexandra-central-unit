package com.kms.alexandra.centralunit;


import android.bluetooth.BluetoothGattCharacteristic;
import android.util.Log;

import com.kms.alexandra.data.model.Controller;
import com.kms.alexandra.data.model.actions.BaseAction;
import com.kms.alexandra.data.model.gadgets.Gadget;

import java.util.concurrent.DelayQueue;
import java.util.concurrent.TimeUnit;


/**
 * allows thread-safe BLE communication
 *
 * @author Mateusz Zasoński
 * @version 0.1
 */
public class BLEController implements Controller {

    public static final String TAG = "BLEController";
    private static final long MESSAGE_INTERVAL = 50;
    private DelayQueue<BaseAction> actionQueue = new DelayQueue<BaseAction>();

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
        Log.d(TAG, "zakolejkowano akcje "+action.getAction()+": "+action.getParameter()+", czas: "+String.valueOf(action.getDelay(TimeUnit.MILLISECONDS)));
    }

    private class BLEMessenger extends Thread {

        @Override
        public void run() {
            while(isRunning)
            {
                BaseAction action = actionQueue.poll();
                if(action != null)
                {
                    if(action.getGadget().getState().equals(Gadget.GadgetState.OK))
                    {
                        if(action.getGatt() != null)
                        {
                            Log.i(TAG, "rusza akcja: "+action.getService().toString());
                            BluetoothGattCharacteristic characteristic = action.getGatt().getService(action.getService()).getCharacteristic(action.getCharacteristic());
                            characteristic.setValue(Integer.parseInt(action.getParameter()), BluetoothGattCharacteristic.FORMAT_UINT8, 0);
                            action.getGatt().writeCharacteristic(characteristic);
                        }
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
