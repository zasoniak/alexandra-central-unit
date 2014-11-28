package com.kms.alexandracentralunit.data.model;


import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


/**
 * Created by Mateusz Zasoński on 2014-11-17.
 * MultiSocket - class for all devices with electric sockets like Wall Sockets, Extension Cords etc.
 */
public class MultiSocket extends Gadget implements Switchable {

    protected int socketNumber;
    protected List<Socket> channels = new ArrayList<Socket>();

    public MultiSocket(UUID id, String system, String roomId, String name, String address, GadgetType type, int socketNumber) {
        super(id, system, roomId, name, address, type);
        this.socketNumber = socketNumber;
        for(int i = 0; i < socketNumber; i++)
        {
            this.channels.add(new Socket());
        }
    }

    @Override
    public BaseAction prepare(ActionMessage actionMessage) {

        switch(ActionType.valueOf(actionMessage.action))
        {
            case SwitchAll:
                return new ActionSwitchAll(this.gatt, actionMessage.parameter);
            case SwitchChannelOne:
                return new ActionSwitchChannelOne(this.gatt, actionMessage.parameter);
            case SwitchChannelTwo:
                break;
            case SwitchChannelThree:
                break;
            case SwitchChannelFour:
                break;
            case SwitchChannelFive:
                break;
            case SwitchChannelSix:
                break;
            default:
                return null;
        }
        return super.prepare(actionMessage);
    }

    @Override
    public boolean isOn() {

        boolean state = false;
        for(Socket socket : channels)
        {
            state |= socket.isOn();
        }
        return state;
    }

    @Override
    public void setOn(boolean state) {
        Log.d("cale gniazdko", "setOn");
        for(Socket socket : channels)
        {
            socket.setOn(state);
        }
        notifyObservers("isOn", isOn());
    }

    public int getSocketNumber() {
        return socketNumber;
    }

    public void setChannelOn(int channel, boolean state) {
        Log.d("socket "+String.valueOf(channel), "ustawiono na: "+String.valueOf(state));
        channels.get(channel).setOn(state);
        notifyObservers("isOnChannel"+String.valueOf(channel), channels.get(channel).isOn());
    }

    public boolean getChannelOn(int channel) {
        return channels.get(channel).isOn();
    }

    public double getChannelPowerConsuption(int channel) {
        return channels.get(channel).getPowerConsumption();
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
