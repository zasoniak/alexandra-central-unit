package com.kms.alexandracentralunit;


import android.util.Log;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.kms.alexandracentralunit.data.model.ActionMessage;
import com.kms.alexandracentralunit.data.model.Scene;

import java.util.UUID;


/**
 * Created by Mateusz Zasoński on 2014-11-15.
 * FirebaseRemoteControlService remoteControl extension allowing remote control via Firebase platform
 * provides remote mirror of gadgets' properties enabling changes from outside home
 *
 * @author Mateusz Zasoński
 * @versio 0.1
 */
public class FirebaseControlMessageDispatcher extends ControlMessageDispatcher {

    private static final String SCENES = "scenes";
    private static final String ACTIONS = "actions";
    private ControlService control;

    public FirebaseControlMessageDispatcher() {
        super();
        final Firebase remoteControlReference = new Firebase("https://sizzling-torch-8921.firebaseio.com/control/"+String.valueOf(CoreService.getHomeId())+"/");
        this.control = ControlService.getInstance();
        remoteControlReference.child(SCENES).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                if(dataSnapshot.hasChild(Scene.ID))
                {
                    control.run(dataSnapshot.child(Scene.ID).getValue().toString());
                }
                remoteControlReference.child(SCENES).child(dataSnapshot.getKey()).removeValue();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

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
        });

        remoteControlReference.child(ACTIONS).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                if(dataSnapshot.hasChild(ActionMessage.GADGET) && dataSnapshot.hasChild(ActionMessage.ACTION) && dataSnapshot.hasChild(ActionMessage.PARAMETER))
                {
                    ActionMessage message;
                    try
                    {
                        UUID gadgetID = UUID.fromString(dataSnapshot.child(ActionMessage.GADGET).getValue().toString());
                        String action = dataSnapshot.child(ActionMessage.ACTION).getValue().toString();
                        String parameter = dataSnapshot.child(ActionMessage.PARAMETER).getValue().toString();
                        if(dataSnapshot.hasChild(ActionMessage.DELAY))
                        {
                            try
                            {
                                long delay = Long.parseLong(dataSnapshot.child(ActionMessage.DELAY).getValue().toString());
                                message = new ActionMessage(gadgetID, action, parameter, delay);
                            }
                            catch (Exception ex)
                            {
                                message = new ActionMessage(gadgetID, action, parameter);
                                Log.e(ACTIONS, "delay - long parse error");
                            }
                        }
                        else
                        {
                            message = new ActionMessage(gadgetID, action, parameter);
                        }
                        control.run(message);
                    }
                    catch (Exception ex)
                    {
                        Log.e(ACTIONS, "gadgetID - UUID parse error");
                    }
                    finally
                    {
                        remoteControlReference.child(ACTIONS).child(dataSnapshot.getKey()).removeValue();
                    }
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

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
        });

    }

}
