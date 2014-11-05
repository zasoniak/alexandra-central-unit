package com.kms.alexandracentralunit.data.model;


import java.util.List;
import java.util.UUID;


/**
 * Created by Mateusz Zasoński on 2014-10-16.
 */
public class Room {

    List<Gadget> gadgets;
    private long systemId;
    private UUID id;
    private String name;
    private int color;

    public Room(List<Gadget> gadgets, long systemId, UUID id, String name, int color) {
        this.gadgets = gadgets;
        this.systemId = systemId;
        this.id = id;
        this.name = name;
        this.color = color;
    }

    public long getSystemId() {
        return systemId;
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getColor() {
        return color;
    }
}
