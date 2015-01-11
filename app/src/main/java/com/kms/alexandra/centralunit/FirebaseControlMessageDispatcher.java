package com.kms.alexandra.centralunit;


import android.util.Log;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.kms.alexandra.data.model.Scene;
import com.kms.alexandra.data.model.actions.ActionMessage;

import java.util.UUID;


/**
 * Dispatches control messages from Firebase
 * <p/>
 * Encapsulate data if needed and passes it to internal control for further processing
 *
 * @author Mateusz Zasoński
 * @version 0.1
 */
public class FirebaseControlMessageDispatcher extends ControlMessageDispatcher {

    private static final String SCENES = "scenes";
    private static final String ACTIONS = "actions";
    private Control control;

    public FirebaseControlMessageDispatcher() {
        super();
        final Firebase remoteControlReference = new Firebase("https://sizzling-torch-8921.firebaseio.com/control/"+String.valueOf(CoreService.getHomeId())+"/");
        this.control = Control.getInstance();
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
                            catch (NumberFormatException ex)
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
