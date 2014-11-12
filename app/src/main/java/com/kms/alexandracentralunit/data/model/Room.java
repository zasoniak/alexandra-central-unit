package com.kms.alexandracentralunit.data.model;


import java.util.List;


/**
 * Created by Mateusz Zasoński on 2014-10-16.
 */
public class Room {

    final private String id;
    List<Gadget> gadgets;
    private String name;
    private int color;

    public Room(String id, String name, int color, List<Gadget> gadgets) {
        this.id = id;
        this.name = name;
        this.gadgets = gadgets;
        this.color = color;
    }

    public String getId() {
        return id;
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
