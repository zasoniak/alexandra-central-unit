package com.kms.alexandracentralunit;

/**
 * Created by Mateusz Zasoński on 2014-10-14.
 */
public class Module {
    private int id;
    private String name;
    private String address;
    private int room;
    private int type;

    //database repository
    public ModuleRepository repository;


    public int getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public String getAddress() {
        return this.address;
    }

    public int getRoom() {
        return this.room;
    }

    public int getType() {
        return this.type;
    }

}
