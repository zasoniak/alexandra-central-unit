package com.kms.alexandracentralunit.data.model;


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

    }

}
