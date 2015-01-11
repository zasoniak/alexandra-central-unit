package com.kms.alexandra.data;


import android.util.Log;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.kms.alexandra.data.model.Room;
import com.kms.alexandra.data.model.gadgets.Gadget;
import com.kms.alexandra.data.model.gadgets.MultiDimmer;
import com.kms.alexandra.data.model.gadgets.MultiLight;
import com.kms.alexandra.data.model.gadgets.MultiSocket;
import com.kms.alexandra.data.model.gadgets.OpenClosedSensor;
import com.kms.alexandra.data.model.gadgets.RGBMultiController;
import com.kms.alexandra.data.model.gadgets.WallSocket;
import com.kms.alexandra.data.model.gadgets.WeatherStation;

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
    public static Gadget create(UUID id, Room room, String name, String address, Gadget.GadgetType type, int parameter, boolean installed, int icon, int firmware) {
        switch(type)
        {
            case WallSocket:
                return new WallSocket(id, room, name, address, type, parameter, installed, icon, firmware);
            case ExtensionCord:
                return new MultiSocket(id, room, name, address, type, parameter, installed, icon, firmware);
            case LightSwitch:
                return new MultiLight(id, room, name, address, type, parameter, installed, icon, firmware);
            case Dimmer:
                return new MultiDimmer(id, room, name, address, type, parameter, installed, icon, firmware);
            case RGBController:
                return new RGBMultiController(id, room, name, address, type, parameter, installed, icon, firmware);
            case OpenClosedSensor:
                return new OpenClosedSensor(id, room, name, address, type, parameter, installed, icon, firmware);
            case WeatherStation:
                return new WeatherStation(id, room, name, address, type, parameter, installed, icon, firmware);
            default:
                return new MultiSocket(id, room, name, address, type, parameter, installed, icon, firmware);
        }
    }

    public static Gadget create(UUID id, String temporaryRoomID, String name, String address, Gadget.GadgetType type, int parameter, boolean installed, int icon, int firmware) {
        switch(type)
        {
            case WallSocket:
                return new WallSocket(id, temporaryRoomID, name, address, type, parameter, installed, icon, firmware);
            case ExtensionCord:
                return new MultiSocket(id, temporaryRoomID, name, address, type, parameter, installed, icon, firmware);
            case LightSwitch:
                return new MultiLight(id, temporaryRoomID, name, address, type, parameter, installed, icon, firmware);
            case Dimmer:
                return new MultiDimmer(id, temporaryRoomID, name, address, type, parameter, installed, icon, firmware);
            case RGBController:
                return new RGBMultiController(id, temporaryRoomID, name, address, type, parameter, installed, icon, firmware);
            case OpenClosedSensor:
                return new OpenClosedSensor(id, temporaryRoomID, name, address, type, parameter, installed, icon, firmware);
            case WeatherStation:
                return new WeatherStation(id, temporaryRoomID, name, address, type, parameter, installed, icon, firmware);
            default:
                return new MultiSocket(id, temporaryRoomID, name, address, type, parameter, installed, icon, firmware);
        }
    }

    public static void downloadAndAdd(final UUID id, final String roomId, final String name, final int icon, final HomeManager homeManager) {
        final String TAG = "GadgetDownloader";
        Firebase gadgetsRoot = new Firebase("https://sizzling-torch-8921.firebaseio.com/gadgets/");

        gadgetsRoot.child(id.toString()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if((homeManager.home.getRoom(roomId) != null) && dataSnapshot.hasChild(Gadget.CHANNELS) && dataSnapshot.hasChild(Gadget.INSTALLED) && dataSnapshot.hasChild(Gadget.MAC_ADDRESS) && dataSnapshot.hasChild(Gadget.TYPE) && dataSnapshot.hasChild(Gadget.FIRMWARE))
                {
                    try
                    {
                        String MAC = dataSnapshot.child(Gadget.MAC_ADDRESS).getValue().toString();
                        Gadget.GadgetType type = Gadget.GadgetType.valueOf(dataSnapshot.child(Gadget.TYPE).getValue().toString());
                        int parameter = Integer.parseInt(dataSnapshot.child(Gadget.CHANNELS).getValue().toString());
                        boolean installed = Boolean.parseBoolean(dataSnapshot.child(Gadget.INSTALLED).getValue().toString());
                        int firmware = Integer.parseInt(dataSnapshot.child(Gadget.FIRMWARE).getValue().toString());
                        Room room = homeManager.home.getRoom(roomId);
                        homeManager.add(create(id, room, name, MAC, type, parameter, installed, icon, firmware));
                    }
                    catch (IllegalArgumentException ex)
                    {
                        Log.e(TAG, "Gadget - UUID parse error");
                    }
                }
                else
                {
                    Log.e(TAG, "Gadget - missing data");
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }
}
