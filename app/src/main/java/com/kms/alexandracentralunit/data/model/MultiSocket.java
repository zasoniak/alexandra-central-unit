package com.kms.alexandracentralunit.data.model;


import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


/**
 * Created by Mateusz Zasoński on 2014-11-17.
 */
public class MultiSocket extends Gadget implements Switchable {

    protected int socketNumber;
    protected List<Socket> sockets = new ArrayList<Socket>();

    public MultiSocket(UUID id, String system, String roomId, String name, String address, GadgetType type, int socketNumber) {
        super(id, system, roomId, name, address, type);
        this.socketNumber = socketNumber;
        for(int i = 0; i < socketNumber; i++)
        {
            this.sockets.add(new Socket());
        }
    }

    @Override
    public boolean isOn() {

        boolean state = false;
        for(Socket socket : sockets)
        {
            state |= socket.isOn();
        }
        return state;
    }

    @Override
    public void setOn(boolean state) {
        Log.d("cale gniazdko", "setOn");
        for(Socket socket : sockets)
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
        sockets.get(channel).setOn(state);
        notifyObservers("isOnChannel"+String.valueOf(channel), sockets.get(channel).isOn());
    }

    public boolean getChannelOn(int channel) {
        return sockets.get(channel).isOn();
    }

    public double getChannelPowerConsuption(int channel) {
        return sockets.get(channel).getPowerConsuption();
    }
}
