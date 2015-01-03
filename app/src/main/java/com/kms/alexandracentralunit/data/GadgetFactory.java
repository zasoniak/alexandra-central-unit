package com.kms.alexandracentralunit.data;


import android.os.AsyncTask;
import android.util.Log;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.kms.alexandracentralunit.data.model.Gadget;
import com.kms.alexandracentralunit.data.model.MultiSocket;

import java.util.UUID;
import java.util.concurrent.CountDownLatch;


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

    private class GadgetDownloader extends AsyncTask<UUID, Void, Gadget> {

        private final String TAG = "GadgetDownloader";
        Gadget gadget;

        @Override
        protected Gadget doInBackground(UUID... uuids) {
            final CountDownLatch latch = new CountDownLatch(1);

            Firebase gadgetsRoot = new Firebase("https://sizzling-torch-8921.firebaseio.com/gadgets/");
            gadgetsRoot.child(uuids[0].toString()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if(dataSnapshot.hasChild(Gadget.ROOM_ID) && dataSnapshot.hasChild(Gadget.NAME) && dataSnapshot.hasChild(Gadget.MAC_ADDRESS) && dataSnapshot.hasChild(Gadget.TYPE))
                    {
                        try
                        {
                            UUID id = UUID.fromString(dataSnapshot.getKey());
                            String roomId = dataSnapshot.child(Gadget.ROOM_ID).getValue().toString();
                            String name = dataSnapshot.child(Gadget.NAME).getValue().toString();
                            String MAC = dataSnapshot.child(Gadget.MAC_ADDRESS).getValue().toString();
                            Gadget.GadgetType type = Gadget.GadgetType.valueOf(dataSnapshot.child(Gadget.TYPE).getValue().toString());
                            int parameter = Integer.parseInt(dataSnapshot.child(Gadget.CHANNELS).getValue().toString());
                            boolean installed = Boolean.parseBoolean(dataSnapshot.child(Gadget.INSTALLED).getValue().toString());
                            gadget = create(id, roomId, name, MAC, type, parameter, installed);
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
                    latch.countDown();
                }

                @Override
                public void onCancelled(FirebaseError firebaseError) {

                }
            });
            try
            {
                latch.await();
                return gadget;
            }
            catch (InterruptedException e)
            {
                e.printStackTrace();
                return null;
            }
        }
    }
}
