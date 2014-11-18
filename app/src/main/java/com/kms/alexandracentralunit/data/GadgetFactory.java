package com.kms.alexandracentralunit.data;


import com.kms.alexandracentralunit.data.model.Gadget;
import com.kms.alexandracentralunit.data.model.MultiSocket;

import java.util.UUID;


/**
 * Created by Mateusz Zasoński on 2014-10-14.
 */
public class GadgetFactory {

    public static Gadget create(UUID id, String system_id, String roomId, String name, String address, Gadget.GadgetType type) {
        switch(type)
        {
            case WallSocket:
                return new MultiSocket(id, system_id, roomId, name, address, type, 2);
            case ExtensionCord:
                return new MultiSocket(id, system_id, roomId, name, address, type, 2);
            case LightSwitch:
                return new Gadget(id, system_id, roomId, name, address, type);
            case Dimmer:
                return new Gadget(id, system_id, roomId, name, address, type);
            case RGBLight:
                return new Gadget(id, system_id, roomId, name, address, type);
            default:
                return new Gadget(id, system_id, roomId, name, address, type);
        }
    }
}
