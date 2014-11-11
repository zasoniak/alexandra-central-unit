package com.kms.alexandracentralunit.data;


import android.content.Intent;
import android.util.Log;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.kms.alexandracentralunit.CoreService;
import com.kms.alexandracentralunit.data.model.Gadget;
import com.kms.alexandracentralunit.data.model.Room;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Mateusz Zasoński on 2014-11-11.
 */
public class FirebaseSyncService extends SyncService {

    public FirebaseSyncService() {
        super();

        Firebase homeReference = new Firebase("https://sizzling-torch-8921.firebaseio.com/systems/"+String.valueOf(CoreService.getHomeId())+"/");

        homeReference.child("users").addChildEventListener(new ChildEventListener() {
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

        homeReference.child("gadgets").addChildEventListener(new ChildEventListener() {
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

        homeReference.child("rooms").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Log.d("room", dataSnapshot.getKey()+": "+dataSnapshot.getValue().toString());

                long id = Long.parseLong(dataSnapshot.child("id").getValue().toString());
                long systemId = 0;
                String name = dataSnapshot.child("name").getValue().toString();
                int color = Integer.parseInt(dataSnapshot.child("color").getValue().toString());
                List<Gadget> gadgets = new ArrayList<Gadget>();
                if(dataSnapshot.child("gadgets").hasChildren())
                {
                    for(DataSnapshot snapshot : dataSnapshot.child("gadgets").getChildren())
                    {
                        // gadgets.add(CoreService.getHome().getGadget(UUID.fromString(snapshot.child("id").getValue().toString())));
                    }
                }
                add(new Room(id, systemId, name, color, gadgets));
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                long id = Long.parseLong(dataSnapshot.child("id").getValue().toString());
                long systemId = 0;
                String name = dataSnapshot.child("name").getValue().toString();
                int color = Integer.parseInt(dataSnapshot.child("color").getValue().toString());
                List<Gadget> gadgets = new ArrayList<Gadget>();
                if(dataSnapshot.child("gadgets").hasChildren())
                {
                    for(DataSnapshot snapshot : dataSnapshot.child("gadgets").getChildren())
                    {
                        // gadgets.add(CoreService.getHome().getGadget(UUID.fromString(snapshot.child("id").getValue().toString())));
                    }
                }
                update(new Room(id, systemId, name, color, gadgets));
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                long id = Long.parseLong(dataSnapshot.child("id").getValue().toString());
                long systemId = 0;
                String name = dataSnapshot.child("name").getValue().toString();
                int color = Integer.parseInt(dataSnapshot.child("color").getValue().toString());
                List<Gadget> gadgets = new ArrayList<Gadget>();
                delete(new Room(id, systemId, name, color, gadgets));
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

        homeReference.child("scenes").addChildEventListener(new ChildEventListener() {
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

        homeReference.child("schedule").addChildEventListener(new ChildEventListener() {
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

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.d("firebase", "intentservice");
        super.onHandleIntent(intent);
    }
}
