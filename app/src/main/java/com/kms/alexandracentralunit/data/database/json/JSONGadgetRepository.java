package com.kms.alexandracentralunit.data.database.json;


import com.kms.alexandracentralunit.data.database.GadgetRepository;
import com.kms.alexandracentralunit.data.model.Gadget;

import java.util.List;
import java.util.UUID;


/**
 * Created by Mateusz Zasoński on 2014-11-11.
 */
public class JSONGadgetRepository implements GadgetRepository {

    @Override
    public boolean add(Gadget gadget) {
        return false;
    }

    @Override
    public boolean delete(Gadget gadget) {
        return false;
    }

    @Override
    public boolean update(Gadget gadget) {
        return false;
    }

    @Override
    public Gadget find(UUID id) {
        return null;
    }

    @Override
    public List<Gadget> getAll() {
        return null;
    }

    @Override
    public List<Gadget> getAllByRoom(UUID roomID) {
        return null;
    }

    @Override
    public List<Gadget> getAllByType(int type) {
        return null;
    }
}
