package com.kms.alexandra.data;


import com.kms.alexandra.data.model.Gadget;
import com.kms.alexandra.data.model.Room;
import com.kms.alexandra.data.model.Scene;
import com.kms.alexandra.data.model.ScheduledScene;
import com.kms.alexandra.data.model.User;


/**
 * @author Mateusz Zasoński
 */
public abstract class HomeManagerDecorator extends HomeManager {

    protected HomeManager homeManager;

    public HomeManagerDecorator(HomeManager homeManager) {
        super(homeManager);
        this.homeManager = homeManager;
    }

    @Override
    public boolean add(Room room) {
        return homeManager.add(room);
    }

    @Override
    public boolean delete(Room room) {
        return homeManager.delete(room);
    }

    @Override
    public boolean update(Room room) {
        return homeManager.update(room);
    }

    @Override
    public boolean add(Gadget newGadget) {
        return homeManager.add(newGadget);
    }

    @Override
    public boolean delete(Gadget gadget) {
        return homeManager.delete(gadget);
    }

    @Override
    public boolean update(Gadget newGadget) {
        return homeManager.update(newGadget);
    }

    @Override
    public boolean add(Scene scene) {
        return homeManager.add(scene);
    }

    @Override
    public boolean delete(Scene scene) {
        return homeManager.delete(scene);
    }

    @Override
    public boolean update(Scene newScene) {
        return homeManager.update(newScene);
    }

    @Override
    public boolean add(ScheduledScene scheduledscene) {
        return homeManager.add(scheduledscene);
    }

    @Override
    public boolean delete(ScheduledScene scheduledscene) {
        return homeManager.delete(scheduledscene);
    }

    @Override
    public boolean update(ScheduledScene scheduledscene) {
        return homeManager.update(scheduledscene);
    }

    @Override
    public boolean add(User user) {
        return homeManager.add(user);
    }

    @Override
    public boolean delete(User user) {
        return homeManager.delete(user);
    }

    @Override
    public boolean update(User user) {
        return homeManager.update(user);
    }
}
