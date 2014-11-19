package com.kms.alexandracentralunit;


import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.kms.alexandracentralunit.data.model.Gadget;
import com.kms.alexandracentralunit.data.model.MultiSocket;
import com.kms.alexandracentralunit.data.model.Observer;
import com.kms.alexandracentralunit.data.model.Socket;
import com.kms.alexandracentralunit.data.model.Switch;


/**
 * Created by Mateusz Zasoński on 2014-11-15.
 * FirebaseRemoteControlService remoteControl extension allowing remote control via Firebase platform
 * provides remote mirror of gadgets' properties enabling changes from outside home
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
        private FirebaseGadgetObserver observer;

        public GadgetChildEventListener(Gadget gadget) {
            this.gadgetReference = gadget;
            this.observer = new FirebaseGadgetObserver();
            this.gadgetReference.registerObserver(observer);

            //overall gadget information
            remoteControlReference.child(gadgetReference.getId().toString()).child(Gadget.STATE).setValue(gadgetReference.getState().toString());
            remoteControlReference.child(gadgetReference.getId().toString()).child(Gadget.TYPE).setValue(gadgetReference.getType().toString());

            if(gadgetReference.getType().equals(Gadget.GadgetType.WallSocket) || gadgetReference.getType().equals(Gadget.GadgetType.ExtensionCord))
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
                switch(gadgetReference.getType())
                {
                    case WallSocket:
                        if(dataSnapshot.getKey().equals(Switch.IS_ON))
                        {
                            ((MultiSocket) this.gadgetReference).setOn(Boolean.parseBoolean(dataSnapshot.getValue().toString()));
                        }
                        else
                        {
                            ((MultiSocket) this.gadgetReference).setChannelOn(Integer.parseInt(dataSnapshot.getKey()), Boolean.parseBoolean(dataSnapshot.child(Socket.IS_ON).getValue().toString()));

                        }
                        break;
                    case ExtensionCord:
                        if(dataSnapshot.getKey().equals(Switch.IS_ON))
                        {
                            ((MultiSocket) this.gadgetReference).setOn(Boolean.parseBoolean(dataSnapshot.getValue().toString()));
                        }
                        else
                        {
                            ((MultiSocket) this.gadgetReference).setChannelOn(Integer.parseInt(dataSnapshot.getKey()), Boolean.parseBoolean(dataSnapshot.child(Socket.IS_ON).getValue().toString()));

                        }
                        break;
                    default:
                        break;
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

        class FirebaseGadgetObserver implements Observer {

            @Override
            public void update(String field, Object value) {
                if(field.equals("isOn"))
                {
                    remoteControlReference.child(gadgetReference.getId().toString()).child(Switch.IS_ON).setValue(value);

                }
                else
                {
                    if(field.equals("isOnChannel0"))
                    {
                        remoteControlReference.child(gadgetReference.getId().toString()).child("0").child(Socket.IS_ON).setValue(value);
                    }
                }
            }
        }
    }

}
