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
    protected HomeManager homeManager;

    public SyncService() {
        super("Sync service");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        home = CoreService.getHome();
        homeRepository = CoreService.getHomeRepository();
        homeManager = new LocalHomeManagerDecorator(new HomeManager(CoreService.getHome(), CoreService.getHomeRepository()));
    }

    protected boolean add(Room room) {
        Log.d("sync", "newRoom");
        return homeManager.add(room);
    }

    protected boolean delete(Room room) {
        return homeManager.delete(room);
    }

    protected boolean update(Room room) {
        return homeManager.update(room);
    }

    protected boolean add(Gadget newGadget) {
        return homeManager.add(newGadget);
    }

    protected boolean delete(Gadget gadget) {
        return homeManager.delete(gadget);
    }

    protected boolean update(Gadget newGadget) {
        return homeManager.update(newGadget);
    }

    protected boolean add(Scene scene) {
        return homeManager.add(scene);
    }

    protected boolean delete(Scene scene) {
        return homeManager.delete(scene);
    }

    protected boolean update(Scene newScene) {
        return homeManager.update(newScene);
    }

    protected boolean add(ScheduledScene scheduledscene) {
        return homeManager.add(scheduledscene);
    }

    protected boolean delete(ScheduledScene scheduledscene) {
        return homeManager.delete(scheduledscene);
    }

    protected boolean update(ScheduledScene scheduledscene) {
        return homeManager.update(scheduledscene);
    }

    protected boolean add(User user) {
        return homeManager.add(user);
    }

    protected boolean delete(User user) {
        return homeManager.delete(user);
    }

    protected boolean update(User user) {
        return homeManager.update(user);
    }

}
