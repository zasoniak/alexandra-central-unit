package com.kms.alexandra.data.model.actions;


import android.bluetooth.BluetoothGatt;

import com.kms.alexandra.data.model.Controller;
import com.kms.alexandra.data.model.SceneComponent;
import com.kms.alexandra.data.model.gadgets.Gadget;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
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

    public static final String GADGET_ID = "gadget";
    public static final String ACTION = "action";
    public static final String PARAMETER = "parameter";
    public static final String DELAY = "delay";
    protected Gadget gadget;

    protected String action;
    protected BluetoothGatt gatt;
    protected String parameter;
    protected long delay = 0;
    protected long submissionTime = 0;
    protected UUID service;
    protected UUID characteristic;

    public BaseAction() {

    }

    public BaseAction(Gadget gadget, String action, BluetoothGatt gatt, String parameter, long delay, UUID service, UUID characteristic) {
        this.gadget = gadget;
        this.action = action;
        this.gatt = gatt;
        this.parameter = parameter;
        this.delay = delay;
        this.service = service;
        this.characteristic = characteristic;
    }

    public void setSubmissionTime(long submissionTime) {
        this.submissionTime = submissionTime;
    }

    @Override
    public BluetoothGatt getGatt() {
        return this.gatt;
    }

    @Override
    public UUID getService() {
        return this.service;
    }

    @Override
    public UUID getCharacteristic() {
        return this.characteristic;
    }

    @Override
    public String getParameter() {
        return this.parameter;
    }

    public void setParameter(String parameter) {
        this.parameter = parameter;
    }

    public void setGatt(BluetoothGatt gatt) {
        this.gatt = gatt;
    }

    @Override
    public void start(Controller controller) {
        controller.queue(this);
    }

    @Override
    public List<SceneComponent> getComponents() {
        return null;
    }

    @Override
    public List<UUID> getGadgetsID() {
        ArrayList<UUID> ids = new ArrayList<UUID>();
        ids.add(gadget.getId());
        return ids;
    }

    public Gadget getGadget() {
        return this.gadget;
    }

    public void setGadget(Gadget gadget) {
        this.gadget = gadget;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public long getDelay() {
        return delay;
    }

    public void setDelay(long delay) {
        this.delay = delay;
    }

    public UUID getGadgetID() {
        return gadget.getId();
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

    public JSONObject toJSONObject() {
        JSONObject result = new JSONObject();
        try
        {
            result.put(BaseAction.GADGET_ID, this.gadget.getId().toString());
            result.put(BaseAction.ACTION, this.action);
            result.put(BaseAction.DELAY, this.delay);
            result.put(BaseAction.PARAMETER, this.parameter);
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
        return result;
    }
}
