package com.kms.alexandra.data;


import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.firebase.client.ChildEventListener;
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
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;


/**
 * Entry point for remote synchronization
 * Receives and processes data from Firebase server
 *
 * @author Mateusz Zasoński
 * @version 0.1
 */
public class FirebaseSyncService extends SyncService {
    //TODO: constants!!!!

    private static final String TAG = "FirebaseSyncService";

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.d("firebase", "intentservice");
        super.onHandleIntent(intent);
        final Firebase homeReference = new Firebase("https://sizzling-torch-8921.firebaseio.com/configuration/"+homeManager.getHome().getId()+"/");

        homeReference.child(Home.NAME).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.getValue() != null)
                {
                    SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString(MainActivity.HOME_NAME, dataSnapshot.getValue().toString());
                    editor.apply();
                    homeManager.getHome().setName(dataSnapshot.getValue().toString());
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

        //TODO: users?
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
                Log.i("room", "added");
                if(dataSnapshot.hasChild(Room.NAME) && dataSnapshot.hasChild(Room.COLOR))
                {
                    String id = dataSnapshot.getKey();
                    String name = dataSnapshot.child(Room.NAME).getValue().toString();
                    int color = Integer.parseInt(dataSnapshot.child(Room.COLOR).getValue().toString());
                    List<UUID> gadgets = new ArrayList<UUID>();
                    if(dataSnapshot.child(Room.GADGETS).hasChildren())
                    {
                        for(DataSnapshot snapshot : dataSnapshot.child(Room.GADGETS).getChildren())
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

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                Log.i("room", "changed");
                if(dataSnapshot.hasChild(Room.NAME) && dataSnapshot.hasChild(Room.COLOR))
                {
                    String id = dataSnapshot.getKey();
                    String name = dataSnapshot.child(Room.NAME).getValue().toString();
                    int color = Integer.parseInt(dataSnapshot.child(Room.COLOR).getValue().toString());
                    List<UUID> gadgets = new ArrayList<UUID>();
                    if(dataSnapshot.child(Room.GADGETS).hasChildren())
                    {
                        for(DataSnapshot snapshot : dataSnapshot.child(Room.GADGETS).getChildren())
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
                    update(new Room(id, name, color, gadgets));
                }
                else
                {
                    Log.e(TAG, "Rooms - missing data");
                }
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                Log.i("room", "removed");
                if(dataSnapshot.hasChild(Room.NAME) && dataSnapshot.hasChild(Room.COLOR))
                {
                    String id = dataSnapshot.getKey();
                    String name = dataSnapshot.child(Room.NAME).getValue().toString();
                    int color = Integer.parseInt(dataSnapshot.child(Room.COLOR).getValue().toString());
                    List<UUID> gadgets = new ArrayList<UUID>();
                    if(dataSnapshot.child(Room.GADGETS).hasChildren())
                    {
                        for(DataSnapshot snapshot : dataSnapshot.child(Room.GADGETS).getChildren())
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
                    delete(new Room(id, name, color, gadgets));
                }
                else
                {
                    Log.e(TAG, "Rooms - missing data");
                }
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
            }
        });

        TimerTask gadgetTask = new TimerTask() {
            @Override
            public void run() {
                homeReference.child(Home.GADGETS).addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        Log.i("gadget", "added");
                        if(dataSnapshot.hasChild(Gadget.ROOM_ID) && dataSnapshot.hasChild(Gadget.NAME) && dataSnapshot.hasChild(Gadget.MAC_ADDRESS) && dataSnapshot.hasChild(Gadget.TYPE) && dataSnapshot.hasChild(Gadget.CHANNELS) && dataSnapshot.hasChild(Gadget.INSTALLED) && dataSnapshot.hasChild(Gadget.ICON) && dataSnapshot.hasChild(Gadget.FIRMWARE))
                        {
                            try
                            {
                                UUID id = UUID.fromString(dataSnapshot.getKey());
                                String roomId = dataSnapshot.child(Gadget.ROOM_ID).getValue().toString();
                                Room room = homeManager.home.getRoom(roomId);
                                String name = dataSnapshot.child(Gadget.NAME).getValue().toString();
                                String MAC = dataSnapshot.child(Gadget.MAC_ADDRESS).getValue().toString();
                                Gadget.GadgetType type = Gadget.GadgetType.valueOf(dataSnapshot.child(Gadget.TYPE).getValue().toString());
                                int parameter = Integer.parseInt(dataSnapshot.child(Gadget.CHANNELS).getValue().toString());
                                boolean installed = Boolean.parseBoolean(dataSnapshot.child(Gadget.INSTALLED).getValue().toString());
                                int icon = Integer.parseInt(dataSnapshot.child(Gadget.ICON).getValue().toString());
                                int firmware = Integer.parseInt(dataSnapshot.child(Gadget.FIRMWARE).getValue().toString());
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

                    @Override
                    public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                        Log.i("gadget", "changed");
                        if(dataSnapshot.hasChild(Gadget.ROOM_ID) && dataSnapshot.hasChild(Gadget.NAME) && dataSnapshot.hasChild(Gadget.MAC_ADDRESS) && dataSnapshot.hasChild(Gadget.TYPE) && dataSnapshot.hasChild(Gadget.CHANNELS) && dataSnapshot.hasChild(Gadget.INSTALLED) && dataSnapshot.hasChild(Gadget.ICON) && dataSnapshot.hasChild(Gadget.FIRMWARE))
                        {
                            try
                            {
                                UUID id = UUID.fromString(dataSnapshot.getKey());
                                String roomId = dataSnapshot.child(Gadget.ROOM_ID).getValue().toString();
                                Room room = homeManager.home.getRoom(roomId);
                                String name = dataSnapshot.child(Gadget.NAME).getValue().toString();
                                String MAC = dataSnapshot.child(Gadget.MAC_ADDRESS).getValue().toString();
                                Gadget.GadgetType type = Gadget.GadgetType.valueOf(dataSnapshot.child(Gadget.TYPE).getValue().toString());
                                int parameter = Integer.parseInt(dataSnapshot.child(Gadget.CHANNELS).getValue().toString());
                                boolean installed = Boolean.parseBoolean(dataSnapshot.child(Gadget.INSTALLED).getValue().toString());
                                int icon = Integer.parseInt(dataSnapshot.child(Gadget.ICON).getValue().toString());
                                int firmware = Integer.parseInt(dataSnapshot.child(Gadget.FIRMWARE).getValue().toString());
                                update(GadgetFactory.create(id, room, name, MAC, type, parameter, installed, icon, firmware));
                            }
                            catch (Exception ex)
                            {
                                Log.e(TAG, "Gadget - UUID parse error", ex);
                            }
                        }
                        else
                        {
                            Log.e(TAG, "Gadget - missing data");
                        }
                    }

                    @Override
                    public void onChildRemoved(DataSnapshot dataSnapshot) {
                        Log.i("gadget", "removed");
                        if(dataSnapshot.hasChild(Gadget.ROOM_ID) && dataSnapshot.hasChild(Gadget.NAME) && dataSnapshot.hasChild(Gadget.MAC_ADDRESS) && dataSnapshot.hasChild(Gadget.TYPE) && dataSnapshot.hasChild(Gadget.CHANNELS) && dataSnapshot.hasChild(Gadget.INSTALLED) && dataSnapshot.hasChild(Gadget.ICON) && dataSnapshot.hasChild(Gadget.FIRMWARE))
                        {
                            try
                            {
                                UUID id = UUID.fromString(dataSnapshot.getKey());
                                String roomId = dataSnapshot.child(Gadget.ROOM_ID).getValue().toString();
                                Room room = homeManager.home.getRoom(roomId);
                                String name = dataSnapshot.child(Gadget.NAME).getValue().toString();
                                String MAC = dataSnapshot.child(Gadget.MAC_ADDRESS).getValue().toString();
                                Gadget.GadgetType type = Gadget.GadgetType.valueOf(dataSnapshot.child(Gadget.TYPE).getValue().toString());
                                int parameter = Integer.parseInt(dataSnapshot.child(Gadget.CHANNELS).getValue().toString());
                                boolean installed = Boolean.parseBoolean(dataSnapshot.child(Gadget.INSTALLED).getValue().toString());
                                int icon = Integer.parseInt(dataSnapshot.child(Gadget.ICON).getValue().toString());
                                int firmware = Integer.parseInt(dataSnapshot.child(Gadget.FIRMWARE).getValue().toString());
                                delete(GadgetFactory.create(id, room, name, MAC, type, parameter, installed, icon, firmware));
                            }
                            catch (Exception ex)
                            {
                                Log.e(TAG, "Gadget - UUID parse error", ex);
                            }
                        }
                        else
                        {
                            Log.e(TAG, "Gadget - missing data");
                        }
                    }

                    @Override
                    public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                    }

                    @Override
                    public void onCancelled(FirebaseError firebaseError) {
                    }
                });
            }
        };
        Timer timerGadget = new Timer();
        timerGadget.schedule(gadgetTask, 1000);

        TimerTask taskScenes = new TimerTask() {
            @Override
            public void run() {
                homeReference.child(Home.SCENES).addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        Log.i("scene", "added");
                        if(dataSnapshot.hasChild(Scene.NAME) && (dataSnapshot.hasChild(Scene.SUBSCENES) || dataSnapshot.hasChild(Scene.ACTIONS)))
                        {
                            String id = dataSnapshot.getKey();
                            String name = dataSnapshot.child(Scene.NAME).getValue().toString();
                            SceneBuilder builder = new SceneBuilder(homeManager.getHome());
                            builder.create(id, name);

                            /**
                             * essential action data encapsulation
                             * and passing it to scene builder
                             */
                            List<ActionMessage> actions = new ArrayList<ActionMessage>();
                            if(dataSnapshot.hasChild(Scene.ACTIONS))
                            {
                                for(DataSnapshot actionSnapshot : dataSnapshot.child(Scene.ACTIONS).getChildren())
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
                            if(dataSnapshot.hasChild(Scene.SUBSCENES))
                            {
                                for(DataSnapshot subsceneSnapshot : dataSnapshot.child(Scene.SUBSCENES).getChildren())
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
                            if(dataSnapshot.hasChild(Scene.TRIGGERS))
                            {
                                for(DataSnapshot triggerSnapshot : dataSnapshot.child(Scene.TRIGGERS).getChildren())
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

                    @Override
                    public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                        Log.i("scene", "changed");
                        if(dataSnapshot.hasChild(Scene.NAME) && (dataSnapshot.hasChild(Scene.SUBSCENES) || dataSnapshot.hasChild(Scene.ACTIONS)))
                        {
                            String id = dataSnapshot.getKey();
                            String name = dataSnapshot.child(Scene.NAME).getValue().toString();
                            SceneBuilder builder = new SceneBuilder(homeManager.getHome());
                            builder.create(id, name);
                            List<Trigger> triggers = new ArrayList<Trigger>();

                            /**
                             * essential action data encapsulation
                             * and passing it to scene builder
                             */

                            List<ActionMessage> actions = new ArrayList<ActionMessage>();
                            if(dataSnapshot.hasChild(Scene.ACTIONS))
                            {
                                for(DataSnapshot actionSnapshot : dataSnapshot.child(Scene.ACTIONS).getChildren())
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
                            if(dataSnapshot.hasChild(Scene.SUBSCENES))
                            {
                                for(DataSnapshot subsceneSnapshot : dataSnapshot.child(Scene.SUBSCENES).getChildren())
                                {
                                    subscenes.add(subsceneSnapshot.child(Scene.ID).getValue().toString());
                                }
                            }
                            builder.addSubscenes(subscenes);

                            /**
                             * first step of trigger creation
                             * for next step passing triggers list to scene builder
                             */
                            if(dataSnapshot.hasChild(Scene.TRIGGERS))
                            {
                                for(DataSnapshot triggerSnapshot : dataSnapshot.child(Scene.TRIGGERS).getChildren())
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
                            update(builder.getScene());
                        }
                        else
                        {
                            Log.e(TAG, "Scene - missing data");
                        }
                    }

                    @Override
                    public void onChildRemoved(DataSnapshot dataSnapshot) {
                        Log.i("scene", "removed");
                        if(dataSnapshot.hasChild(Scene.NAME) && (dataSnapshot.hasChild(Scene.SUBSCENES) || dataSnapshot.hasChild(Scene.ACTIONS)))
                        {
                            String id = dataSnapshot.getKey();
                            String name = dataSnapshot.child(Scene.NAME).getValue().toString();
                            SceneBuilder builder = new SceneBuilder(homeManager.getHome());
                            builder.create(id, name);
                            List<Trigger> triggers = new ArrayList<Trigger>();

                            /**
                             * essential action data encapsulation
                             * and passing it to scene builder
                             */
                            List<ActionMessage> actions = new ArrayList<ActionMessage>();
                            if(dataSnapshot.hasChild(Scene.ACTIONS))
                            {
                                for(DataSnapshot actionSnapshot : dataSnapshot.child(Scene.ACTIONS).getChildren())
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
                            if(dataSnapshot.hasChild(Scene.SUBSCENES))
                            {
                                for(DataSnapshot subsceneSnapshot : dataSnapshot.child(Scene.SUBSCENES).getChildren())
                                {
                                    subscenes.add(subsceneSnapshot.child(Scene.ID).getValue().toString());
                                }
                            }
                            builder.addSubscenes(subscenes);

                            /**
                             * first step of trigger creation
                             * for next step passing triggers list to scene builder
                             */
                            if(dataSnapshot.hasChild(Scene.TRIGGERS))
                            {
                                for(DataSnapshot triggerSnapshot : dataSnapshot.child(Scene.TRIGGERS).getChildren())
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
                            delete(builder.getScene());
                        }
                        else
                        {
                            Log.e(TAG, "Scene - missing data");
                        }
                    }

                    @Override
                    public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                    }

                    @Override
                    public void onCancelled(FirebaseError firebaseError) {
                    }
                });
            }
        };
        Timer timerScenes = new Timer();
        timerScenes.schedule(taskScenes, 2000);

        TimerTask taskSchedules = new TimerTask() {
            @Override
            public void run() {
                homeReference.child(Home.SCHEDULE).addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        Log.i("schedule", "added");
                        if(dataSnapshot.hasChild(ScheduledScene.SCENE) && dataSnapshot.hasChild(ScheduledScene.HOUR) && dataSnapshot.hasChild(ScheduledScene.MINUTES) && dataSnapshot.hasChild(ScheduledScene.DAYS_OF_WEEK))
                        {
                            try
                            {
                                String id = dataSnapshot.getKey();
                                String scene = dataSnapshot.child(ScheduledScene.SCENE).getValue().toString();
                                int hour = Integer.parseInt(dataSnapshot.child(ScheduledScene.HOUR).getValue().toString());
                                int minutes = Integer.parseInt(dataSnapshot.child(ScheduledScene.MINUTES).getValue().toString());

                                GenericTypeIndicator<List<String>> t = new GenericTypeIndicator<List<String>>() {
                                };
                                List<String> daysList = dataSnapshot.child(ScheduledScene.DAYS_OF_WEEK).getValue(t);

                                boolean[] daysOfWeek = new boolean[7];
                                for(int i = 0; i < daysList.size(); i++)
                                {
                                    String s1 = daysList.get(i);
                                    daysOfWeek[i] = Boolean.parseBoolean(s1);

                                }
                                HashMap<String, String> conditions = new HashMap<String, String>();
                                if(dataSnapshot.hasChild(ScheduledScene.CONDITIONS))
                                {
                                    for(DataSnapshot snapshot : dataSnapshot.child(ScheduledScene.CONDITIONS).getChildren())
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

                    @Override
                    public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                        Log.i("schedule", "changed");
                        Log.d(TAG, "scheduled changed "+dataSnapshot.getKey());
                        if(dataSnapshot.hasChild(ScheduledScene.SCENE) && dataSnapshot.hasChild(ScheduledScene.HOUR) && dataSnapshot.hasChild(ScheduledScene.MINUTES) && dataSnapshot.hasChild(ScheduledScene.DAYS_OF_WEEK))
                        {
                            try
                            {
                                String id = dataSnapshot.getKey();
                                String scene = dataSnapshot.child(ScheduledScene.SCENE).getValue().toString();
                                int hour = Integer.parseInt(dataSnapshot.child(ScheduledScene.HOUR).getValue().toString());
                                int minutes = Integer.parseInt(dataSnapshot.child(ScheduledScene.MINUTES).getValue().toString());

                                GenericTypeIndicator<List<String>> t = new GenericTypeIndicator<List<String>>() {
                                };
                                List<String> daysList = dataSnapshot.child(ScheduledScene.DAYS_OF_WEEK).getValue(t);

                                boolean[] daysOfWeek = new boolean[7];
                                for(int i = 0; i < daysList.size(); i++)
                                {
                                    String s1 = daysList.get(i);
                                    daysOfWeek[i] = Boolean.parseBoolean(s1);

                                }
                                HashMap<String, String> conditions = new HashMap<String, String>();
                                if(dataSnapshot.hasChild(ScheduledScene.CONDITIONS))
                                {
                                    for(DataSnapshot snapshot : dataSnapshot.child(ScheduledScene.CONDITIONS).getChildren())
                                    {
                                        if(snapshot.hasChild(ScheduledScene.CONDITION_TYPE) && snapshot.hasChild(ScheduledScene.CONDITION_VALUE))
                                        {
                                            conditions.put(snapshot.child(ScheduledScene.CONDITION_TYPE).getValue().toString(), snapshot.child(ScheduledScene.CONDITION_VALUE).getValue().toString());
                                        }
                                    }
                                }
                                update(new ScheduledScene(id, scene, hour, minutes, daysOfWeek, conditions));
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

                    @Override
                    public void onChildRemoved(DataSnapshot dataSnapshot) {
                        Log.i("schedule", "removed");
                        if(dataSnapshot.hasChild(ScheduledScene.SCENE) && dataSnapshot.hasChild(ScheduledScene.HOUR) && dataSnapshot.hasChild(ScheduledScene.MINUTES) && dataSnapshot.hasChild(ScheduledScene.DAYS_OF_WEEK))
                        {
                            try
                            {
                                String id = dataSnapshot.getKey();
                                String scene = dataSnapshot.child(ScheduledScene.SCENE).getValue().toString();
                                int hour = Integer.parseInt(dataSnapshot.child(ScheduledScene.HOUR).getValue().toString());
                                int minutes = Integer.parseInt(dataSnapshot.child(ScheduledScene.MINUTES).getValue().toString());

                                GenericTypeIndicator<List<String>> t = new GenericTypeIndicator<List<String>>() {
                                };
                                List<String> daysList = dataSnapshot.child(ScheduledScene.DAYS_OF_WEEK).getValue(t);

                                boolean[] daysOfWeek = new boolean[7];
                                for(int i = 0; i < daysList.size(); i++)
                                {
                                    String s1 = daysList.get(i);
                                    daysOfWeek[i] = Boolean.parseBoolean(s1);

                                }
                                HashMap<String, String> conditions = new HashMap<String, String>();
                                if(dataSnapshot.hasChild(ScheduledScene.CONDITIONS))
                                {
                                    for(DataSnapshot snapshot : dataSnapshot.child(ScheduledScene.CONDITIONS).getChildren())
                                    {
                                        if(snapshot.hasChild(ScheduledScene.CONDITION_TYPE) && snapshot.hasChild(ScheduledScene.CONDITION_VALUE))
                                        {
                                            conditions.put(snapshot.child(ScheduledScene.CONDITION_TYPE).getValue().toString(), snapshot.child(ScheduledScene.CONDITION_VALUE).getValue().toString());
                                        }
                                    }
                                }
                                delete(new ScheduledScene(id, scene, hour, minutes, daysOfWeek, conditions));
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

                    @Override
                    public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                    }

                    @Override
                    public void onCancelled(FirebaseError firebaseError) {
                    }
                });
            }
        };
        Timer timerSchedules = new Timer();
        timerSchedules.schedule(taskSchedules, 3000);
    }
}
