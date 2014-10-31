package com.kms.alexandracentralunit.Models;


import com.kms.alexandracentralunit.Repositories.GadgetRepository;


/**
 * Created by Mateusz Zasoński on 2014-10-14.
 */
public class Gadget {

    //database repository
    public GadgetRepository repository;
    private long id;
    private String name;
    private String address;
    private long roomId;
    private int type;

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
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
}
