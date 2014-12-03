package com.kms.alexandracentralunit;


import android.util.Log;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;


/**
 * Created by Mateusz Zasoński on 2014-11-15.
 * FirebaseRemoteControlService remoteControl extension allowing remote control via Firebase platform
 * provides remote mirror of gadgets' properties enabling changes from outside home
 */
public class FirebaseControlMessageDispatcher extends ControlMessageDispatcher {

    private ControlService control;

    public FirebaseControlMessageDispatcher() {
        super();
        Firebase remoteControlReference = new Firebase("https://sizzling-torch-8921.firebaseio.com/control/"+String.valueOf(CoreService.getHomeId())+"/");
        this.control = ControlService.getInstance();
        remoteControlReference.child("scenes").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                if(dataSnapshot.hasChild("id"))
                {
                    control.run(dataSnapshot.child("id").getValue().toString());
                }
                Log.d("remote Control scena:", dataSnapshot.child("id").getValue().toString());

                //TODO: usunąć ten message!
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

        remoteControlReference.child("actions").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

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
