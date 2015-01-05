package com.kms.alexandracentralunit.data.database.json;


import android.content.Context;
import android.util.Log;

import com.kms.alexandracentralunit.data.HomeBuilder;
import com.kms.alexandracentralunit.data.database.GadgetRepository;
import com.kms.alexandracentralunit.data.database.HomeRepository;
import com.kms.alexandracentralunit.data.database.RoomRepository;
import com.kms.alexandracentralunit.data.database.SceneRepository;
import com.kms.alexandracentralunit.data.database.ScheduleRepository;
import com.kms.alexandracentralunit.data.database.UserRepository;
import com.kms.alexandracentralunit.data.model.Gadget;
import com.kms.alexandracentralunit.data.model.Home;
import com.kms.alexandracentralunit.data.model.Room;
import com.kms.alexandracentralunit.data.model.Scene;
import com.kms.alexandracentralunit.data.model.ScheduledScene;
import com.kms.alexandracentralunit.data.model.User;

import java.util.ArrayList;


/**
 * Created by Mateusz Zasoński on 2014-11-11.
 */
public class JSONHomeRepository implements HomeRepository {

    private RoomRepository roomRepository;
    private GadgetRepository gadgetRepository;
    private SceneRepository sceneRepository;
    private ScheduleRepository scheduleRepository;
    private UserRepository userRepository;

    public JSONHomeRepository(Context context) {
        roomRepository = new JSONRoomRepository(context);
        gadgetRepository = new JSONGadgetRepository(context);
        sceneRepository = new JSONSceneRepository(context);
        scheduleRepository = new JSONScheduleRepository(context);
        userRepository = new JSONUserRepository(context);
    }

    @Override
    public Home getHome(String id, String name) {

        Home home;
        HomeBuilder builder = new HomeBuilder();
        builder.create(id, name);
        ArrayList<Room> rooms = new ArrayList<Room>();
        rooms.addAll(roomRepository.getAll());
        Log.d("rooms baza", String.valueOf(rooms.size()));
        builder.addRooms(rooms);
        ArrayList<Gadget> gadgets = new ArrayList<Gadget>();
        gadgets.addAll(gadgetRepository.getAll());
        Log.d("gadgets baza", String.valueOf(gadgets.size()));
        builder.addGadgets(gadgets);
        builder.addUsers(new ArrayList<User>());
        builder.addScenes(new ArrayList<Scene>());
        ArrayList<ScheduledScene> scheduledScenes = new ArrayList<ScheduledScene>();
        scheduledScenes.addAll(scheduleRepository.getAll());
        Log.d("scheduledScenes baza", String.valueOf(scheduledScenes.size()));
        builder.addSchedule(scheduledScenes);
        home = builder.getHome();

        sceneRepository.setHome(home);
        ArrayList<Scene> scenes = home.getScenes();
        scenes.addAll(sceneRepository.getAll());
        return home;
    }

    @Override
    public Home getHome(String id) {

        Home home;
        HomeBuilder builder = new HomeBuilder();
        builder.create(id, "dom");
        ArrayList<Room> rooms = new ArrayList<Room>();
        rooms.addAll(roomRepository.getAll());
        Log.d("rooms baza", String.valueOf(rooms.size()));
        builder.addRooms(rooms);
        ArrayList<Gadget> gadgets = new ArrayList<Gadget>();
        gadgets.addAll(gadgetRepository.getAll());
        Log.d("gadgets baza", String.valueOf(gadgets.size()));
        builder.addGadgets(gadgets);
        builder.addUsers(new ArrayList<User>());
        builder.addScenes(new ArrayList<Scene>());
        ArrayList<ScheduledScene> scheduledScenes = new ArrayList<ScheduledScene>();
        scheduledScenes.addAll(scheduleRepository.getAll());
        Log.d("scheduledScenes baza", String.valueOf(scheduledScenes.size()));
        builder.addSchedule(scheduledScenes);
        home = builder.getHome();

        sceneRepository.setHome(home);
        ArrayList<Scene> scenes = home.getScenes();
        scenes.addAll(sceneRepository.getAll());
        return home;
    }

    @Override
    public boolean add(Home home) {
        return false;
    }

    @Override
    public boolean update(Home home) {
        return false;
    }

    @Override
    public boolean delete(Home home) {
        return false;
    }

    @Override
    public boolean add(Gadget gadget) {
        return gadgetRepository.add(gadget);
    }

    @Override
    public boolean update(Gadget gadget) {
        return gadgetRepository.update(gadget);
    }

    @Override
    public boolean delete(Gadget gadget) {
        return gadgetRepository.delete(gadget);
    }

    @Override
    public boolean add(Scene scene) {
        return sceneRepository.add(scene);
    }

    @Override
    public boolean update(Scene scene) {
        return sceneRepository.update(scene);
    }

    @Override
    public boolean delete(Scene scene) {
        return sceneRepository.delete(scene);
    }

    @Override
    public boolean add(Room room) {
        return roomRepository.add(room);
    }

    @Override
    public boolean update(Room room) {
        return roomRepository.update(room);
    }

    @Override
    public boolean delete(Room room) {
        return roomRepository.delete(room);
    }

    @Override
    public boolean add(ScheduledScene scheduledScene) {
        return scheduleRepository.add(scheduledScene);
    }

    @Override
    public boolean update(ScheduledScene scheduledScene) {
        return scheduleRepository.update(scheduledScene);
    }

    @Override
    public boolean delete(ScheduledScene scheduledScene) {
        return scheduleRepository.delete(scheduledScene);
    }

    @Override
    public boolean add(User user) {
        return userRepository.add(user);
    }

    @Override
    public boolean update(User user) {
        return userRepository.update(user);
    }

    @Override
    public boolean delete(User user) {
        return userRepository.delete(user);
    }
}
