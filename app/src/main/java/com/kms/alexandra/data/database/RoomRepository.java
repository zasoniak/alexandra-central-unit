package com.kms.alexandra.data.database;


import com.kms.alexandra.data.model.Room;

import java.util.List;


/**
 * Created by Mateusz Zasoński on 2014-10-16.
 */
public interface RoomRepository {

    public boolean add(Room room);
    public boolean delete(Room room);
    public boolean update(Room room);
    public Room find(String id);
    public List<Room> getAll();
}
