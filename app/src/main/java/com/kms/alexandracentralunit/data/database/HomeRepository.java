package com.kms.alexandracentralunit.data.database;


import com.kms.alexandracentralunit.data.model.Gadget;
import com.kms.alexandracentralunit.data.model.Home;
import com.kms.alexandracentralunit.data.model.Room;
import com.kms.alexandracentralunit.data.model.Scene;
import com.kms.alexandracentralunit.data.model.ScheduledScene;
import com.kms.alexandracentralunit.data.model.User;


/**
 * Created by Mateusz Zasoński on 2014-11-11.
 */
public interface HomeRepository {

    public Home getHome(String id, String name);
    public boolean add(Home home);
    public boolean update(Home home);
    public boolean delete(Home home);

    public boolean add(Gadget gadget);
    public boolean update(Gadget gadget);
    public boolean delete(Gadget gadget);

    public boolean add(Scene scene);
    public boolean update(Scene scene);
    public boolean delete(Scene scene);

    public boolean add(Room room);
    public boolean update(Room room);
    public boolean delete(Room room);

    public boolean add(ScheduledScene scheduledscene);
    public boolean update(ScheduledScene scheduledscene);
    public boolean delete(ScheduledScene scheduledscene);

    public boolean add(User user);
    public boolean update(User user);
    public boolean delete(User user);
}
