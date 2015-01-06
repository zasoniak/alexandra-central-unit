package com.kms.alexandra.data;


import android.util.Log;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.kms.alexandra.data.model.Gadget;
import com.kms.alexandra.data.model.MultiSocket;

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
    public static Gadget create(UUID id, String roomId, String name, String address, Gadget.GadgetType type, int parameter, boolean installed) {
        switch(type)
        {
            case WallSocket:
                return new MultiSocket(id, roomId, name, address, type, parameter, installed);
            case ExtensionCord:
                return new MultiSocket(id, roomId, name, address, type, parameter, installed);
            case LightSwitch:
                return new MultiSocket(id, roomId, name, address, type, parameter, installed);
            case Dimmer:
                return new MultiSocket(id, roomId, name, address, type, parameter, installed);
            case RGBLight:
                return new MultiSocket(id, roomId, name, address, type, parameter, installed);
            default:
                return new MultiSocket(id, roomId, name, address, type, parameter, installed);
        }
    }

    public static void downloadAndAdd(final UUID id, final String roomId, final String name, final int icon, final HomeManager homeManager) {
        final String TAG = "GadgetDownloader";
        Firebase gadgetsRoot = new Firebase("https://sizzling-torch-8921.firebaseio.com/gadgets/");

        gadgetsRoot.child(id.toString()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChild(Gadget.CHANNELS) && dataSnapshot.hasChild(Gadget.INSTALLED) && dataSnapshot.hasChild(Gadget.MAC_ADDRESS) && dataSnapshot.hasChild(Gadget.TYPE))
                {
                    try
                    {
                        String MAC = dataSnapshot.child(Gadget.MAC_ADDRESS).getValue().toString();
                        Gadget.GadgetType type = Gadget.GadgetType.valueOf(dataSnapshot.child(Gadget.TYPE).getValue().toString());
                        int parameter = Integer.parseInt(dataSnapshot.child(Gadget.CHANNELS).getValue().toString());
                        boolean installed = Boolean.parseBoolean(dataSnapshot.child(Gadget.INSTALLED).getValue().toString());
                        homeManager.add(create(id, roomId, name, MAC, type, parameter, installed));
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
