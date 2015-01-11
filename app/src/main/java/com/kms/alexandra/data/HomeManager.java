package com.kms.alexandra.data;


import android.util.Log;

import com.kms.alexandra.data.database.HomeRepository;
import com.kms.alexandra.data.model.Home;
import com.kms.alexandra.data.model.Room;
import com.kms.alexandra.data.model.Scene;
import com.kms.alexandra.data.model.ScheduledScene;
import com.kms.alexandra.data.model.User;
import com.kms.alexandra.data.model.gadgets.Gadget;


/**
 * @author Mateusz Zasoński
 */
public class HomeManager {

    protected Home home;
    protected HomeRepository homeRepository;

    public HomeManager(HomeManager homeManager) {
        this.home = homeManager.home;
        this.homeRepository = homeManager.homeRepository;
    }

    public HomeManager(Home home, HomeRepository homeRepository) {
        this.home = home;
        this.homeRepository = homeRepository;
    }

    public boolean add(Room room) {
        Log.d("homeManager", "newRoom");
        return true;
    }

    public Home getHome() {
        return this.home;
    }

    public boolean delete(Room room) {
        return true;
    }

    public boolean update(Room room) {
        return true;
    }

    public boolean add(Gadget newGadget) {
        return true;
    }

    public boolean delete(Gadget gadget) {
        return true;
    }

    public boolean update(Gadget newGadget) {
        return true;
    }

    public boolean add(Scene scene) {
        return true;
    }

    public boolean delete(Scene scene) {
        return true;
    }

    public boolean update(Scene newScene) {
        return true;
    }

    public boolean add(ScheduledScene scheduledscene) {
        return true;
    }

    public boolean delete(ScheduledScene scheduledscene) {
        return true;
    }

    public boolean update(ScheduledScene scheduledscene) {
        return true;
    }

    public boolean add(User user) {
        return true;
    }

    public boolean delete(User user) {
        return true;
    }

    public boolean update(User user) {
        return true;
    }

}
