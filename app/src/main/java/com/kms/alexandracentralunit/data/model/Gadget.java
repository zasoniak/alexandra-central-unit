package com.kms.alexandracentralunit.data.model;


import java.util.UUID;


/**
 * Created by Mateusz Zasoński on 2014-10-14.
 */
public class Gadget {

    private UUID id;
    private long system;
    private long roomId;
    private String name;
    private String address;
    private int type;

    public Gadget(UUID id, long system, long roomId, String name, String address, int type) {
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

    public long getSystem() {
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

    public long getRoom() {
        return roomId;
    }

    public int getType() {
        return type;
    }

    public void setRoomId(long roomId) {
        this.roomId = roomId;
    }

    public String toString() {
        return this.name+": "+this.id.toString();
    }

    public void setup() {
        //TODO: communication setup
    }

    public void run(String parameters) {

    }

}
