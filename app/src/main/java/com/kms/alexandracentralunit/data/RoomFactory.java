package com.kms.alexandracentralunit.data;


import android.content.ContentValues;

import com.kms.alexandracentralunit.CoreService;
import com.kms.alexandracentralunit.data.database.sqlite.SQLiteRoomRepository;
import com.kms.alexandracentralunit.data.model.Gadget;
import com.kms.alexandracentralunit.data.model.Room;

import java.util.List;


/**
 * Created by Mateusz Zasoński on 2014-11-05.
 */
public class RoomFactory {

    public static Room create(ContentValues values) {

        String id = values.getAsString(SQLiteRoomRepository.KEY_ROOM_ID);
        String name = values.getAsString(SQLiteRoomRepository.KEY_ROOM_NAME);
        int color = values.getAsInteger(SQLiteRoomRepository.KEY_ROOM_COLOR);
        List<Gadget> gadgets = GadgetLinker.getInstance(CoreService.getContext()).getAllByRoom(id);

        return new Room(id, name, color, gadgets);
    }

}
