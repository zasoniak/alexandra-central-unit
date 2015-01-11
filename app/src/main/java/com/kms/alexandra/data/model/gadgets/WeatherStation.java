package com.kms.alexandra.data.model.gadgets;


import com.kms.alexandra.data.model.Room;

import java.util.UUID;


/**
 * @author Mateusz Zasoński
 */
public class WeatherStation extends MultiSensor {

    public WeatherStation(UUID id, String temporaryRoomId, String name, String MAC, GadgetType type, int channels, boolean installed, int icon, int firmware) {
        super(id, temporaryRoomId, name, MAC, type, channels, installed, icon, firmware);
    }

    public WeatherStation(UUID id, Room room, String name, String MAC, GadgetType type, int channels, boolean installed, int icon, int firmware) {
        super(id, room, name, MAC, type, channels, installed, icon, firmware);
    }
}
