package com.kms.alexandracentralunit.data.model;


import java.util.List;


/**
 * Created by Mateusz Zasoński on 2014-10-16.
 */
public class Room {

    List<Gadget> gadgets;
    private long id;
    private long systemId;
    private String name;
    private int color;

    public Room(long id, long systemId, String name, int color, List<Gadget> gadgets) {
        this.id = id;
        this.systemId = systemId;
        this.name = name;
        this.gadgets = gadgets;
        this.color = color;
    }

    public long getSystemId() {
        return systemId;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public List<Gadget> getGadgets() {
        return gadgets;
    }

    public void setGadgets(List<Gadget> gadgets) {
        this.gadgets = gadgets;
    }
}
