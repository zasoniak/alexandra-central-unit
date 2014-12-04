package com.kms.alexandracentralunit.data.model;


import java.util.List;
import java.util.UUID;


/**
 * Describes single room and maintain list of gadgets located inside the room
 *
 * @author Mateusz Zasoński
 * @version 0.1
 */
public class Room {

    public static final String ID = "id";
    public static final String GADGETS = "gadgets";
    public static final String NAME = "name";
    public static final String COLOR = "color";
    final private String id;
    List<UUID> gadgets;
    private String name;
    private int color;

    public Room(String id, String name, int color, List<UUID> gadgets) {
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

    public List<UUID> getGadgets() {
        return gadgets;
    }

    public void setGadgets(List<UUID> gadgets) {
        this.gadgets = gadgets;
    }

    public void addGadget(UUID gadget) {
        this.gadgets.add(gadget);
    }
}
