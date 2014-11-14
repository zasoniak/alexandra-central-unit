package com.kms.alexandracentralunit.data.model;


import android.content.Intent;

import com.kms.alexandracentralunit.CoreService;
import com.kms.alexandracentralunit.HistorianBroadcastReceiver;

import java.util.Calendar;
import java.util.HashMap;
import java.util.UUID;


/**
 * Created by Mateusz Zasoński on 2014-10-14.
 */
public class Gadget {

    private UUID id;
    private String system;
    private String roomId;
    private String name;
    private String address;
    private String type; //TODO: delete when subclasses created!!!

    public Gadget(UUID id, String system, String roomId, String name, String address, String type) {
        this.id = id;
        this.system = system;
        this.name = name;
        this.address = address;
        this.roomId = roomId;
        this.type = type;
    }

    public UUID getId() {
        return id;
    }

    public String getSystem() {
        return system;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public String getRoom() {
        return roomId;
    }

    public String getType() {
        return type;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public String toString() {
        return this.name+": "+this.id.toString();
    }

    public void setup() {
        //TODO: communication setup
    }

    public void run(HashMap<String, String> parameters) {

        Intent intent = new Intent(CoreService.getContext(), HistorianBroadcastReceiver.class);
        intent.putExtra(HistorianBroadcastReceiver.LOG_TYPE, HistorianBroadcastReceiver.LOG_ACTION);
        intent.putExtra(HistorianBroadcastReceiver.GADGET, this.id.toString());
        intent.putExtra(HistorianBroadcastReceiver.TIME, Calendar.getInstance().getTime().toString());
        intent.putExtra(HistorianBroadcastReceiver.ACTION, "jakas akcja");
        intent.putExtra(HistorianBroadcastReceiver.PARAMETERS, "jakies parametry");
        CoreService.getContext().sendBroadcast(intent);

        Intent intent2 = new Intent(CoreService.getContext(), HistorianBroadcastReceiver.class);
        intent2.putExtra(HistorianBroadcastReceiver.LOG_TYPE, HistorianBroadcastReceiver.LOG_MEASUREMENT);
        intent2.putExtra(HistorianBroadcastReceiver.GADGET, this.id.toString());
        intent2.putExtra(HistorianBroadcastReceiver.TIME, Calendar.getInstance().getTime().toString());
        intent2.putExtra(HistorianBroadcastReceiver.TYPE, "temperatura");
        intent2.putExtra(HistorianBroadcastReceiver.VALUE, "30");
        intent2.putExtra(HistorianBroadcastReceiver.UNIT, "st.C");
        CoreService.getContext().sendBroadcast(intent2);
        //        LocalBroadcastManager broadcastManager  = LocalBroadcastManager.getInstance(CoreService.getContext());
        //        broadcastManager.sendBroadcast(intent);
    }

}
