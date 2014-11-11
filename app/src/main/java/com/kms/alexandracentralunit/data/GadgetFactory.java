package com.kms.alexandracentralunit.data;


import android.content.ContentValues;

import com.kms.alexandracentralunit.data.database.sqlite.SQLiteGadgetRepository;
import com.kms.alexandracentralunit.data.model.Gadget;

import java.util.UUID;


/**
 * Created by Mateusz Zasoński on 2014-10-14.
 */
public class GadgetFactory {

    public static Gadget create(ContentValues values) {

        UUID id = UUID.fromString(values.getAsString(SQLiteGadgetRepository.KEY_GADGET_ID));
        long system_id = values.getAsLong(SQLiteGadgetRepository.KEY_GADGET_SYSTEM);
        long roomId = values.getAsLong(SQLiteGadgetRepository.KEY_GADGET_ROOM);
        String name = values.getAsString(SQLiteGadgetRepository.KEY_GADGET_NAME);
        String address = values.getAsString(SQLiteGadgetRepository.KEY_GADGET_MAC_ADDRESS);
        //TODO: different subclasses!
        int type = values.getAsInteger(SQLiteGadgetRepository.KEY_GADGET_TYPE);

        return new Gadget(id, system_id, roomId, name, address, type);
    }
}
