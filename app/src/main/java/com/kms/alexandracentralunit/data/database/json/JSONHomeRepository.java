package com.kms.alexandracentralunit.data.database.json;


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

    public JSONHomeRepository() {
        roomRepository = new JSONRoomRepository();
        gadgetRepository = new JSONGadgetRepository();
        sceneRepository = new JSONSceneRepository();
        scheduleRepository = new JSONScheduleRepository();
        userRepository = new JSONUserRepository();
    }

    @Override
    public Home getHome(String id, String name) {

        HomeBuilder builder = new HomeBuilder();
        builder.create(id, name);
        builder.addGadgets(new ArrayList<Gadget>());
        builder.addRooms(new ArrayList<Room>());
        builder.addUsers(new ArrayList<User>());
        builder.addScenes(new ArrayList<Scene>());
        builder.addSchedule(new ArrayList<ScheduledScene>());

        //        builder.create(2, "domek");
        //        builder.addUsers(userRepository.getAll());
        //        builder.addGadgets(gadgetRepository.getAll());
        //        builder.addRooms(roomRepository.getAll());
        //        builder.addScenes(sceneRepository.getAll());
        //        builder.addSchedule(scheduleRepository.getAll());

        return builder.getHome();
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
