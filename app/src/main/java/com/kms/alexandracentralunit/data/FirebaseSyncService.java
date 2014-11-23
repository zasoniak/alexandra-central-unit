package com.kms.alexandracentralunit.data;


import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.kms.alexandracentralunit.CoreService;
import com.kms.alexandracentralunit.data.database.json.JSONScheduleRepository;
import com.kms.alexandracentralunit.data.model.Action;
import com.kms.alexandracentralunit.data.model.Gadget;
import com.kms.alexandracentralunit.data.model.Home;
import com.kms.alexandracentralunit.data.model.Room;
import com.kms.alexandracentralunit.data.model.Scene;
import com.kms.alexandracentralunit.data.model.ScheduledScene;
import com.kms.alexandracentralunit.data.model.Trigger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;


/**
 * Created by Mateusz Zasoński on 2014-11-11.
 */
public class FirebaseSyncService extends SyncService {

    public FirebaseSyncService() {
        super();
        Firebase homeReference = new Firebase("https://sizzling-torch-8921.firebaseio.com/configuration/"+String.valueOf(CoreService.getHomeId())+"/");

        homeReference.child(Home.NAME).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(CoreService.getContext());
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString(CoreService.HOME_NAME, dataSnapshot.getValue().toString());
                editor.apply();
                home.setName(dataSnapshot.getValue().toString());
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

        homeReference.child(Home.USERS).addChildEventListener(new ChildEventListener() {
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

        homeReference.child(Home.ROOMS).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                String id = dataSnapshot.getKey();
                String name = dataSnapshot.child("name").getValue().toString();
                int color = Integer.parseInt(dataSnapshot.child("color").getValue().toString());
                List<Gadget> gadgets = new ArrayList<Gadget>();
                //                if(dataSnapshot.child("gadgets").hasChildren())
                //                {
                //                    for(DataSnapshot snapshot : dataSnapshot.child("gadgets").getChildren())
                //                    {
                //                        gadgets.add(CoreService.getHome().getGadget(UUID.fromString(snapshot.child("id").getValue().toString())));
                //                    }
                //                }
                add(new Room(id, name, color, gadgets));
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                String id = dataSnapshot.getKey();
                String name = dataSnapshot.child("name").getValue().toString();
                int color = Integer.parseInt(dataSnapshot.child("color").getValue().toString());
                List<Gadget> gadgets = new ArrayList<Gadget>();
                //                if(dataSnapshot.child("gadgets").hasChildren())
                //                {
                //                    for(DataSnapshot snapshot : dataSnapshot.child("gadgets").getChildren())
                //                    {
                //                        gadgets.add(CoreService.getHome().getGadget(UUID.fromString(snapshot.child("id").getValue().toString())));
                //                    }
                //                }
                update(new Room(id, name, color, gadgets));
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                String id = dataSnapshot.getKey();
                String name = dataSnapshot.child("name").getValue().toString();
                int color = Integer.parseInt(dataSnapshot.child("color").getValue().toString());
                List<Gadget> gadgets = new ArrayList<Gadget>();
                delete(new Room(id, name, color, gadgets));
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

        homeReference.child(Home.GADGETS).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                UUID id = UUID.fromString(dataSnapshot.getKey());
                String systemId = CoreService.getHomeId();
                String roomId = dataSnapshot.child("roomId").getValue().toString();
                String name = dataSnapshot.child("name").getValue().toString();
                String MAC = dataSnapshot.child("MAC").getValue().toString();
                Gadget.GadgetType type = Gadget.GadgetType.valueOf(dataSnapshot.child("type").getValue().toString());
                add(GadgetFactory.create(id, systemId, roomId, name, MAC, type));
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                UUID id = UUID.fromString(dataSnapshot.getKey());
                String systemId = CoreService.getHomeId();
                String roomId = dataSnapshot.child("roomId").getValue().toString();
                String name = dataSnapshot.child("name").getValue().toString();
                String MAC = dataSnapshot.child("MAC").getValue().toString();
                Gadget.GadgetType type = Gadget.GadgetType.valueOf(dataSnapshot.child("type").getValue().toString());
                update(GadgetFactory.create(id, systemId, roomId, name, MAC, type));
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                UUID id = UUID.fromString(dataSnapshot.getKey());
                String systemId = CoreService.getHomeId();
                String roomId = dataSnapshot.child("roomId").getValue().toString();
                String name = dataSnapshot.child("name").getValue().toString();
                String MAC = dataSnapshot.child("MAC").getValue().toString();
                Gadget.GadgetType type = Gadget.GadgetType.valueOf(dataSnapshot.child("type").getValue().toString());
                delete(GadgetFactory.create(id, systemId, roomId, name, MAC, type));
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

        homeReference.child(Home.SCENES).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                String id = dataSnapshot.getKey();
                String name = dataSnapshot.child("name").getValue().toString();
                SceneBuilder builder = new SceneBuilder();
                builder.create(id, name);
                List<Trigger> triggers = new ArrayList<Trigger>();
                for(DataSnapshot triggerSnapshot : dataSnapshot.child("triggers").getChildren())
                {
                    String action = triggerSnapshot.child("action").getValue().toString();
                    UUID gadget = UUID.fromString(triggerSnapshot.child("gadget").getValue().toString());
                    HashMap<String, String> parameters = new HashMap<String, String>();
                    for(DataSnapshot parameter : triggerSnapshot.child("parameters").getChildren())
                    {
                        parameters.put(parameter.child("type").getValue().toString(), parameter.child("value").getValue().toString());
                    }
                    //       triggers.add(new Trigger(id, gadget, action, parameters));
                }
                builder.addTriggers(triggers);

                List<Action> actions = new ArrayList<Action>();
                for(DataSnapshot actionSnapshot : dataSnapshot.child("actions").getChildren())
                {
                    String action = actionSnapshot.child("action").getValue().toString();
                    Gadget gadget = home.getGadget(UUID.fromString(actionSnapshot.child("gadget").getValue().toString()));
                    HashMap<String, String> parameters = new HashMap<String, String>();
                    long offset = Long.parseLong(actionSnapshot.child("offset").getValue().toString());
                    for(DataSnapshot parameter : actionSnapshot.child("parameters").getChildren())
                    {
                        parameters.put(parameter.child("type").getValue().toString(), parameter.child("value").getValue().toString());
                    }
                    actions.add(new Action(id, gadget, action, parameters, offset));
                }
                builder.addActions(actions);
                List<Scene> subscenes = new ArrayList<Scene>();
                for(DataSnapshot subsceneSnapshot : dataSnapshot.child("subscenes").getChildren())
                {
                    if(home.getScene(subsceneSnapshot.child("id").getValue().toString()) != null)
                    {
                        subscenes.add(home.getScene(subsceneSnapshot.child("id").getValue().toString()));
                    }
                }
                builder.addSubscenes(subscenes);

                add(builder.getScene());
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                String id = dataSnapshot.getKey();
                String name = dataSnapshot.child("name").getValue().toString();
                SceneBuilder builder = new SceneBuilder();
                builder.create(id, name);
                List<Trigger> triggers = new ArrayList<Trigger>();
                for(DataSnapshot triggerSnapshot : dataSnapshot.child("triggers").getChildren())
                {
                    String action = triggerSnapshot.child("action").getValue().toString();
                    UUID gadget = UUID.fromString(triggerSnapshot.child("gadget").getValue().toString());
                    HashMap<String, String> parameters = new HashMap<String, String>();
                    for(DataSnapshot parameter : triggerSnapshot.child("parameters").getChildren())
                    {
                        parameters.put(parameter.child("type").getValue().toString(), parameter.child("value").getValue().toString());
                    }
                    //       triggers.add(new Trigger(id, gadget, action, parameters));
                }
                builder.addTriggers(triggers);

                List<Action> actions = new ArrayList<Action>();
                for(DataSnapshot actionSnapshot : dataSnapshot.child("actions").getChildren())
                {
                    String action = actionSnapshot.child("action").getValue().toString();
                    Gadget gadget = home.getGadget(UUID.fromString(actionSnapshot.child("gadget").getValue().toString()));
                    HashMap<String, String> parameters = new HashMap<String, String>();
                    long offset = Long.parseLong(actionSnapshot.child("offset").getValue().toString());
                    for(DataSnapshot parameter : actionSnapshot.child("parameters").getChildren())
                    {
                        parameters.put(parameter.child("type").getValue().toString(), parameter.child("value").getValue().toString());
                    }
                    actions.add(new Action(id, gadget, action, parameters, offset));
                }
                builder.addActions(actions);
                List<Scene> subscenes = new ArrayList<Scene>();
                for(DataSnapshot subsceneSnapshot : dataSnapshot.child("subscenes").getChildren())
                {
                    if(home.getScene(subsceneSnapshot.child("id").getValue().toString()) != null)
                    {
                        subscenes.add(home.getScene(subsceneSnapshot.child("id").getValue().toString()));
                    }
                }
                builder.addSubscenes(subscenes);

                update(builder.getScene());
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                String id = dataSnapshot.getKey();
                String name = dataSnapshot.child("name").getValue().toString();
                SceneBuilder builder = new SceneBuilder();
                builder.create(id, name);
                List<Trigger> triggers = new ArrayList<Trigger>();
                for(DataSnapshot triggerSnapshot : dataSnapshot.child("triggers").getChildren())
                {
                    String action = triggerSnapshot.child("action").getValue().toString();
                    UUID gadget = UUID.fromString(triggerSnapshot.child("gadget").getValue().toString());
                    HashMap<String, String> parameters = new HashMap<String, String>();
                    for(DataSnapshot parameter : triggerSnapshot.child("parameters").getChildren())
                    {
                        parameters.put(parameter.child("type").getValue().toString(), parameter.child("value").getValue().toString());
                    }
                    //           triggers.add(new Trigger(id, gadget, action, parameters));
                }
                builder.addTriggers(triggers);

                List<Action> actions = new ArrayList<Action>();
                for(DataSnapshot actionSnapshot : dataSnapshot.child("actions").getChildren())
                {
                    String action = actionSnapshot.child("action").getValue().toString();
                    Gadget gadget = home.getGadget(UUID.fromString(actionSnapshot.child("gadget").getValue().toString()));
                    HashMap<String, String> parameters = new HashMap<String, String>();
                    long offset = Long.parseLong(actionSnapshot.child("offset").getValue().toString());
                    for(DataSnapshot parameter : actionSnapshot.child("parameters").getChildren())
                    {
                        parameters.put(parameter.child("type").getValue().toString(), parameter.child("value").getValue().toString());
                    }
                    actions.add(new Action(id, gadget, action, parameters, offset));
                }
                builder.addActions(actions);
                List<Scene> subscenes = new ArrayList<Scene>();
                for(DataSnapshot subsceneSnapshot : dataSnapshot.child("subscenes").getChildren())
                {
                    if(home.getScene(subsceneSnapshot.child("id").getValue().toString()) != null)
                    {
                        subscenes.add(home.getScene(subsceneSnapshot.child("id").getValue().toString()));
                    }
                }
                builder.addSubscenes(subscenes);

                delete(builder.getScene());
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

        homeReference.child(Home.SCHEDULE).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                String id = dataSnapshot.getKey();
                String scene = dataSnapshot.child("scene").getValue().toString();
                long time = Long.parseLong(dataSnapshot.child("time").getValue().toString());
                long repeatInterval = Long.parseLong(dataSnapshot.child("repeatInterval").getValue().toString());
                HashMap<String, String> conditions = new HashMap<String, String>();
                for(DataSnapshot snapshot : dataSnapshot.child("conditions").getChildren())
                {
                    conditions.put(snapshot.child("type").getValue().toString(), snapshot.child("value").getValue().toString());
                }

                add(new ScheduledScene(id, scene, time, repeatInterval, conditions));
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                String id = dataSnapshot.getKey();
                String scene = dataSnapshot.child("scene").getValue().toString();
                long time = Long.parseLong(dataSnapshot.child("time").getValue().toString());
                long repeatInterval = Long.parseLong(dataSnapshot.child("repeatInterval").toString());
                HashMap<String, String> conditions = new HashMap<String, String>();
                for(DataSnapshot snapshot : dataSnapshot.child("conditions").getChildren())
                {
                    conditions.put(snapshot.child("type").getValue().toString(), snapshot.child("value").getValue().toString());
                }

                update(new ScheduledScene(id, scene, time, repeatInterval, conditions));
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                String id = dataSnapshot.getKey();
                String scene = dataSnapshot.child(JSONScheduleRepository.SCENE).getValue().toString();
                long time = Long.parseLong(dataSnapshot.child(JSONScheduleRepository.TIME).getValue().toString());
                long repeatInterval = Long.parseLong(dataSnapshot.child(JSONScheduleRepository.REPEAT_INTERVAL).toString());
                HashMap<String, String> conditions = new HashMap<String, String>();
                for(DataSnapshot snapshot : dataSnapshot.child(JSONScheduleRepository.CONDITIONS).getChildren())
                {
                    conditions.put(snapshot.child(JSONScheduleRepository.CONDITION_TYPE).getValue().toString(), snapshot.child(JSONScheduleRepository.CONDITION_VALUE).getValue().toString());
                }

                delete(new ScheduledScene(id, scene, time, repeatInterval, conditions));
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
