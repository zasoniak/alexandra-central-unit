package com.kms.alexandracentralunit.data.model;


import java.util.ArrayList;
import java.util.UUID;


/**
 * Created by Mateusz Zasoński on 2014-11-11.
 */
public class Home {

    private final String id;
    private String name;

    private ArrayList<Gadget> gadgets = new ArrayList<Gadget>();
    private ArrayList<Scene> scenes = new ArrayList<Scene>();
    private ArrayList<Room> rooms = new ArrayList<Room>();
    private ArrayList<ScheduledScene> schedule = new ArrayList<ScheduledScene>();
    private ArrayList<User> users = new ArrayList<User>();

    public Home(String id, String name, ArrayList<Gadget> gadgets, ArrayList<Scene> scenes, ArrayList<Room> rooms, ArrayList<ScheduledScene> schedule, ArrayList<User> users) {
        this.id = id;
        this.name = name;
        this.gadgets = gadgets;
        this.scenes = scenes;
        this.rooms = rooms;
        this.schedule = schedule;
        this.users = users;
    }

    public ArrayList<Gadget> getGadgets() {
        return gadgets;
    }

    public ArrayList<Scene> getScenes() {
        return scenes;
    }

    public ArrayList<Room> getRooms() {
        return rooms;
    }

    public ArrayList<ScheduledScene> getSchedule() {
        return schedule;
    }

    public ArrayList<User> getUsers() {
        return users;
    }

    public Gadget getGadget(UUID id) {
        for(Gadget gadget : this.gadgets)
        {
            if(gadget.getId().equals(id))
            {
                return gadget;
            }
        }
        return null;
    }

    public Scene getScene(String id) {
        for(Scene scene : this.scenes)
        {
            if(scene.getId().equals(id))
            return scene;
        }
        return null;
    }

    public Room getRoom(String id) {
        for(Room room : this.rooms)
        {
            if(room.getId().equals(id))
            return room;
        }
        return null;
    }

    public ScheduledScene getScheduledScene(String id) {
        for(ScheduledScene scheduledScene : this.schedule)
        {
            if(scheduledScene.getId().equals(id))
            return scheduledScene;
        }
        return null;
    }

    public User getUser(String id) {
        for(User user : this.users)
        {
            if(user.getId().equals(id))
            return user;
        }
        return null;
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
}
