package com.kms.alexandra.data.model;


import java.util.UUID;


/**
 * @author Mateusz Zasoński
 */
public class WallSocket extends MultiSocket {

    public WallSocket(UUID id, Room room, String name, String address, GadgetType type, int socketNumber, boolean installed, int icon, int firmware) {
        super(id, room, name, address, type, socketNumber, installed, icon, firmware);
    }

    public WallSocket(UUID id, String temporaryRoomId, String name, String address, GadgetType type, int socketNumber, boolean installed, int icon, int firmware) {
        super(id, temporaryRoomId, name, address, type, socketNumber, installed, icon, firmware);
    }
}
