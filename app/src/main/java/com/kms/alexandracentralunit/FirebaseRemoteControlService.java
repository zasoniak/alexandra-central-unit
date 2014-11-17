package com.kms.alexandracentralunit;


import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.kms.alexandracentralunit.data.model.Gadget;
import com.kms.alexandracentralunit.data.model.MultiSocket;
import com.kms.alexandracentralunit.data.model.Socket;


/**
 * Created by Mateusz Zasoński on 2014-11-15.
 */
public class FirebaseRemoteControlService extends RemoteControlService {

    //TODO: here listeners to be placed
    Firebase remoteControlReference;

    public FirebaseRemoteControlService() {
        super();
        remoteControlReference = new Firebase("https://sizzling-torch-8921.firebaseio.com/control/"+String.valueOf(CoreService.getHomeId())+"/");

        for(final Gadget gadget : home.getGadgets())
        {
            remoteControlReference.child(gadget.getId().toString()).addChildEventListener(new GadgetChildEventListener(gadget));
        }

    }

    class GadgetChildEventListener implements ChildEventListener {

        private Gadget gadgetReference;

        public GadgetChildEventListener(Gadget gadgetReference) {
            this.gadgetReference = gadgetReference;

            //overall gadget information
            remoteControlReference.child(gadgetReference.getId().toString()).child(Gadget.STATE).setValue("OK");
            remoteControlReference.child(gadgetReference.getId().toString()).child(Gadget.TYPE).setValue(gadgetReference.getType());

            if(gadgetReference.getType().equals(Gadget.TYPE_WALL_SOCKET) || gadgetReference.getType().equals(Gadget.TYPE_EXTENSION_CORD))
            {
                remoteControlReference.child(gadgetReference.getId().toString()).child(Socket.IS_ON).setValue(((MultiSocket) gadgetReference).isOn());
                for(int i = 0; i < ((MultiSocket) gadgetReference).getSocketNumber(); i++)
                {
                    remoteControlReference.child(gadgetReference.getId().toString()).child(String.valueOf(i)).child(Socket.IS_ON).setValue(((MultiSocket) gadgetReference).getChannelOn(i));
                    remoteControlReference.child(gadgetReference.getId().toString()).child(String.valueOf(i)).child(Socket.POWER_CONSUMPTION).setValue(((MultiSocket) gadgetReference).getChannelPowerConsuption(i));
                }
            }
        }

        @Override
        public void onChildAdded(DataSnapshot dataSnapshot, String s) {

        }

        @Override
        public void onChildChanged(DataSnapshot dataSnapshot, String s) {
            if(!dataSnapshot.getKey().equals(Gadget.STATE))
            {
                if(this.gadgetReference.getType().equals(Gadget.TYPE_WALL_SOCKET) || this.gadgetReference.getType().equals(Gadget.TYPE_EXTENSION_CORD))
                {
                    if(dataSnapshot.getKey().equals("isOn"))
                    {
                        ((MultiSocket) this.gadgetReference).setOn(Boolean.parseBoolean(dataSnapshot.getValue().toString()));
                    }
                    else
                    {
                        ((MultiSocket) this.gadgetReference).setChannelOn(Integer.parseInt(dataSnapshot.getKey()), Boolean.parseBoolean(dataSnapshot.child("isOn").getValue().toString()));

                    }
                }
            }

        }

        @Override
        public void onChildRemoved(DataSnapshot dataSnapshot) {

        }

        @Override
        public void onChildMoved(DataSnapshot dataSnapshot, String s) {

        }

        @Override
        public void onCancelled(FirebaseError firebaseError) {

        }
    }

}
