package com.kms.alexandracentralunit.data;


import com.kms.alexandracentralunit.data.model.Gadget;

import java.util.UUID;


/**
 * Created by Mateusz Zasoński on 2014-10-14.
 */
public class GadgetFactory {

    public static Gadget create(UUID id, String system_id, String roomId, String name, String address, String type) {

        //TODO: different subclasses chosen by gadget type
        return new Gadget(id, system_id, roomId, name, address, type);
    }
}
