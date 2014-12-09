package com.kms.alexandracentralunit.data;


import android.app.IntentService;
import android.content.Intent;

import com.kms.alexandracentralunit.CoreService;
import com.kms.alexandracentralunit.FirebaseCurrentStateObserver;
import com.kms.alexandracentralunit.data.database.HomeRepository;
import com.kms.alexandracentralunit.data.model.Gadget;
import com.kms.alexandracentralunit.data.model.Home;
import com.kms.alexandracentralunit.data.model.Room;
import com.kms.alexandracentralunit.data.model.Scene;
import com.kms.alexandracentralunit.data.model.ScheduledScene;
import com.kms.alexandracentralunit.data.model.User;


/**
 * provides system synchronization with remote data repositories
 * <p/>
 * base class for remote synchronization services,
 * providing add/update/delete methods for working system and local repositories
 *
 * @author Mateusz Zasoński
 * @version 0.1
 */
public class SyncService extends IntentService {

    protected Home home;
    protected HomeRepository homeRepository;

    public SyncService() {
        super("Sync service");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        home = CoreService.getHome();
        homeRepository = CoreService.getHomeRepository();
    }

    //TODO: po zgonie program wczytuje wsio jako added!!! co z tymi się zmieniły w tym czasie? (powinny być potraktowane jako updated)
    protected boolean add(Room room) {
        if(home.getRoom(room.getId()) == null)
        {
            home.getRooms().add(room);
            return homeRepository.add(room);
        }
        else
        {
            update(room);
        }
        return false;
    }

    protected boolean delete(Room room) {
        Room temp;
        if((temp = home.getRoom(room.getId())) != null)
        {
            home.getRooms().remove(temp);
            return homeRepository.delete(temp);
        }
        return false;
    }

    protected boolean update(Room room) {
        for(Room room1 : home.getRooms())
        {
            if(room1.getId().equals(room.getId()))
            {
                room1.setColor(room.getColor());
                room1.setName(room.getName());
                room1.setGadgets(room.getGadgets());
                return homeRepository.update(room1);
            }
        }
        return false;
    }

    protected boolean add(Gadget newGadget) {
        if(home.getGadget(newGadget.getId()) == null)
        {
            home.getGadgets().add(newGadget);
            FirebaseCurrentStateObserver.getInstance().addGadget(newGadget);
            if(home.getRoom(newGadget.getRoom()) != null)
            {
                home.getRoom(newGadget.getRoom()).addGadget(newGadget.getId());
            }
            return homeRepository.add(newGadget);
        }
        else
        {
            update(newGadget);
        }
        return false;
    }

    protected boolean delete(Gadget gadget) {
        home.getGadgets().remove(gadget);
        return homeRepository.delete(gadget);
    }

    protected boolean update(Gadget newGadget) {
        for(Gadget gadget : home.getGadgets())
        {
            if(gadget.getId().equals(newGadget.getId()))
            {
                gadget.setName(newGadget.getName());
                if(!newGadget.getRoom().equals(gadget.getRoom()))
                {
                    home.getRoom(gadget.getRoom()).getGadgets().remove(gadget.getId());
                    home.getRoom(newGadget.getRoom()).getGadgets().add(newGadget.getId());
                    gadget.setRoomId(newGadget.getRoom());
                }
                return homeRepository.update(gadget);
            }
        }
        return false;
    }

    protected boolean add(Scene scene) {
        if(home.getScene(scene.getId()) == null)
        {
            home.getScenes().add(scene);
            scene.registerTriggers(home);
            return homeRepository.add(scene);
        }
        else
        {
            update(scene);
        }
        return false;
    }

    protected boolean delete(Scene scene) {
        home.getScenes().remove(scene);
        return homeRepository.delete(scene);
    }

    protected boolean update(Scene newScene) {
        for(Scene scene : home.getScenes())
        {
            if(scene.getId().equals(newScene.getId()))
            {
                scene.setName(newScene.getName());
                scene.unregisterTriggers(home);
                scene.setTriggers(newScene.getTriggers());
                scene.registerTriggers(home);
                scene.setChildren(newScene.getChildren());
                return homeRepository.update(scene);
            }
        }
        return false;
    }

    protected boolean add(ScheduledScene scheduledscene) {
        if(home.getScheduledScene(scheduledscene.getId()) == null)
        {
            home.getSchedule().add(scheduledscene);
            return homeRepository.add(scheduledscene);
        }
        else
        {
            update(scheduledscene);
        }
        return false;
    }

    protected boolean delete(ScheduledScene scheduledscene) {
        home.getSchedule().remove(scheduledscene);
        return homeRepository.delete(scheduledscene);
    }

    protected boolean update(ScheduledScene scheduledscene) {
        for(ScheduledScene scheduledscene1 : home.getSchedule())
        {
            if(scheduledscene1.getId().equals(scheduledscene.getId()))
            {
                scheduledscene1 = scheduledscene;
                return homeRepository.update(scheduledscene1);
            }
        }
        return false;
    }

    protected boolean add(User user) {
        if(home.getUser(user.getId()) == null)
        {
            home.getUsers().add(user);
            return homeRepository.add(user);
        }
        else
        {
            update(user);
        }
        return false;
    }

    protected boolean delete(User user) {
        home.getUsers().remove(user);
        return homeRepository.delete(user);
    }

    protected boolean update(User user) {
        for(User user1 : home.getUsers())
        {
            if(user1.getId().equals(user.getId()))
            {
                user1 = user;
                return homeRepository.update(user1);
            }
        }
        return false;
    }

}
