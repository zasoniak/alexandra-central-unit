package com.kms.alexandra.data.model;


import java.util.UUID;


/**
 * @author Mateusz Zasoński
 */
public class MultiDimmer extends MultiLight {

    public MultiDimmer(UUID id, Room room, String name, String address, GadgetType type, int channelsNumber, boolean installed, int icon, int firmware) {
        super(id, room, name, address, type, channelsNumber, installed, icon, firmware);
    }

    public MultiDimmer(UUID id, String temporaryRoomId, String name, String address, GadgetType type, int socketNumber, boolean installed, int icon, int firmware) {
        super(id, temporaryRoomId, name, address, type, socketNumber, installed, icon, firmware);
    }
}
