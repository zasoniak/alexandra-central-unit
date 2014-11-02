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
        String name = values.getAsString(SQLiteGadgetRepository.KEY_GADGET_NAME);
        String address = values.getAsString(SQLiteGadgetRepository.KEY_GADGET_MAC_ADDRESS);
        UUID roomId = UUID.fromString(values.getAsString(SQLiteGadgetRepository.KEY_GADGET_ROOM));
        int type = values.getAsInteger(SQLiteGadgetRepository.KEY_GADGET_TYPE);

        return new Gadget(id, name, address, roomId, type);
    }
}
