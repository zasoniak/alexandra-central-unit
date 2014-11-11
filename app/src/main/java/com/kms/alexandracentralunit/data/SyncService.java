package com.kms.alexandracentralunit.data;


import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import com.kms.alexandracentralunit.CoreService;
import com.kms.alexandracentralunit.data.database.HomeRepository;
import com.kms.alexandracentralunit.data.model.Gadget;
import com.kms.alexandracentralunit.data.model.Home;
import com.kms.alexandracentralunit.data.model.Room;
import com.kms.alexandracentralunit.data.model.Scene;
import com.kms.alexandracentralunit.data.model.ScheduledScene;
import com.kms.alexandracentralunit.data.model.User;


public class SyncService extends IntentService {

    protected Home home;
    protected HomeRepository homeRepository;

    public SyncService() {
        super("Sync service");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.d("syncservice", "started");
        home = CoreService.getHome();
        homeRepository = CoreService.getHomeRepository();

    }

    public boolean sync() {
        //TODO: sync on demand?
        return false;
    }

    protected boolean add(Room room) {
        if(home.getRoom(room.getId()) == null)
        home.getRooms().add(room);
        Log.d("ilosc pokoi:", String.valueOf(home.getRooms().size()));
        return homeRepository.add(room);

    }

    protected boolean delete(Room room) {
        Room temp = home.getRoom(room.getId());
        home.getRooms().remove(temp);

        return homeRepository.delete(temp);

    }

    protected boolean update(Room room) {
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

    protected boolean add(Gadget gadget) {
        if(home.getGadget(gadget.getId()) == null)
        home.getGadgets().add(gadget);

        return homeRepository.add(gadget);

    }

    protected boolean delete(Gadget gadget) {
        home.getGadgets().remove(gadget);
        return homeRepository.delete(gadget);

    }

    protected boolean update(Gadget gadget) {
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

    protected boolean add(Scene scene) {
        if(home.getScene(scene.getId()) == null)
        home.getScenes().add(scene);

        return homeRepository.add(scene);

    }

    protected boolean delete(Scene scene) {
        home.getScenes().remove(scene);
        return homeRepository.delete(scene);

    }

    protected boolean update(Scene scene) {
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

    protected boolean add(ScheduledScene scheduledscene) {
        if(home.getScheduledScene(scheduledscene.getId()) == null)
        home.getSchedule().add(scheduledscene);

        return homeRepository.add(scheduledscene);

    }

    protected boolean delete(ScheduledScene scheduledscene) {
        home.getSchedule().remove(scheduledscene);
        return homeRepository.delete(scheduledscene);

    }

    protected boolean update(ScheduledScene scheduledscene) {
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

    protected boolean add(User user) {
        if(home.getUser(user.getId()) == null)
        home.getUsers().add(user);

        return homeRepository.add(user);

    }

    protected boolean delete(User user) {
        home.getUsers().remove(user);
        return homeRepository.delete(user);

    }

    protected boolean update(User user) {
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
