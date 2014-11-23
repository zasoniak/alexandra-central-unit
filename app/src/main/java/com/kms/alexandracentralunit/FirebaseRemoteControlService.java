package com.kms.alexandracentralunit;


import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.kms.alexandracentralunit.data.model.ActionMessage;
import com.kms.alexandracentralunit.data.model.Gadget;
import com.kms.alexandracentralunit.data.model.MultiSocket;
import com.kms.alexandracentralunit.data.model.Observer;
import com.kms.alexandracentralunit.data.model.Socket;
import com.kms.alexandracentralunit.data.model.Switch;

import java.util.UUID;


/**
 * Created by Mateusz Zasoński on 2014-11-15.
 * FirebaseRemoteControlService remoteControl extension allowing remote control via Firebase platform
 * provides remote mirror of gadgets' properties enabling changes from outside home
 */
public class FirebaseRemoteControlService extends RemoteControlService {

    //TODO: here listeners to be placed
    private Firebase remoteControlReference;
    private ControlService control;

    public FirebaseRemoteControlService() {
        super();
        remoteControlReference = new Firebase("https://sizzling-torch-8921.firebaseio.com/control/"+String.valueOf(CoreService.getHomeId())+"/");
        this.control = ControlService.getInstance();

        for(final Gadget gadget : CoreService.getHome().getGadgets())
        {
            remoteControlReference.child(gadget.getId().toString()).addChildEventListener(new GadgetChildEventListener(gadget));
        }

    }

    class GadgetChildEventListener implements ChildEventListener {

        private UUID gadgetID;
        private Gadget.GadgetType gadgetType;
        private Gadget gadgetReference;
        private FirebaseGadgetObserver observer;

        public GadgetChildEventListener(Gadget gadget) {
            this.gadgetReference = gadget;
            this.gadgetID = gadget.getId();
            this.gadgetType = gadget.getType();
            this.observer = new FirebaseGadgetObserver(gadget);
            this.observer.register();

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
                ActionMessage actionMessage;
                switch(gadgetType)
                {
                    case WallSocket:
                        if(dataSnapshot.getKey().equals(Switch.IS_ON))
                        {
                            actionMessage = new ActionMessage(gadgetID, MultiSocket.ActionType.SwitchAll.toString(), dataSnapshot.getValue().toString());
                            control.run(actionMessage);
                        }
                        else
                        {
                            switch(Integer.parseInt(dataSnapshot.getKey()))
                            {
                                case 0:
                                    actionMessage = new ActionMessage(gadgetID, MultiSocket.ActionType.SwitchChannelOne.toString(), dataSnapshot.getValue().toString());
                                    control.run(actionMessage);
                                    break;
                                case 1:
                                    actionMessage = new ActionMessage(gadgetID, MultiSocket.ActionType.SwitchChannelTwo.toString(), dataSnapshot.getValue().toString());
                                    control.run(actionMessage);
                                    break;
                                case 2:
                                    actionMessage = new ActionMessage(gadgetID, MultiSocket.ActionType.SwitchChannelThree.toString(), dataSnapshot.getValue().toString());
                                    control.run(actionMessage);
                                    break;
                                case 3:
                                    actionMessage = new ActionMessage(gadgetID, MultiSocket.ActionType.SwitchChannelFour.toString(), dataSnapshot.getValue().toString());
                                    control.run(actionMessage);
                                    break;
                                case 4:
                                    actionMessage = new ActionMessage(gadgetID, MultiSocket.ActionType.SwitchChannelFive.toString(), dataSnapshot.getValue().toString());
                                    control.run(actionMessage);
                                    break;
                                case 5:
                                    actionMessage = new ActionMessage(gadgetID, MultiSocket.ActionType.SwitchChannelSix.toString(), dataSnapshot.getValue().toString());
                                    control.run(actionMessage);
                                    break;
                            }
                        }
                        break;
                    case ExtensionCord:
                        if(dataSnapshot.getKey().equals(Switch.IS_ON))
                        {
                            actionMessage = new ActionMessage(gadgetID, MultiSocket.ActionType.SwitchAll.toString(), dataSnapshot.getValue().toString());
                            control.run(actionMessage);
                        }
                        else
                        {
                            switch(Integer.parseInt(dataSnapshot.getKey()))
                            {
                                case 0:
                                    actionMessage = new ActionMessage(gadgetID, MultiSocket.ActionType.SwitchChannelOne.toString(), dataSnapshot.getValue().toString());
                                    control.run(actionMessage);
                                    break;
                                case 1:
                                    actionMessage = new ActionMessage(gadgetID, MultiSocket.ActionType.SwitchChannelTwo.toString(), dataSnapshot.getValue().toString());
                                    control.run(actionMessage);
                                    break;
                                case 2:
                                    actionMessage = new ActionMessage(gadgetID, MultiSocket.ActionType.SwitchChannelThree.toString(), dataSnapshot.getValue().toString());
                                    control.run(actionMessage);
                                    break;
                                case 3:
                                    actionMessage = new ActionMessage(gadgetID, MultiSocket.ActionType.SwitchChannelFour.toString(), dataSnapshot.getValue().toString());
                                    control.run(actionMessage);
                                    break;
                                case 4:
                                    actionMessage = new ActionMessage(gadgetID, MultiSocket.ActionType.SwitchChannelFive.toString(), dataSnapshot.getValue().toString());
                                    control.run(actionMessage);
                                    break;
                                case 5:
                                    actionMessage = new ActionMessage(gadgetID, MultiSocket.ActionType.SwitchChannelSix.toString(), dataSnapshot.getValue().toString());
                                    control.run(actionMessage);
                                    break;
                            }
                        }
                        break;
                    case LightSwitch:
                        break;
                    case Dimmer:
                        break;
                    case RGBLight:
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

            private Gadget gadgetReference;

            public FirebaseGadgetObserver(Gadget gadgetReference) {
                this.gadgetReference = gadgetReference;
            }

            public void register() {
                this.gadgetReference.registerObserver(this);
            }

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
