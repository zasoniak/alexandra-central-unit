package com.kms.alexandracentralunit.data;


import android.content.ContentValues;

import com.kms.alexandracentralunit.CoreService;
import com.kms.alexandracentralunit.data.database.sqlite.SQLiteRoomRepository;
import com.kms.alexandracentralunit.data.model.Gadget;
import com.kms.alexandracentralunit.data.model.Room;

import java.util.List;
import java.util.UUID;


/**
 * Created by Mateusz Zasoński on 2014-11-05.
 */
public class RoomFactory {

    public static Room create(ContentValues values) {

        UUID id = (UUID) values.get(SQLiteRoomRepository.KEY_ROOM_ID);
        long systemId = values.getAsLong(SQLiteRoomRepository.KEY_ROOM_SYSTEM);
        String name = values.getAsString(SQLiteRoomRepository.KEY_ROOM_NAME);
        int color = values.getAsInteger(SQLiteRoomRepository.KEY_ROOM_COLOR);
        List<Gadget> gadgets = GadgetLinker.getInstance(CoreService.getContext()).getAllByRoom(id);

        return new Room(gadgets, systemId, id, name, color);
    }

}
