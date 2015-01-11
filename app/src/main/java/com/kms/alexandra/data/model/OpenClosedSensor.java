package com.kms.alexandra.data.model;


import java.util.UUID;


/**
 * @author Mateusz Zasoński
 */
public class OpenClosedSensor extends MultiSensor {

    public OpenClosedSensor(UUID id, String temporaryRoomId, String name, String MAC, GadgetType type, int channels, boolean installed, int icon, int firmware) {
        super(id, temporaryRoomId, name, MAC, type, channels, installed, icon, firmware);
    }

    public OpenClosedSensor(UUID id, Room room, String name, String MAC, GadgetType type, int channels, boolean installed, int icon, int firmware) {
        super(id, room, name, MAC, type, channels, installed, icon, firmware);
    }
}
