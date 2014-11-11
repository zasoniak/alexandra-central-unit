package com.kms.alexandracentralunit.data;


import android.app.IntentService;
import android.content.Intent;

import com.kms.alexandracentralunit.CoreService;
import com.kms.alexandracentralunit.data.database.HomeRepository;
import com.kms.alexandracentralunit.data.model.Gadget;
import com.kms.alexandracentralunit.data.model.Home;
import com.kms.alexandracentralunit.data.model.Room;
import com.kms.alexandracentralunit.data.model.Scene;
import com.kms.alexandracentralunit.data.model.ScheduledScene;
import com.kms.alexandracentralunit.data.model.User;


public class SyncService extends IntentService {

    private Home home;
    private HomeRepository homeRepository;

    public SyncService() {
        super("Sync service");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        home = CoreService.getHome();
        homeRepository = CoreService.getHomeRepository();

    }

    public boolean sync() {
        //TODO: sync on demand?
        return false;
    }

    private boolean add(Room room) {
        home.getRooms().add(room);

        return homeRepository.add(room);

    }

    private boolean delete(Room room) {
        home.getRooms().remove(room);

        return homeRepository.delete(room);

    }

    private boolean update(Room room) {
        for(Room room1 : home.getRooms())
        {
            if(room1.getId() == room.getId())
            {
                room1.setColor(room.getColor());
                room1.setName(room.getName());
                room1.setGadgets(room.getGadgets());
                return homeRepository.update(room1);
            }
        }
        return false;
    }

    private boolean add(Gadget gadget) {
        home.getGadgets().add(gadget);

        return homeRepository.add(gadget);

    }

    private boolean delete(Gadget gadget) {
        home.getGadgets().remove(gadget);
        return homeRepository.delete(gadget);

    }

    private boolean update(Gadget gadget) {
        for(Gadget gadget1 : home.getGadgets())
        {
            if(gadget1.getId().equals(gadget.getId()))
            {
                gadget1.setName(gadget.getName());
                gadget1.setRoomId(gadget.getRoom());
                return homeRepository.update(gadget1);
            }
        }
        return false;
    }

    private boolean add(Scene scene) {
        home.getScenes().add(scene);

        return homeRepository.add(scene);

    }

    private boolean delete(Scene scene) {
        home.getScenes().remove(scene);
        return homeRepository.delete(scene);

    }

    private boolean update(Scene scene) {
        for(Scene scene1 : home.getScenes())
        {
            if(scene1.getId() == scene.getId())
            {
                scene1.setName(scene.getName());
                scene1.setOffset(scene.getOffset());
                scene1.setTriggers(scene.getTriggers());
                scene1.setChildren(scene.getChildren());
                return homeRepository.update(scene1);
            }
        }
        return false;
    }

    private boolean add(ScheduledScene scheduledscene) {
        home.getSchedule().add(scheduledscene);

        return homeRepository.add(scheduledscene);

    }

    private boolean delete(ScheduledScene scheduledscene) {
        home.getSchedule().remove(scheduledscene);
        return homeRepository.delete(scheduledscene);

    }

    private boolean update(ScheduledScene scheduledscene) {
        for(ScheduledScene scheduledscene1 : home.getSchedule())
        {
            if(scheduledscene1.getId() == scheduledscene.getId())
            {
                scheduledscene1 = scheduledscene;
                return homeRepository.update(scheduledscene1);
            }
        }
        return false;
    }

    private boolean add(User user) {
        home.getUsers().add(user);

        return homeRepository.add(user);

    }

    private boolean delete(User user) {
        home.getUsers().remove(user);
        return homeRepository.delete(user);

    }

    private boolean update(User user) {
        for(User user1 : home.getUsers())
        {
            if(user1.getId() == user.getId())
            {
                user1 = user;
                return homeRepository.update(user1);
            }
        }
        return false;
    }

}
