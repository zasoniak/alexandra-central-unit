package com.kms.alexandracentralunit.data;


import com.kms.alexandracentralunit.data.model.Gadget;
import com.kms.alexandracentralunit.data.model.MultiSocket;

import java.util.UUID;


/**
 * provides different gadget according to its type
 * <p/>
 * factory method pattern, provides different outcome object according to gadget type selected
 *
 * @author Mateusz Zasoński
 * @version 0.1
 */
public class GadgetFactory {

    //TODO: additional parameter, like number of channels
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
