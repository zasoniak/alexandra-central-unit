package com.kms.alexandra.data.model;


import android.bluetooth.BluetoothGatt;
import android.content.Intent;

import com.kms.alexandra.centralunit.CoreService;
import com.kms.alexandra.centralunit.HistorianBroadcastReceiver;

import org.json.JSONObject;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;


/**
 * Created by Mateusz Zasoński on 2014-10-14.
 * Gadget - base class for all field devices in system
 * provides common fields, basic functionality and logging mechanism
 */
public abstract class Gadget extends Observable {

    //gadget types  -> to be defined as list with constant numbers and included in system documentation
    public static final String TYPE = "type";
    //gadget states
    public static final String STATE = "state";

    public static final String ID = "id";
    public static final String ROOM_ID = "roomId";
    public static final String NAME = "name";
    public static final String MAC_ADDRESS = "MAC";
    public static final String CHANNELS = "channels";
    public static final String INSTALLED = "installed";
    public static final String ICON = "icon";
    public static final String FIRMWARE = "firmware";

    protected GadgetType type;
    protected GadgetState state;
    protected BluetoothGatt gatt;
    protected UUID id;
    protected String roomId;
    protected Room room;
    protected String name;
    protected String MAC;
    protected int channels;
    protected boolean installed;
    protected int icon;
    protected int firmware;

    public Gadget() {
    }

    public Gadget(UUID id, String roomId, String name, String MAC, GadgetType type, int channels, boolean installed) {
        this.id = id;
        this.name = name;
        this.MAC = MAC;
        this.roomId = roomId;
        this.type = type;
        this.state = GadgetState.Offline;
        this.channels = channels;
        this.installed = installed;
    }

    public int getChannels() {
        return this.channels;
    }

    public abstract JSONObject toJSON();
    public abstract Map<String, Object> getCurrentState();

    public UUID getId() {
        return id;
    }

    public GadgetState getState() {
        return state;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMAC() {
        return MAC;
    }

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public GadgetType getType() {
        return type;
    }

    public boolean isInstalled() {
        return this.installed;
    }

    public void setInstalled(boolean isInstalled) {
        this.installed = isInstalled;
    }

    public String toString() {
        return this.name+": "+this.id.toString();
    }

    public void setup() {
        //TODO: communication setup
    }

    public void run(String action, HashMap<String, String> parameters) {

        Intent intent = new Intent(CoreService.getContext(), HistorianBroadcastReceiver.class);
        intent.putExtra(HistorianBroadcastReceiver.LOG_TYPE, HistorianBroadcastReceiver.LOG_ACTION);
        intent.putExtra(HistorianBroadcastReceiver.GADGET, this.id.toString());
        intent.putExtra(HistorianBroadcastReceiver.TIME, Calendar.getInstance().getTime().toString());
        intent.putExtra(HistorianBroadcastReceiver.ACTION, action);
        intent.putExtra(HistorianBroadcastReceiver.PARAMETERS, parameters.toString());
        CoreService.getContext().sendBroadcast(intent);

        Intent intent2 = new Intent(CoreService.getContext(), HistorianBroadcastReceiver.class);
        intent2.putExtra(HistorianBroadcastReceiver.LOG_TYPE, HistorianBroadcastReceiver.LOG_MEASUREMENT);
        intent2.putExtra(HistorianBroadcastReceiver.GADGET, this.id.toString());
        intent2.putExtra(HistorianBroadcastReceiver.TIME, Calendar.getInstance().getTime().toString());
        intent2.putExtra(HistorianBroadcastReceiver.TYPE, "temperatura");
        intent2.putExtra(HistorianBroadcastReceiver.VALUE, "30");
        intent2.putExtra(HistorianBroadcastReceiver.UNIT, "st.C");
        CoreService.getContext().sendBroadcast(intent2);

        Intent intent3 = new Intent(CoreService.getContext(), HistorianBroadcastReceiver.class);
        intent3.putExtra(HistorianBroadcastReceiver.LOG_TYPE, HistorianBroadcastReceiver.LOG_ERROR);
        intent3.putExtra(HistorianBroadcastReceiver.TIME, Calendar.getInstance().getTime().toString());
        intent3.putExtra(HistorianBroadcastReceiver.DESCRIPTION, "trololo, poszedl blad!");
        CoreService.getContext().sendBroadcast(intent3);

    }

    public abstract String[] getSupportedActions();

    public abstract BaseAction prepare(ActionMessage actionMessage);

    public static enum GadgetType {
        WallSocket,
        ExtensionCord,
        LightSwitch,
        Dimmer,
        RGBLight
    }

    public static enum GadgetState {
        OK,
        Offline,
        Error
    }

    public static enum ActionType {
        SwitchAll,
        SwitchChannelOne,
        SwitchChannelTwo,
        SwitchChannelThree,
        SwitchChannelFour,
        SwitchChannelFive,
        SwitchChannelSix
    }

}
