package com.kms.alexandra.data;


import com.kms.alexandra.data.model.Gadget;
import com.kms.alexandra.data.model.Home;
import com.kms.alexandra.data.model.Room;
import com.kms.alexandra.data.model.Scene;
import com.kms.alexandra.data.model.ScheduledScene;
import com.kms.alexandra.data.model.User;

import java.util.ArrayList;
import java.util.List;


/**
 * builder for entire home ecosystem object
 *
 * @author Mateusz Zasoński
 * @version 0.1
 */
public class HomeBuilder {

    private String id;
    private String name;

    private ArrayList<Gadget> gadgets = new ArrayList<Gadget>();
    private ArrayList<Scene> scenes = new ArrayList<Scene>();
    private ArrayList<Room> rooms = new ArrayList<Room>();
    private ArrayList<ScheduledScene> schedule = new ArrayList<ScheduledScene>();
    private ArrayList<User> users = new ArrayList<User>();

    public HomeBuilder() {
    }

    public HomeBuilder create(String id, String name) {
        this.id = id;
        this.name = name;
        return this;
    }

    public HomeBuilder addGadgets(List<Gadget> gadgets) {
        this.gadgets = (ArrayList<Gadget>) gadgets;
        return this;
    }

    public HomeBuilder addRooms(List<Room> rooms) {
        this.rooms = (ArrayList<Room>) rooms;
        return this;
    }

    public HomeBuilder addScenes(List<Scene> scenes) {
        this.scenes = (ArrayList<Scene>) scenes;
        return this;
    }

    public HomeBuilder addUsers(List<User> users) {
        this.users = (ArrayList<User>) users;
        return this;
    }

    public HomeBuilder addSchedule(List<ScheduledScene> schedule) {
        this.schedule = (ArrayList<ScheduledScene>) schedule;
        return this;
    }

    public Home getHome() {
        Home home = new Home(this.id, this.name, this.gadgets, this.scenes, this.rooms, this.schedule, this.users);

        for(Gadget gadget : home.getGadgets())
        {
            if(home.getRoom(gadget.getTemporaryRoomId()) != null)
            {
                gadget.setRoom(home.getRoom(gadget.getTemporaryRoomId()));
            }
        }

        return home;
    }

}
