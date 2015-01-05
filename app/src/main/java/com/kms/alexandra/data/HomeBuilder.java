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

    public void create(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public void addGadgets(List<Gadget> gadgets) {
        this.gadgets = (ArrayList<Gadget>) gadgets;
    }

    public void addRooms(List<Room> rooms) {
        this.rooms = (ArrayList<Room>) rooms;
    }

    public void addScenes(List<Scene> scenes) {
        this.scenes = (ArrayList<Scene>) scenes;
    }

    public void addUsers(List<User> users) {
        this.users = (ArrayList<User>) users;
    }

    public void addSchedule(List<ScheduledScene> schedule) {
        this.schedule = (ArrayList<ScheduledScene>) schedule;
    }

    public Home getHome() {
        return new Home(this.id, this.name, this.gadgets, this.scenes, this.rooms, this.schedule, this.users);
    }

}
