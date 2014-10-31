package com.kms.alexandracentralunit.data.database;


import com.kms.alexandracentralunit.data.model.Room;

import java.util.List;


/**
 * Created by Mateusz Zasoński on 2014-10-16.
 */
public interface RoomRepository {

    public boolean add(Room room);
    public boolean delete(Room room);
    public boolean update(Room room);
    public Room find(long id);
    public List<Room> getAll();
    public List<Room> getAllByType(int type);

}
