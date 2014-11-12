package com.kms.alexandracentralunit.data.database;


import com.kms.alexandracentralunit.data.model.Gadget;

import java.util.List;
import java.util.UUID;


/**
 * Created by Mateusz Zasoński on 2014-10-14.
 */
public interface GadgetRepository {

    public boolean add(Gadget gadget);
    public boolean delete(Gadget gadget);
    public boolean update(Gadget gadget);
    public Gadget find(UUID id);
    public List<Gadget> getAll();
    public List<Gadget> getAllByRoom(String roomId);
    public List<Gadget> getAllByType(String type);
}
