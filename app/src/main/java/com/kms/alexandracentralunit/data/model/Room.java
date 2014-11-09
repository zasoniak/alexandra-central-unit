package com.kms.alexandracentralunit.data.model;


import java.util.List;
import java.util.UUID;


/**
 * Created by Mateusz Zasoński on 2014-10-16.
 */
public class Room {

    private UUID id;
    private long systemId;
    private String name;
    private int color;
    List<Gadget> gadgets;

    public Room(UUID id, long systemId, String name, int color, List<Gadget> gadgets) {
        this.id = id;
        this.systemId = systemId;
        this.name = name;
        this.gadgets = gadgets;
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

    public List<Gadget> getGadgets() {
        return gadgets;
    }
}
