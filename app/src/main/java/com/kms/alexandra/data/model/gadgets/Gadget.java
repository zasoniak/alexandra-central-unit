package com.kms.alexandra.data.model.gadgets;


import android.bluetooth.BluetoothGatt;

import com.kms.alexandra.data.model.Listable;
import com.kms.alexandra.data.model.Observable;
import com.kms.alexandra.data.model.Room;
import com.kms.alexandra.data.model.actions.ActionMessage;
import com.kms.alexandra.data.model.actions.BaseAction;

import org.json.JSONObject;

import java.util.Map;
import java.util.UUID;


/**
 * Created by Mateusz Zasoński on 2014-10-14.
 * Gadget - base class for all field devices in system
 * provides common fields, basic functionality and logging mechanism
 */
public abstract class Gadget extends Observable implements Listable {

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
    protected Room room;
    protected String temporaryRoomId;
    protected String name;
    protected String MAC;
    protected int channels;
    protected boolean installed;
    protected int icon;
    protected int firmware;

    public Gadget() {
    }

    public Gadget(UUID id, String temporaryRoomId, String name, String MAC, GadgetType type, int channels, boolean installed, int icon, int firmware) {
        this.id = id;
        this.name = name;
        this.MAC = MAC;
        this.temporaryRoomId = temporaryRoomId;
        this.type = type;
        this.state = GadgetState.Offline;
        this.channels = channels;
        this.installed = installed;
        this.icon = icon;
        this.firmware = firmware;
    }

    public Gadget(UUID id, Room room, String name, String MAC, GadgetType type, int channels, boolean installed, int icon, int firmware) {
        this.id = id;
        this.name = name;
        this.MAC = MAC;
        this.room = room;
        this.type = type;
        this.state = GadgetState.Offline;
        this.channels = channels;
        this.installed = installed;
        this.icon = icon;
        this.firmware = firmware;
    }

    public String getTemporaryRoomId() {
        return temporaryRoomId;
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

    public GadgetType getType() {
        return type;
    }

    public int getIcon() {
        return icon;
    }

    public int getFirmware() {
        return firmware;
    }

    public boolean isInstalled() {
        return this.installed;
    }

    public void setInstalled(boolean isInstalled) {
        this.installed = isInstalled;
    }

    public String toString() {
        return this.name+", "+this.room.getName();
    }

    public void setup() {
        //TODO: communication setup
    }

    public abstract String[] getSupportedActions();

    public abstract BaseAction prepare(ActionMessage actionMessage);

    public static enum GadgetType {
        WallSocket,
        ExtensionCord,
        LightSwitch,
        Dimmer,
        RGBController,
        OpenClosedSensor,
        WeatherStation
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
        SetBrightness,
        SetBrightnessChannelOne,
        SetBrightnessChannelTwo
    }

    @Override
    public String getObjectType() {
        return getType().name();
    }
}
