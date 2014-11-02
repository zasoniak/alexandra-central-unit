package com.kms.alexandracentralunit.data.model;


import com.kms.alexandracentralunit.data.database.GadgetRepository;

import java.util.UUID;


/**
 * Created by Mateusz Zasoński on 2014-10-14.
 */
public class Gadget {

    //database repository
    public GadgetRepository repository;
    private UUID id;
    private String name;
    private String address;
    private UUID roomId;
    private int type;

    public Gadget() {
    }

    public Gadget(UUID id, String name, String address, UUID roomId, int type) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.roomId = roomId;
        this.type = type;
    }

    public Gadget(GadgetRepository repository, UUID id, String name, String address, UUID roomId, int type) {
        this.repository = repository;
        this.id = id;
        this.name = name;
        this.address = address;
        this.roomId = roomId;
        this.type = type;
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public UUID getRoom() {
        return roomId;
    }

    public int getType() {
        return type;
    }

    public void save() {
        this.repository.add(this);
    }
}
