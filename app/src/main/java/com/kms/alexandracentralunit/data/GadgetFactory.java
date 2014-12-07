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
    public static Gadget create(UUID id, String roomId, String name, String address, Gadget.GadgetType type, int parameter) {
        switch(type)
        {
            case WallSocket:
                return new MultiSocket(id, roomId, name, address, type, parameter);
            case ExtensionCord:
                return new MultiSocket(id, roomId, name, address, type, parameter);
            case LightSwitch:
                return new MultiSocket(id, roomId, name, address, type, parameter);
            case Dimmer:
                return new MultiSocket(id, roomId, name, address, type, parameter);
            case RGBLight:
                return new MultiSocket(id, roomId, name, address, type, parameter);
            default:
                return new MultiSocket(id, roomId, name, address, type, parameter);
        }
    }
}
