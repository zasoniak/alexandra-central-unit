package com.kms.alexandra.data.model;


import android.bluetooth.BluetoothGatt;

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
    protected UUID gadgetID;

    protected String action;
    protected BluetoothGatt gatt;
    protected String parameter;
    protected long delay = 0;
    protected long submissionTime = 0;

    public void setSubmissionTime(long submissionTime) {
        this.submissionTime = submissionTime;
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
        ids.add(gadgetID);
        return ids;
    }

    public String getAction() {
        return action;
    }

    public long getDelay() {
        return delay;
    }

    public void setDelay(long delay) {
        this.delay = delay;
    }

    public UUID getGadgetID() {
        return gadgetID;
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
            result.put(BaseAction.GADGET_ID, this.gadgetID.toString());
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
