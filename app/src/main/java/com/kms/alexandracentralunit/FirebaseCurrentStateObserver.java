package com.kms.alexandracentralunit;


import com.firebase.client.Firebase;
import com.kms.alexandracentralunit.data.model.Gadget;
import com.kms.alexandracentralunit.data.model.Observer;

import java.util.ArrayList;
import java.util.List;


/**
 * @author Mateusz Zasoński
 */
public class FirebaseCurrentStateObserver extends CurrentStateObserver {

    private Firebase currentStateReference;

    public FirebaseCurrentStateObserver() {

        String FIREBASE_ROOT = "https://sizzling-torch-8921.firebaseio.com/currentState/"+String.valueOf(CoreService.getHomeId())+"/";
        currentStateReference = new Firebase(FIREBASE_ROOT);
        List<FirebaseGadgetObserver> observers = new ArrayList<FirebaseGadgetObserver>();
        for(Gadget gadget : CoreService.getHome().getGadgets())
        {
            FirebaseGadgetObserver observer = new FirebaseGadgetObserver(gadget);
            observer.register();
            observer.pushCurrentState();
            observers.add(observer);
        }
    }

    class FirebaseGadgetObserver implements Observer {

        private Gadget gadgetReference;

        public FirebaseGadgetObserver(Gadget gadgetReference) {
            this.gadgetReference = gadgetReference;
        }

        public void register() {
            this.gadgetReference.registerObserver(this);
        }

        public void pushCurrentState() {
            currentStateReference.child(gadgetReference.getId().toString()).setValue(gadgetReference.getCurrentState());
            //            currentStateReference.child(gadgetReference.getId().toString()).child(Gadget.TYPE).setValue(gadgetReference.getType().toString());
            //            currentStateReference.child(gadgetReference.getId().toString()).child(Gadget.STATE).setValue(gadgetReference.getState());
            //            switch(gadgetReference.getType())
            //            {
            //                case WallSocket:
            //                {
            //                    currentStateReference.child(gadgetReference.getId().toString()).child(Socket.ON).setValue(((MultiSocket) gadgetReference).isOn());
            //                    for(int i = 0; i < ((MultiSocket) gadgetReference).getSocketNumber(); i++)
            //                    {
            //                        currentStateReference.child(gadgetReference.getId().toString()).child(String.valueOf(i)).child(Socket.ON).setValue(((MultiSocket) gadgetReference).getChannelOn(i));
            //                        currentStateReference.child(gadgetReference.getId().toString()).child(String.valueOf(i)).child(Socket.POWER_CONSUMPTION).setValue(((MultiSocket) gadgetReference).getChannelPowerConsuption(i));
            //                    }
            //                }
            //                break;
            //                case ExtensionCord:
            //                    break;
            //                case LightSwitch:
            //                    break;
            //                case Dimmer:
            //                    break;
            //                case RGBLight:
            //                    break;
            //            }
        }

        @Override
        public void update(final String field, Object value) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    currentStateReference.child(gadgetReference.getId().toString()).setValue(gadgetReference.toJSON());
                }
                //                    switch(gadgetReference.getType())
                //                    {
                //
                //                        case WallSocket:
                //                        {
                //                            if(field.equals(Socket.ON))
                //                            {
                //                                currentStateReference.child(gadgetReference.getId().toString()).child(Socket.ON).setValue(((MultiSocket) gadgetReference).isOn());
                //                            }
                //                            else
                //                            {
                //                                if(field.equals(Gadget.STATE))
                //                                {
                //                                    currentStateReference.child(gadgetReference.getId().toString()).child(Gadget.STATE).setValue(gadgetReference.getState());
                //                                }
                //                            }
                //                        }
                //                        break;
                //                        case ExtensionCord:
                //                            break;
                //                        case LightSwitch:
                //                            break;
                //                        case Dimmer:
                //                            break;
                //                        case RGBLight:
                //                            break;
                //                    }
                //                }
            });

        }
    }
}
