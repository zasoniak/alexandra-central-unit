package com.kms.alexandracentralunit.data.model;


import android.bluetooth.BluetoothGatt;

import com.kms.alexandracentralunit.BLEController;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;


/**
 * Base action implementation providing basic functionality and interfaces coverage
 * To be extended for device-specific actions
 *
 * @author Mateusz Zasoński
 * @version 0.1
 */
public class BaseAction implements SceneComponent, BLEAction, Delayed {

    protected BluetoothGatt gatt;
    protected String parameter;
    protected long delay = 0;
    protected long submissionTime = 0;

    public void setSubmissionTime(long submissionTime) {
        this.submissionTime = submissionTime;
    }

    public void setDelay(long delay) {
        this.delay = delay;
    }

    @Override
    public BluetoothGatt getGatt() {
        return this.gatt;
    }

    @Override
    public UUID getService() {
        return null;
    }

    @Override
    public UUID getCharacteristic() {
        return null;
    }

    @Override
    public String getParameter() {
        return this.parameter;
    }

    @Override
    public void start(BLEController controller) {
        controller.queue(this);
    }

    @Override
    public List<SceneComponent> getComponents() {
        return null;
    }

    @Override
    public long getDelay(TimeUnit timeUnit) {
        return timeUnit.convert(delay-(System.currentTimeMillis()-submissionTime), TimeUnit.MILLISECONDS);
    }

    @Override
    public int compareTo(Delayed delayed) {
        if(delayed == this)
        {
            return 0;
        }
        return Long.valueOf(getDelay(TimeUnit.MILLISECONDS)).compareTo(delayed.getDelay(TimeUnit.MILLISECONDS));

    }
}
