package com.kms.alexandra.data;


import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.GenericTypeIndicator;
import com.firebase.client.ValueEventListener;
import com.kms.alexandra.centralunit.MainActivity;
import com.kms.alexandra.data.model.Home;
import com.kms.alexandra.data.model.Room;
import com.kms.alexandra.data.model.Scene;
import com.kms.alexandra.data.model.ScheduledScene;
import com.kms.alexandra.data.model.Trigger;
import com.kms.alexandra.data.model.actions.ActionMessage;
import com.kms.alexandra.data.model.gadgets.Gadget;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;


/**
 * Entry point for remote synchronization
 * Receives and processes data from Firebase server
 *
 * @author Mateusz Zasoński
 * @version 0.1
 */
public class FirebaseSyncService2 extends SyncService {

    private static final String TAG = "FirebaseSyncService";

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.d("firebase", "intentservice");
        super.onHandleIntent(intent);
        final Firebase homeReference = new Firebase("https://sizzling-torch-8921.firebaseio.com/configuration/"+homeManager.getHome().getId()+"/");

        homeReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                //home name
                if(dataSnapshot.hasChild(Home.NAME))
                {
                    DataSnapshot homeNameSnap = dataSnapshot.child(Home.NAME);
                    if(homeNameSnap.getValue() != null)
                    {
                        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putString(MainActivity.HOME_NAME, homeNameSnap.getValue().toString());
                        editor.apply();
                        homeManager.getHome().setName(homeNameSnap.getValue().toString());
                    }
                }
                //users
                if(dataSnapshot.hasChild(Home.USERS))
                {
                    DataSnapshot usersSnap = dataSnapshot.child(Home.USERS);
                    Log.d(Home.USERS, usersSnap.toString());
                }
                //rooms
                if(dataSnapshot.hasChild(Home.ROOMS))
                {
                    for(DataSnapshot roomSnap : dataSnapshot.child(Home.ROOMS).getChildren())
                    {
                        if(roomSnap.hasChild(Room.NAME) && roomSnap.hasChild(Room.COLOR))
                        {
                            String id = roomSnap.getKey();
                            String name = roomSnap.child(Room.NAME).getValue().toString();
                            int color = Integer.parseInt(roomSnap.child(Room.COLOR).getValue().toString());
                            List<UUID> gadgets = new ArrayList<UUID>();
                            if(roomSnap.child(Room.GADGETS).hasChildren())
                            {
                                for(DataSnapshot snapshot : roomSnap.child(Room.GADGETS).getChildren())
                                {
                                    try
                                    {
                                        gadgets.add(UUID.fromString(snapshot.child(Room.ID).getValue().toString()));
                                    }
                                    catch (IllegalArgumentException ex)
                                    {
                                        Log.e(TAG, "Rooms - gadget UUID parse error", ex);
                                    }
                                }
                            }
                            add(new Room(id, name, color, gadgets));
                        }
                        else
                        {
                            Log.e(TAG, "Rooms - missing data");
                        }
                    }
                }

                //gadgets
                if(dataSnapshot.hasChild(Home.GADGETS))
                {
                    for(DataSnapshot gadgetSnap : dataSnapshot.child(Home.GADGETS).getChildren())
                    {
                        if(gadgetSnap.hasChild(Gadget.ROOM_ID) && gadgetSnap.hasChild(Gadget.NAME) && gadgetSnap.hasChild(Gadget.MAC_ADDRESS) && gadgetSnap.hasChild(Gadget.TYPE) && gadgetSnap.hasChild(Gadget.CHANNELS) && gadgetSnap.hasChild(Gadget.INSTALLED) && gadgetSnap.hasChild(Gadget.ICON) && gadgetSnap.hasChild(Gadget.FIRMWARE))
                        {
                            try
                            {
                                UUID id = UUID.fromString(gadgetSnap.getKey());
                                String roomId = gadgetSnap.child(Gadget.ROOM_ID).getValue().toString();
                                Room room = homeManager.home.getRoom(roomId);
                                String name = gadgetSnap.child(Gadget.NAME).getValue().toString();
                                String MAC = gadgetSnap.child(Gadget.MAC_ADDRESS).getValue().toString();
                                Gadget.GadgetType type = Gadget.GadgetType.valueOf(gadgetSnap.child(Gadget.TYPE).getValue().toString());
                                int parameter = Integer.parseInt(gadgetSnap.child(Gadget.CHANNELS).getValue().toString());
                                boolean installed = Boolean.parseBoolean(gadgetSnap.child(Gadget.INSTALLED).getValue().toString());
                                int icon = Integer.parseInt(gadgetSnap.child(Gadget.ICON).getValue().toString());
                                int firmware = Integer.parseInt(gadgetSnap.child(Gadget.FIRMWARE).getValue().toString());
                                add(GadgetFactory.create(id, room, name, MAC, type, parameter, installed, icon, firmware));
                            }
                            catch (Exception ex)
                            {
                                Log.e(TAG, "Gadget - UUID parse error");
                            }
                        }
                        else
                        {
                            Log.e(TAG, "Gadget - missing data");
                        }
                    }
                }
                //scenes
                if(dataSnapshot.hasChild(Home.SCENES))
                {
                    for(DataSnapshot sceneSnap : dataSnapshot.child(Home.SCENES).getChildren())
                    {
                        if(sceneSnap.hasChild(Scene.NAME) && (sceneSnap.hasChild(Scene.SUBSCENES) || sceneSnap.hasChild(Scene.ACTIONS)))
                        {
                            String id = sceneSnap.getKey();
                            String name = sceneSnap.child(Scene.NAME).getValue().toString();
                            SceneBuilder builder = new SceneBuilder(homeManager.getHome());
                            builder.create(id, name);

                            /**
                             * essential action data encapsulation
                             * and passing it to scene builder
                             */
                            List<ActionMessage> actions = new ArrayList<ActionMessage>();
                            if(sceneSnap.hasChild(Scene.ACTIONS))
                            {
                                for(DataSnapshot actionSnapshot : sceneSnap.child(Scene.ACTIONS).getChildren())
                                {
                                    String action = actionSnapshot.child(ActionMessage.ACTION).getValue().toString();
                                    try
                                    {
                                        UUID gadget = UUID.fromString(actionSnapshot.child(ActionMessage.GADGET).getValue().toString());
                                        String parameter = actionSnapshot.child(ActionMessage.PARAMETER).getValue().toString();
                                        long delay = Long.parseLong(actionSnapshot.child(ActionMessage.DELAY).getValue().toString());
                                        actions.add(new ActionMessage(gadget, action, parameter, delay));
                                    }
                                    catch (IllegalArgumentException ex)
                                    {
                                        Log.e(TAG, "Scene - action - gadget UUID parse error", ex);
                                    }
                                }
                            }
                            builder.addActions(actions);

                            /**
                             * getting subscenes' ID list
                             * and passing it to scene builder
                             */
                            List<String> subscenes = new ArrayList<String>();
                            if(sceneSnap.hasChild(Scene.SUBSCENES))
                            {
                                for(DataSnapshot subsceneSnapshot : sceneSnap.child(Scene.SUBSCENES).getChildren())
                                {
                                    subscenes.add(subsceneSnapshot.child(Scene.ID).getValue().toString());
                                }
                            }
                            builder.addSubscenes(subscenes);

                            /**
                             * first step of trigger creation
                             * for next step passing triggers list to scene builder
                             */

                            List<Trigger> triggers = new ArrayList<Trigger>();
                            if(sceneSnap.hasChild(Scene.TRIGGERS))
                            {
                                for(DataSnapshot triggerSnapshot : sceneSnap.child(Scene.TRIGGERS).getChildren())
                                {
                                    Trigger trigger = new Trigger(id);
                                    for(DataSnapshot condition : triggerSnapshot.child(Trigger.CONDITIONS).getChildren())
                                    {
                                        try
                                        {
                                            UUID gadgetID = UUID.fromString(condition.child(Trigger.CONDITION_GADGET).getValue().toString());
                                            String parameter = condition.child(Trigger.CONDITION_PARAMETER).getValue().toString();
                                            String value = condition.child(Trigger.CONDITION_VALUE).getValue().toString();
                                            trigger.addObserver(gadgetID, parameter, value);
                                        }
                                        catch (IllegalArgumentException ex)
                                        {
                                            Log.e(TAG, "scene - trigger - gadget UUID parse error", ex);
                                        }
                                    }
                                    triggers.add(trigger);
                                }
                            }
                            builder.addTriggers(triggers);
                            add(builder.getScene());
                        }
                        else
                        {
                            Log.e(TAG, "Scene - missing data");
                        }
                    }
                }
                //schedules
                if(dataSnapshot.hasChild(Home.SCHEDULE))
                {
                    for(DataSnapshot scheduleSnap : dataSnapshot.child(Home.SCHEDULE).getChildren())
                    {
                        if(scheduleSnap.hasChild(ScheduledScene.SCENE) && scheduleSnap.hasChild(ScheduledScene.HOUR) && scheduleSnap.hasChild(ScheduledScene.MINUTES) && scheduleSnap.hasChild(ScheduledScene.DAYS_OF_WEEK))
                        {
                            try
                            {
                                String id = scheduleSnap.getKey();
                                String scene = scheduleSnap.child(ScheduledScene.SCENE).getValue().toString();
                                int hour = Integer.parseInt(scheduleSnap.child(ScheduledScene.HOUR).getValue().toString());
                                int minutes = Integer.parseInt(scheduleSnap.child(ScheduledScene.MINUTES).getValue().toString());

                                GenericTypeIndicator<List<String>> t = new GenericTypeIndicator<List<String>>() {
                                };
                                List<String> daysList = scheduleSnap.child(ScheduledScene.DAYS_OF_WEEK).getValue(t);

                                boolean[] daysOfWeek = new boolean[7];
                                for(int i = 0; i < daysList.size(); i++)
                                {
                                    String s1 = daysList.get(i);
                                    daysOfWeek[i] = Boolean.parseBoolean(s1);

                                }
                                HashMap<String, String> conditions = new HashMap<String, String>();
                                if(scheduleSnap.hasChild(ScheduledScene.CONDITIONS))
                                {
                                    for(DataSnapshot snapshot : scheduleSnap.child(ScheduledScene.CONDITIONS).getChildren())
                                    {
                                        if(snapshot.hasChild(ScheduledScene.CONDITION_TYPE) && snapshot.hasChild(ScheduledScene.CONDITION_VALUE))
                                        {
                                            conditions.put(snapshot.child(ScheduledScene.CONDITION_TYPE).getValue().toString(), snapshot.child(ScheduledScene.CONDITION_VALUE).getValue().toString());
                                        }
                                    }
                                }
                                add(new ScheduledScene(id, scene, hour, minutes, daysOfWeek, conditions));
                            }
                            catch (NumberFormatException ex)
                            {
                                Log.e(TAG, "Schedule - long parse error", ex);
                            }
                        }
                        else
                        {
                            Log.e(TAG, "Schedule - missing data");
                        }
                    }
                }
                //TODO:  set flag completed
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

    }
}
